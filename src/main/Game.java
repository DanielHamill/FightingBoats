package main;

import java.util.Scanner;

public class Game {
	
	private enum State {SETUP, GAME, END};
	private Scanner scanner;
	
	private boolean running;
	private String hostName;
	private State state;
	
	public Game() {
		running = true;
		state = State.SETUP;
		mainLoop();
	}
	
	public void mainLoop(){
		scanner = new Scanner(System.in);
		while(running) {
			switch(state) {
				case SETUP: setUp();
					break;
				case GAME: game();
					break;
				case END: end();
					break;
				default: return;
			}
		}
	}
	
	public void setUp() {
		String raw = "";
		int x = 0;
		int y = 0;
		System.out.println("Where would you like to place the ship?");
		raw = scanner.nextLine();
		int charIndex = 0;
		for(int i=0; i<raw.length(); i++) {
			if(Character.isLetter(raw.charAt(i))) {
				charIndex = i;
				break;
			}
		}
		x = Integer.parseInt(raw.substring(0,charIndex));
//		y = Integer.parseInt(Character.toUpraw.substring(charIndex,raw.length()));
	}
	
	public void game() {
		
	}

	public void end() {
		
	}
	
	public void AIMove(){
		
	}
	
}
