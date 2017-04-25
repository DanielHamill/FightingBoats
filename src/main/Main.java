package main;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		String input = "";
		while(!input.equals("no")) {
			Game game = new Game();
			System.out.println("Do you want to play again?");
			input = scanner.nextLine();
		}
	}
	
}
