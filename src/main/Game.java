package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import main.Boat.boatType;

public class Game {
	// board constants
	private static int BOARD_SIZE = 10;
	private static int BOAT_AMOUNT = 5;

	private Scanner scanner;

	// networking stuff
	private String host;
	private Socket socket;
	private PrintStream streamOut;
	private BufferedReader streamIn;

	// game data boards and pieces
	private boolean running;
	private boolean setup;
	private boolean gameOver;
	private Board enemyBoard;
	private Board ownBoard;
	private ArrayList<Boat> boats;
	private boolean attacking;

	// setup variables
	int boatToPlace;
	Boat.boatType[] arr;

	public Game() {
		enemyBoard = new Board(BOARD_SIZE,BOARD_SIZE);
		ownBoard = new Board(BOARD_SIZE,BOARD_SIZE);
		boats = new ArrayList<Boat>();

		scanner = new Scanner(System.in);
		boatToPlace = 0;
		arr = new Boat.boatType[]{boatType.BIG_OL_BOAT, boatType.PLANE_HOLDY_BOAT, boatType.LIL_TINY_BOAT};

		running = true;
		setup = false;
		gameOver = false;

		mainLoop();
	}

	public void mainLoop(){
		while(running) {
			try{
				System.out.println(setup);
				if(!setup)
					setUp();
				else
					game();
				if(gameOver)
					end();
			} catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("That boat does not fit on the board. Try again");
			} catch (IOException e) {
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setUp() throws IOException, NumberFormatException{
		if(boats.size()>=BOAT_AMOUNT) {
			setup = true;
			return;
		}

		if(host==null) {
			System.out.println("Are you hosting this game? (y/n)");
			String raw = scanner.nextLine();
			if(raw.equals("y")) {
				host = "hosting";
				socket = new ServerSocket(8888).accept();

				System.out.println("Connected to " + socket.getInetAddress().getHostName()); 
				streamOut = new PrintStream(socket.getOutputStream());
				streamIn = new BufferedReader (new InputStreamReader(socket.getInputStream()));

				attacking = Math.random()>.55 ? true : false;
				if(attacking) send("r");
				else send("a");
			}
			else {
				System.out.println("What is the adress you would like to connect to?");
				host = scanner.nextLine();
				socket = new Socket(host,8888);

				System.out.println("Connected to " + socket.getInetAddress().getHostName()); 
				streamOut = new PrintStream(socket.getOutputStream());
				streamIn = new BufferedReader (new InputStreamReader(socket.getInputStream()));

				String msg = recieve();
				attacking = msg.equals("a") ? true : false;
				System.out.println(attacking);
			}

		}
		if(boatToPlace==0) ownBoard.drawBoard();
//		if(boatToPlace<BOAT_AMOUNT) setup = false;
		String raw = "";
		int x = 0;
		int y = 0;
		int boatIndex = boatToPlace;
		if(boatToPlace>=arr.length) boatIndex = arr.length-1;
		String s = boatToPlace < BOAT_AMOUNT-1 ? "s " : " ";
		System.out.println("You have " + (BOAT_AMOUNT-boatToPlace) + " boat" + s + "to place.");
		System.out.println("Where would you like to place the " + Boat.BOAT_INFO[boatIndex*2] + "? " + Boat.BOAT_INFO[boatIndex*2+1]);
		raw = scanner.nextLine();
		int charIndex = 0;
		for(int i=0; i<raw.length(); i++) {
			if(!Character.isLetter(raw.charAt(i))) {
				charIndex = i;
				break;
			}
		}
		x = (int)Character.toUpperCase(raw.charAt(0))-65;
		y = Integer.parseInt(raw.substring(charIndex,raw.length()))-1;

		System.out.println("Would you like the boat vertical or horizontal? (v/h)");
		raw = scanner.nextLine();
		boolean vertical = true;
		if(raw.equals("h")) {vertical = false;}

		Boat newBoat = new Boat(x,y,vertical,arr[boatIndex]);
	
		for(int i = newBoat.xPos(); i < newBoat.xPos()+newBoat.LENGTH; i++) {
			for(int j = newBoat.yPos(); j < newBoat.yPos()+newBoat.WIDTH; j++) {
				if(hitBoat(i,j)) {
					System.out.println("Sorry, there is already a boat there. Place it somewhere else");
					return;
				}
			}
		}

		boats.add(newBoat);
		ownBoard.setBoat(boats.get(boats.size()-1));
		ownBoard.drawBoard();
		boatToPlace++;
	}

	public void game() throws IOException {
		if(attacking) {
			System.out.println("Where would you like to attack?");
			String raw = scanner.nextLine();
			int charIndex = 0;
			for(int i=0; i<raw.length(); i++) {
				if(!Character.isLetter(raw.charAt(i))) {
					charIndex = i;
					break;
				}
			}
			int x = (int)Character.toUpperCase(raw.charAt(0))-65;
			int y = Integer.parseInt(raw.substring(1,raw.length()))-1;

			send(x+","+y);
			boolean hit = recieve().equals("true") ? true : false;
			if(hit) {
				enemyBoard.hitCell(x, y, 'x');
				System.out.println("Hit");
			}
			else {
				enemyBoard.hitCell(x, y, 'o');
				System.out.println("Miss");
			}
			enemyBoard.drawBoard();
		}
		else {
			System.out.println("Wait for opponent");
			String raw = recieve();
			int x = Integer.parseInt(raw.substring(0,raw.indexOf(",")));
			int y = Integer.parseInt(raw.substring(raw.indexOf(",")+1,raw.length()));
			boolean hit = hitBoat(x,y);
			send(Boolean.toString(hit));
			if(hit)System.out.println("The enemy hit at " + (char)(x+65) + "," + y+1);
			else System.out.println("The enemy missed");
		}
		attacking = !attacking;
	}

	public void end() {

	}

	public void AIMove(){

	}

	private void send(String msg) {
		streamOut.println(msg);
	}

	private String recieve() throws IOException {
		return streamIn.readLine();
	}

	public boolean hitBoat(int xPos, int yPos){
		for(Boat boat: boats) {
			if(boat.hit(xPos, yPos)) {
				return true;
			}
		}
		return false;
	}

	public boolean allBoatsSunk(){
		for(Boat boat: boats) {
			if(!boat.sunk()) return false;
		}
		return true;
	}

}
