package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.plaf.synth.SynthSeparatorUI;

import main.Boat.boatType;

public class Game {
	// board constants
	private static int BOARD_SIZE = 10;
	private static int BOAT_AMOUNT = 1;

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
		//initialize game elements
		enemyBoard = new Board(BOARD_SIZE,BOARD_SIZE);
		ownBoard = new Board(BOARD_SIZE,BOARD_SIZE);
		boats = new ArrayList<Boat>();

		//game utilities
		scanner = new Scanner(System.in);
		boatToPlace = 0;
		arr = new Boat.boatType[]{boatType.BIG_OL_BOAT, boatType.PLANE_HOLDY_BOAT, boatType.LIL_TINY_BOAT};

		//game data
		running = true;
		setup = false;
		gameOver = false;

		mainLoop();
	}

	public void mainLoop(){
		while(running) {
			try{
				//main game loop
				if(!setup)
					setUp();
				else if(!gameOver)
					game();
				else
					end();
				//Exception catching
			} catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("That boat does not fit on the board. Try again");
//				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Sorry, you are experiencing connection issues");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setUp() throws IOException, NumberFormatException{
		//setup, handles connection, and board setup
		if(boats.size()>=BOAT_AMOUNT) {
			setup = true;
			return;
		}

		if(host==null) {
			//setting up the sockets and connecting the players
			System.out.println("Are you hosting this game? (y/n)");
			String raw = scanner.nextLine();
			if(raw.equals("y")) {
				//handles setup for the server
				host = "hosting";
				socket = new ServerSocket(8888).accept();

				System.out.println("Connected to " + socket.getInetAddress().getHostName()); 
				streamOut = new PrintStream(socket.getOutputStream());
				streamIn = new BufferedReader (new InputStreamReader(socket.getInputStream()));

				attacking = Math.random()>.55 ? true : false;
				if(attacking) send("r");
				else send("a");
			}
			else if(raw.equals("n")){
				//handes setup for the client
				System.out.println("What is the adress you would like to connect to?");
				host = scanner.nextLine();
				socket = new Socket(host,8888);

				System.out.println("Connected to " + socket.getInetAddress().getHostName()); 
				streamOut = new PrintStream(socket.getOutputStream());
				streamIn = new BufferedReader (new InputStreamReader(socket.getInputStream()));

				String msg = recieve();
				attacking = msg.equals("a") ? true : false;
			}
			else {
				System.out.println("Sorry, please only input \"y\" or \"n\"");
				return;
			}

		}
		//places boats
		if(boatToPlace==0) ownBoard.drawBoard();
		String raw = "";
		int x = 0;
		int y = 0;
		
		//keeps track of the boats that need to be placed
		int boatIndex = boatToPlace;
		if(boatToPlace>=arr.length) boatIndex = arr.length-1;
		String s = boatToPlace < BOAT_AMOUNT-1 ? "s " : " ";
		System.out.println("You have " + (BOAT_AMOUNT-boatToPlace) + " boat" + s + "to place.");
		System.out.println("Where would you like to place the " + Boat.BOAT_INFO[boatIndex*2] + "? " + Boat.BOAT_INFO[boatIndex*2+1]);
		
		//gets coordinates from player
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

		//creates boat object and places on board
		System.out.println("Would you like the boat vertical or horizontal? (v/h)");
		raw = scanner.nextLine();
		boolean vertical = true;
		if(raw.equals("h")) {vertical = false;}

		Boat newBoat = new Boat(x,y,vertical,arr[boatIndex]);
	
		for(int i = newBoat.xPos(); i < newBoat.xPos()+newBoat.LENGTH; i++) {
			for(int j = newBoat.yPos(); j < newBoat.yPos()+newBoat.WIDTH; j++) {
				if(hitBoat(i,j)) {
					//ERROR CHECKING: returns from method in loop
					System.out.println("Sorry, there is already a boat there. Place it somewhere else");
					return;
				}
				char ch = ownBoard.getBoard()[x][y];
			}
		}

		boats.add(newBoat);
		ownBoard.setBoat(boats.get(boats.size()-1));
		ownBoard.drawBoard();
		boatToPlace++;
	}

	public void game() throws IOException {
		//main game loop between users
		if(attacking) {
			//handles attacking
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
			
			// ERROR CHECKING: OFFBOARD
			char ch = ownBoard.getBoard()[x][y];
			
			// ERROR CHECKING: ALREADY ATTACKED
			if(enemyBoard.getBoard()[x][y] == 'x' || enemyBoard.getBoard()[x][y] == 'o') {
				System.out.println("You already attacked there, try again");
				return;
			}

			send(x+","+y);
			boolean hit = recieve().equals("true") ? true : false;
			if(recieve().equals("true")) {
				gameOver = true;
			}
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
			//handes receiving
			System.out.println("Wait for opponent");
			String raw = recieve();
			int x = Integer.parseInt(raw.substring(0,raw.indexOf(",")));
			int y = Integer.parseInt(raw.substring(raw.indexOf(",")+1,raw.length()));
			boolean hit = hitBoat(x,y);
			send(Boolean.toString(hit));
			if(hit)System.out.println("The enemy hit at " + (char)(x+65) + "," + y+1);
			else System.out.println("The enemy missed");
			send(Boolean.toString(allBoatsSunk()));
			if(allBoatsSunk()) gameOver = true;
		}
		attacking = !attacking;
		
	}

	public void end() throws IOException {
		String outcome = allBoatsSunk() ? "lost..." : "won!";
		System.out.println("Game Over, you " + outcome);
		socket.close();
		running = false;
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
