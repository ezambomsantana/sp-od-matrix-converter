package com.santana.interscsimulator.sbrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class VanetGenerator {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		File file = new File("/home/eduardo/Doutorado/sbrc/events.csv");
		BufferedReader reader = null;
		
		PrintWriter writer = new PrintWriter("/home/eduardo/Doutorado/sbrc/events.tcl", "UTF-8");
		
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

		    	StringBuilder builder = new StringBuilder();
		    	
		    	if (event.equals("start")) {
		    		
			    	builder.append("$node (")
			    		.append(carId)
			    		.append(") set X_")
			    		.append(linkId).append("\n");
		    		

			    	builder.append("$node (")
			    		.append(carId)
			    		.append(") set Y_")
			    		.append(linkId)
			    		.append("\n");

			    	builder.append("$node (")
			    		.append(carId)
			    		.append(") set Z_ 0\n");
			    	
			    	builder.append("$ns at ")
			    		.append(time)
			    		.append("\"$node_(")
			    		.append(carId)
			    		.append(") setdest")
			    		.append(linkId)
			    		.append(" ")
			    		.append(linkId)
			    		.append(" ")
			    		.append(0)
			    		.append("\"\n");
		    			
		    		
		    	} else if (event.equals("move")) {
		    		
		    	} else if (event.equals("arrive")) {
		    		
		    	}
		    	
		    	writer.println(builder.toString());	
		    	
		    }
		    
		    writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}	    
		
		

	}

}
