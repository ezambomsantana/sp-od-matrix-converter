package com.santana.interscsimulator.sbrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.santana.interscsimulator.db.Connector;

public class LogReader {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		File file = new File("/home/eduardo/Doutorado/events.csv");
		BufferedReader reader = null;
		
		PrintWriter writer = new PrintWriter("/home/eduardo/events_lat_long.csv", "UTF-8");
	    
		HashMap<String, float[]> links = Connector.getAllLinks();
		
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
		    	
		    	float [] point = links.get(linkId);
		    	StringBuilder builder = new StringBuilder();
		    	builder.append(time).append(";").
		    		append(event).append(";").
		    		append(carId).append(";").
		    		append(point[0]).append(";").
		    		append(point[1]);
		    	
		    	
		    	if (event.equals("arrival")) {
		    		String totalTime = dados[4];
		    		String distance = dados[5]; 
		    		
		    		builder.append(";")
		    			.append(totalTime).append(";")
		    			.append(distance);
		    	}

		    	
		    	writer.println(builder.toString());		    			    	    	
		    	
		    }
		    
		    writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}	    
		
		

	}

}
