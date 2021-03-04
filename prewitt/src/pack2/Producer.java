package pack2;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Producer extends ThreadUser{
	private String[] filePaths;
	private int currentIndex;
	private Buffer buffer;
	private List<Integer> pixels;
	
	public Producer(Buffer buffer, String ...filePaths) {// vararg for paths
		//initializations
		this.buffer = buffer;
		this.filePaths = filePaths;
	}
	
	public void start() {
		super.start(); //starts the thread
		System.out.println("Starting Producer");
	}
	
	@Override
	public void run() {
		System.out.println("Running producer");
		
		for(int fileIndex = 0; fileIndex < this.filePaths.length ; fileIndex++) {//for every file path 
			this.currentIndex = fileIndex; //saves the index for the current file path
			
			this.processImage(); //process the image
			
			if(pixels != null) { //if the process did not return any pixels, skip this file
				buffer.initBuffer(getHeight(), getWidth()); //init the buffer with data about the image
				
				int quarter = getHeight() * getWidth() /4; //compute what 1/4 of all the pixels mean 
				
				for (int k = 0; k < 4; k++) { //for every quarter 
					if(k == 3) { 
						buffer.put(pixels.subList(k * quarter, getHeight()*getWidth()-1)); //puts the current quarter of data
					}
					else { //if division has a remainder -> the last quarter includes all the extra pixels (of the remainder) 
						buffer.put(pixels.subList( k * quarter, k * quarter + quarter-1)); //puts the last quarter of the data
					}
					
					System.out.println("Producer put " +  (k+1) + "/4 of image ");
					
					try {
						Thread.sleep(1000); // sleeps for 1 sec
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		System.out.println("Producer is dead");
	}
	
	@Override
	public void processImage() {
		BufferedImage img = null;
		long startTime = System.currentTimeMillis(); // the time at which the function started
		
		try {	//reading image from the current path
		    img = ImageIO.read(new File(this.filePaths[this.currentIndex]));
		} catch (IOException e) {}
		
		if(img == null ) { // if image was not found -> the matrix of pixels is null and the function ends
			System.out.println("No image found!");
			pixels = null;
			return ;
		}
		
		long elapsedTime = System.currentTimeMillis() - startTime; // the elapsed time it took to read the image
		System.out.println("Image reading - " + (double)elapsedTime/1000 + " seconds");
		
		this.setWidth(img.getWidth(null)); // sets width using the parent class setter
	    this.setHeight(img.getHeight(null)); // sets height - '' - '' -
	    
	    this.pixels = new ArrayList<Integer>(); // initializing the list of pixels 
	    
	    // reading all the pixels from the BufferedImage and copying them in the ArrayList pixels one by one
	    for (int i = 0; i < getWidth(); i++) {
	        for (int j = 0; j < getHeight(); j++) {
	            pixels.add(img.getRGB(i, j));
	        }
	    }
	}


}
