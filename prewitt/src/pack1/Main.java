package pack1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import pack2.*;

public class Main {
	public static void main(String[] args) {
		//Initializations
		String[] inputFilePaths = null;
		String[] outputFilePaths = null;
		List<String> inputs = new ArrayList<String>();
		List<String> outputs = new ArrayList<String>();
		String line;
		
		Buffer b = new Buffer();
		ThreadUser producer, consumer;
		//Initializations 
		
		if(args.length > 0){ //if received console arguments, filter them for input files and output files
			int mode = -1; // -1 - doesn't take arg, 0 - takes arg for input files, 1 - takes for output files
			
			for(String arg : args) {
				if(arg.equals("-i")) mode = 0; //if the arg that announces input files is found -> change mode accordingly
				else if(arg.equals("-o")) mode = 1; //if the arg that announces output files is found -> change mode accordingly
				else switch(mode) {
					case -1: //does nothing
						break;
					case 0:
						inputs.add(arg); //adds args to inputs
						break;
					case 1:
						outputs.add(arg); //adds args to outputs
						break;
				}
			}
		}
		
		inputFilePaths = new String[inputs.size()]; //creates array the size of the list inputs
		outputFilePaths = new String[outputs.size()]; // creates array the size of the list outputs
		
		inputs.toArray(inputFilePaths); //copies the values from the list to the array
		outputs.toArray(outputFilePaths); //copies the values from the list to the array
		
		if(inputFilePaths.length == 0) { //if there's no args, read input/output data from keyboard
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			//buffer that takes the stream from keyboard
	        try {
	        	System.out.println("Enter Input File(s) Path");
	        	line = br.readLine(); //reads line containing all the input files
	        	inputFilePaths = line.split(" "); //split for every file
	        			
	        	System.out.println("\nEnter Output File(s) Path");
	        	line = br.readLine(); //reads line containing all output files
	        	outputFilePaths = line.split(" "); //split for every file
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		producer = new Producer(b, inputFilePaths); //initialization for producer with buffer and input file paths
		consumer = new Consumer(b, outputFilePaths); //initialization for consumer with buffer and output file paths
		producer.start(); //starts thread
		consumer.start(); //starts thread
	}
}
