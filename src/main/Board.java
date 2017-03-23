package main;

import java.util.ArrayList;

public class Board {
	
	private ArrayList<Boat> boats;
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
		for(int i: boat.getX()) {
			for(int j: boat.getY()) {
				board[j][i] = boat.PIECE;
			}
		}
	}
	
	public void drawBoard() {
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				if(j==0) System.out.print(i);
				
				System.out.print("[" + board[i][j] + "]");
			}
			System.out.println();
		}
	}
	
	public boolean hitCell(int xPos, int yPos){
		return false;
		
	}
	
	public boolean allBoatsSunk(){
		return false;
		
	}
	
}
