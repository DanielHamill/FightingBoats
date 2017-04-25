package main;

import java.util.ArrayList;

public class Board {
	
	private char[][] board;
	private char hit;
	private char missed;
	private char boat;
	
	public Board(int length, int height) {
		board = new char[length][height];
		for(int i=0; i<length; i++) {
			for(int j=0; j<height; j++) {
				board[i][j] = ' ';
			}
		}
	}
	
	public void setBoat(Boat boat) {
		for(int x=boat.getDistance(); x>0; x--) {
			for(int y=boat.getHeight(); y>0; y--) {
				board[boat.xPos()+x-1][boat.yPos()+y-1] = boat.PIECE;
			}
		}
	}
	
	public void hitCell(int xPos, int yPos, char p) {
		board[xPos][yPos] = p;
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
	
	public int getWidth() {
		return board.length;
	}
	
	public int getHeight() {
		return board[0].length;
	}
	
	public char[][] getBoard() {
		return board;
	}
	
}
