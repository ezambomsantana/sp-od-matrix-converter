package com.santana.interscsimulator.sbrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.santana.interscsimulator.db.Connector;

public class CutFileInterval {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		File file = new File("/home/eduardo/Doutorado/sp_completo/events_lat_long.csv");
		BufferedReader reader = null;
		
		PrintWriter writer = new PrintWriter("/home/eduardo/Doutorado/sp_completo/events_lat_long_interval.csv", "UTF-8");
	    
		int start = 28700;
		int finish = 28800;
		
		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    
		    reader.readLine();
		    
		    while ((text = reader.readLine()) != null) {
		    	
		    	String [] dados = text.split(";");
		    	
		    	String time = dados[0];
		    	int timeInt = Integer.parseInt(time);
		    	
		    	if (timeInt < start) {
		    		continue;
		    	}
		    	
		    	if (timeInt > finish) {
		    		break;
		    	}
		    	
		    	
		    	String event = dados[1];
		    	String carId = dados[2];
		    	
		    	double [] coords = cartesian(Float.parseFloat(dados[3]), Float.parseFloat(dados[4]));
		    	
		    	String lat = "" + coords[0];
		    	String lon = "" + coords[1];
		    	
		    	System.out.println(time);
		    	
		    	StringBuilder builder = new StringBuilder();
		    	builder.append(time).append(";").
		    		append(event).append(";").
		    		append(carId).append(";").
		    		append(lat).append(";").
		    		append(lon);
		    	
		    	
		    	if (event.equals("arrival")) {
		    		String totalTime = dados[5];
		    		String distance = dados[6]; 
		    		
		    //		builder.append(";")
		    	//		.append(totalTime).append(";")
		    		//	.append(distance);
		    	}

		    	
		    	writer.println(builder.toString());		    			    	    	
		    	
		    }
		    
		    writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}	    
		
		

	}
	
	public static double[] cartesian(float lat, float lon) {
		double [] coords = new double[2];
		
		coords[0] = Math.abs(6371 * Math.cos(lat) * Math.cos(lon));
		coords[1] = Math.abs(6371 * Math.cos(lat) * Math.sin(lon));
		
		return coords;
		
		
	}
	
}
