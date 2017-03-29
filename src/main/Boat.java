package main;

public class Boat {
	
	public enum boatType{BIG_OL_BOAT, PLANE_HOLDY_BOAT, LIL_TINY_BOAT};
	public static String[] BOAT_INFO = {"Big 'Ol Boat", "5x1", "Plane Holdy Boat", "3x2", "Lil Tiny Boat", "2x1"};

	public final int LENGTH;
	public final int WIDTH;
	public final char PIECE;
	private int xPos;
	private int yPos;
	private boolean[][] xy;
	
	public Boat(int xPos, int yPos, boolean vertical, boatType boat) {
		
		switch(boat) {
		case BIG_OL_BOAT:
			LENGTH = 5;
			WIDTH = 1;
			PIECE = 'O';
			break;
		case PLANE_HOLDY_BOAT:
			LENGTH = 3;
			WIDTH = 2;
			PIECE = '^';
			break;
		default:
			LENGTH = 2;
			WIDTH = 1;
			PIECE = 'o';
			break;
		}
		
		this.xPos = xPos;
		this.yPos = yPos;
		
		xy = vertical ? new boolean[WIDTH][LENGTH] : new boolean[LENGTH][WIDTH];
		for(int x=0; x<xy.length; x++) {
			for(int y=0; y<xy[x].length; y++) {
				xy[x][y] = false;
			}
		}
	}
	
	public boolean hit(int xPos, int yPos) {
		if(xPos < this.xPos || 
				xPos > this.xPos+xy.length-1 ||
				yPos < this.yPos ||
				yPos > this.yPos+xy[0].length-1) {
			return false;
		}
		xy[xPos-this.xPos][yPos-this.yPos] = true;
		return true;
	}
	
	public boolean sunk() {
		for(int x=0; x<xy.length; x++) {
			for(int y=0; y<xy[x].length; y++) {
				if(!xy[x][y]) return false;
			}
		}
		return true;
	}
	
	public int xPos() {
		return xPos;
	}
	
	public int yPos() {
		return yPos;
	}
	
	//NOTE: not the same as LENGTH
	public int getDistance() {
		return xy.length;
	}
	
	//NOTE: not the same as WIDTH
	public int getHeight() {
		return xy[0].length;
	}
	
}
