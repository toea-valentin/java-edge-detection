package pack2;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Consumer extends ThreadUser{
	private Buffer buffer;
	private String[] filePaths;
	private int currentIndex;
	
	private List<Integer> pixelsList;
	private int[][] pixels;
	
	public Consumer(Buffer buffer, String ...filePaths) {
		//Initializations
		this.buffer = buffer;
		this.filePaths = filePaths;
		pixelsList = new ArrayList<Integer>();
	}
	
	public void start() {
		super.start(); //starts the thread
		System.out.println("Starting Consumer");
	}
	
	@Override
	public void run() {
		System.out.println("Running Consumer");
		for(int fileIndex = 0; fileIndex < this.filePaths.length; fileIndex++) { // for every file path 
			this.currentIndex = fileIndex; //saves the index for the current file path
			
			for (int k = 0; k < 4 ; k++) { //iterate over every quarter of an image received over the buffer
				List<Integer> temp = buffer.get(); //gets the quarter of data from buffer
				System.out.println("Consumer got " + (k+1) + "/4 of picture");
				for(int x : temp) { // adds every pixel from that quarter to a pixelsList( that will containg all quarters pixels)
					pixelsList.add(x);
				}
			}
			
			this.setHeight(buffer.getHeight()); //takes the sizing from the buffer
			this.setWidth(buffer.getWidth());
			
			pixels = new int[getWidth()][getHeight()]; //initializing the pixels matrix
			
			// transform the list of pixels in a matrix of pixels according to sizes
			for(int i = 0; i < getWidth(); i++) { 
				for(int j = 0 ; j < getHeight(); j++) {
					if(pixelsList.size() > 0) { // if there are pixels left 
						pixels[i][j] = pixelsList.get(0); //take the first pixel from the list
						pixelsList.remove(0); //remove the first pixel from list -> until the list is empty
					}
				}
			}
			
			this.processImage(); //processes the image
		}
		
		System.out.println("Consumer is dead");
		
	}
	
	@Override
	public void processImage() {
		long startTime = System.currentTimeMillis(); //the time at which the function started
		
		//Use the custom class PrewittSolver that operates on pixels accordingly to the algorithm used for edge recognition
		PrewittSolver pSolver = new PrewittSolver();
		pSolver.convertToGrayscale(pixels); //computes the pixels so they convert to grayscale
		int[][] new_pixels = pSolver.applyPrewittOperator(pixels); // applies the prewitt operator on the whole matrix
		new_pixels = pSolver.applyThreshold(new_pixels); // makes the image black/white using a threshold
		
		long elapsedTime = System.currentTimeMillis() - startTime; // elapsed time for the whole process used previously 
		System.out.println("Image processing - " + (double)elapsedTime/1000 + " seconds");
		
		startTime = System.currentTimeMillis(); //the time at which the writing of the image started
		
		//A buffered image is initialized with the sizes and values of the pixels matrix 
	    BufferedImage image = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_BYTE_BINARY);
	    for (int x = 0; x < getWidth(); x++) {
	        for (int y = 0; y < getHeight(); y++) {
	            image.setRGB(x, y, new_pixels[x][y]);
	        }
	    }
	    
	    //then the File class is used to write the buffered image 
	    File ImageFile = new File(this.filePaths[this.currentIndex]);
	    try {
	        ImageIO.write(image, "bmp", ImageFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    elapsedTime = System.currentTimeMillis() - startTime; // elapsed time for the whole writing process
		System.out.println("(File saved) Image writing - " + (double)elapsedTime/1000 + " seconds");
	}
	

}
