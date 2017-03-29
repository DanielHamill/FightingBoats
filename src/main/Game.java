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
		mainLoop();
	}
	
	public void mainLoop(){
		while(running) {
			try{
				setUp();
				game();
				end();
			} catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("That boat does not fit on the board. Try again");
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}
	}
	
	public void setUp() throws IOException {
		if(boats.size()>=BOAT_AMOUNT) return;
		
		if(host==null) {
			System.out.println("Are you hosting this game? (y/n)");
			String raw = scanner.nextLine();
			if(raw.equals("y")) {
				host = "hosting";
				socket = new ServerSocket(8080).accept();
				attacking = Math.random()>.55 ? true : false;
				if(attacking) send("r");
				else send("a");
			}
			else {
				System.out.println("What is the adress you would like to connect to?");
				host = scanner.nextLine();
				socket = new Socket(host,8080);
				String msg = recieve();
				attacking = msg.equals("a") ? true : false;
				System.out.println(attacking);
			}
			System.out.println("Connected to " + socket.getInetAddress().getHostName()); 
			streamOut = new PrintStream(socket.getOutputStream());
			streamIn = new BufferedReader (new InputStreamReader(socket.getInputStream()));
			
		}
		
		ownBoard.drawBoard();
		while(boatToPlace<BOAT_AMOUNT) {
			String raw = "";
			int x = 0;
			int y = 0;
			int boatIndex = boatToPlace;
			if(boatToPlace>=arr.length) boatIndex = arr.length-1;
			
			System.out.println("You have " + (BOAT_AMOUNT-boatToPlace) + " boats to place.");
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
			
			boats.add(new Boat(x,y,vertical,arr[boatIndex]));
			ownBoard.setBoat(boats.get(boats.size()-1));
			ownBoard.drawBoard();
			boatToPlace++;
		}
	}
	
	public void game() {
		if(host.equals("host")) send("a");
		
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
	
	public boolean hitCell(int xPos, int yPos){
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
