package main;

public class Main {
	
	public static void main(String[] args){
		Board board = new Board(10,10);
		board.setBoat(new Boat(0,5,true,Boat.LIL_TINY_BOAT));
		board.setBoat(new Boat(0,0,true,Boat.BIG_OL_BOAT));
		board.drawBoard();
	}
	
}
