package pack2;

import java.util.List;

public class Buffer {
	private List<Integer> pixels = null;
	private int height, width;
	private boolean available = false;
	
	public synchronized List<Integer> get () {// returns the current list of integers (pixels) hold in the pixels var
		while (!this.available ) {
			try {
				
				wait ();
				// waits for producer to put a value
			} catch (InterruptedException e) {
					e.printStackTrace ();
			}
		}
		this.available = false ;
		notifyAll ();
		return pixels;
	}
	
	public synchronized void put (List<Integer> pixels ) {// takes a list of integers as pixels and waits for it to be taken by the consumer
		while ( this.available ) {
			try {
				wait ();
			// wait for consumer to get a value
			} catch ( InterruptedException e) {
			e.printStackTrace ();
			}
		}
		this.pixels = pixels;
		this.available = true ;
		notifyAll ();
	}
	
	public void initBuffer(int height, int width) {//initializes the metadata about the image: height, width 
		this.height = height;
		this.width = width;
	}
	
	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
	
}
