package pack2;
		
public class PrewittSolver {
	public void convertToGrayscale(int[][] pixels) { //transforms the image to a grayscale version
		int width = pixels.length; //image width
		int height = pixels[0].length; //image height
		
		for (int x = 0; x < width; x++) { // loop through each pixel
		        for (int y = 0; y < height; y++) { // -- 
		    	    int oldrgb = pixels[x][y]; // get the binary value of the pixel
		            int r = (oldrgb>>16)&0xFF; // get the red value from it through shifting and 'and'
		            int g = (oldrgb>>8)&0xFF; // get the green value
		            int b = (oldrgb>>0)&0xFF; //get the blue value
		    	    int gray = (r + g + b) / 3; //compute the gray value based on the red, green and blue values
		    	    int rgb = (oldrgb & 0xff000000) | (gray << 16) | (gray << 8) | gray; //init a new pixel based on the old one and fill it with the gray value
		    	    pixels[x][y] = rgb; // change the old pixel with the new one
		        }
		 }
	}
	
	public int[][] applyPrewittOperator(int[][] pixels) {
		int width = pixels.length;
		int height = pixels[0].length;
		int[][] new_pixels = new int[width][height]; 
		
		for(int x = 1; x < width-2; x++) //loop through each pixel
			for(int y = 1; y < height-2; y++)
				new_pixels[x+1][y+1] = applyMask(pixels,x,y); // for each pixel apply the Prewitt mask
		return new_pixels;
	}
	
	public int[][] applyThreshold(int[][] pixels) {//used for converting the current image to a black and white image based on a chosen threshold
		int width = pixels.length;				  //highlights the edges
		int height = pixels[0].length;
		int[][] new_pixels = new int[width][height];
		
		int threshold = 100; // arbitrary chosen threshold (value between 0-255) 
		
		for(int x = 0; x < width; x++) {//loop through each pixel
			for(int y = 0; y < height; y++) {
				int rgb = pixels[x][y]; //get the binary value of the pixel
	            int r = (rgb>>16)&0xFF; //get the red value (shift and and operators)
	            int g = (rgb>>8)&0xFF; //get the green value
	            int b = (rgb>>0)&0xFF; //get the blue value
	            if(r >= threshold || b >= threshold || g >= threshold) //if the rgb values >= threshold => the new pixel becomes white
                {													  //else it remains 0 => will be black
                    new_pixels[x][y] = 0xFFFFFF;
                }
			}
		}
		
		return new_pixels;
	}
	
	private int applyMask(int[][] pixels, int x, int y) { // computes the pixel after applying the Prewitt masks
		int [][] xKernel = {{-1,0,1}, {-1,0,1}, {-1,0,1}}; //horizontal mask
		int [][] yKernel = {{-1,-1,-1}, {0,0,0}, {1,1,1}}; //vertical mask
		int xG = 0, yG = 0; //values resulted after multiplying the masks with matrix of pixels
		
		for(int i=0; i<3; i++) { //loop through a 3x3 piece of the pixels matrix 
			for(int j=0; j<3 ;j++) {
				xG += pixels[x+i][y+j] * xKernel[i][j]; //horizontal value after applying the vertical mask
				yG += pixels[x+i][y+j] * yKernel[i][j]; //vertical value - '' - '' -
			}
		}
		
		return Math.abs(xG) + Math.abs(yG); //resulted gradient
	}
}
