package main;

public class Boat {
	
	public static final Boat BIG_OL_BOAT = new Boat(5);
	public static final Boat LIL_TINY_BOAT = new Boat(2);
	
	public final int LENGTH;
	private int[] x;
	private int[] y;
	
	public Boat(int length) {
		this.LENGTH = length;
	}

	public Boat(int xPos, int yPos, boolean vertical, Boat boat) {
		this.LENGTH = boat.LENGTH;
		x = vertical ? new int[1] : new int[LENGTH]; 
		y = vertical ? new int[LENGTH] : new int[1]; 
		x[0] = xPos;
		x[0] = yPos;
	}
	
	public void hit() {
		
	}
	
}
