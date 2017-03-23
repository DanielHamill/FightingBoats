package main;

public class Boat {
	
	public static final Boat BIG_OL_BOAT = new Boat(5, 1, 'O');
	public static final Boat LIL_TINY_BOAT = new Boat(2, 1, 'o');
	public static final Boat PLAIN_HOLDY_BOAT = new Boat(3, 2, 's');
	
	public final int LENGTH;
	public final int WIDTH;
	public final char PIECE;
	private int[] x;
	private int[] y;
	
	public Boat(int length, int width, char p) {
		this.LENGTH = length;
		this.WIDTH = width;
		this.PIECE = p;
	}

	public Boat(int xPos, int yPos, boolean vertical, Boat boat) {
		this.LENGTH = boat.LENGTH;
		this.WIDTH = boat.WIDTH;
		this.PIECE = boat.PIECE;
		x = vertical ? new int[WIDTH] : new int[LENGTH]; 
		y = vertical ? new int[LENGTH] : new int[WIDTH];
		for(int i=0; i<x.length; i++) {
			x[i] = xPos+i;
		}
		for(int i=0; i<y.length; i++) {
			y[i] = yPos+i;
		}
	}
	
	public boolean hit(int xPos, int yPos) {
		int xHit = -1;
		int yHit = -1;
		for(int i=0; i<x.length; i++) {
			if(first(x[i])==xPos) xHit = i;
		}
		for(int i=0; i<y.length; i++) {
			if(first(y[i])==yPos) yHit = i;
		}
		if(xHit>-1&&yHit>-1) {
			if(x[xHit]*10==first(x[xHit])*(int)Math.pow(10, x.length)) {
				x[xHit] = -1;
			}
			else {
				x[xHit] *= 10;
			}
			
			if(y[yHit]*10==first(y[yHit])*(int)Math.pow(10, y.length)) {
				y[yHit] = -1;
			}
			else {
				y[yHit] *= 10;
			}
		}
		return (xHit>-1)&&(yHit>-1);
	}
	
	private int first(int num) {
		return Integer.parseInt(Integer.toString(num).substring(0));
	}
	
	public boolean sunk() {
		for(int i: x) {
			if(i!=-1) return false;
		}
		for(int i: y) {
			if(i!=-1) return false;
		}
		return true;
	}

	public int[] getX() {
		return x;
	}

	public int[] getY() {
		return y;
	}
	
}
