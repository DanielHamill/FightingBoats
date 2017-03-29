package main;

public class Boat {
	
	public static final Boat BIG_OL_BOAT = new Boat(5, 1, 'O');
	public static final Boat LIL_TINY_BOAT = new Boat(2, 1, 'o');
	public static final Boat PLAIN_HOLDY_BOAT = new Boat(3, 2, 's');

	public final int LENGTH;
	public final int WIDTH;
	public final char PIECE;
	private int xPos;
	private int yPos;
	private boolean[][] xy;
	
	public Boat(int length, int width, char p) {
		this.LENGTH = length;
		this.WIDTH = width;
		this.PIECE = p;
	}

	public Boat(int xPos, int yPos, boolean vertical, Boat boat) {
		this.LENGTH = boat.LENGTH;
		this.WIDTH = boat.WIDTH;
		this.PIECE = boat.PIECE;
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
