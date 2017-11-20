package com.santana.interscsimulator.sbrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;


public class LogReader {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		File file = new File("/home/eduardo/entrada/sbrc/events.csv");
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    
		    reader.readLine();
		    
		    while ((text = reader.readLine()) != null) {
		    	
		    	String [] dados = text.split(";");
		    	
		    	String time = dados[0];
		    	String event = dados[1];
		    	String carId = dados[2];
		    	String linkId = dados[3];
		    	
		    	System.out.println("time: " + time);
		    	    	
		    	
		    }

		} catch (Exception e) {
			e.printStackTrace();
		}	    
		
		

	}

}
