package main;

import java.util.ArrayList;

public class Board {
	
//	public ArrayList<Boat> boats;
	private char[][] board;
	private char hit;
	private char missed;
	private char boat;
	
	public Board(int length, int height) {
//		boats = new ArrayList<Boat>();
		board = new char[length][height];
		for(int i=0; i<length; i++) {
			for(int j=0; j<height; j++) {
				board[i][j] = ' ';
			}
		}
	}
	
	public void setBoat(Boat boat) {
		for(int x=boat.getDistance(); x>=0; x--) {
			for(int y=0; y<boat.getHeight(); y++) {
				board[boat.xPos()+x][boat.yPos()+y] = boat.PIECE;
			}
		}
	}
	
	public void drawBoard() {
		System.out.println("   A  B  C  D  E  F  G  H  I  J");
		
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				
				if(j==0 && i+1<10) System.out.print(" ");
				if(j==0) System.out.print(i+1);
				System.out.print("[" + board[j][i] + "]");
				
			}
			System.out.println();
		}
	}
	
}
