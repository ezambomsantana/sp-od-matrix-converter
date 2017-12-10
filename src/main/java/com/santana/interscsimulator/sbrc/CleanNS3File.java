package com.santana.interscsimulator.sbrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class CleanNS3File {
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		File file = new File("/home/eduardo/Doutorado/sbrc/test.ns3");
		BufferedReader reader = null;
		
		PrintWriter writer = new PrintWriter("/home/eduardo/Doutorado/sbrc/test_clean.ns3", "UTF-8");
		
		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    
		    reader.readLine();
		    
		    while ((text = reader.readLine()) != null) {
		    	
		    	String [] dados = text.split(" ");
		    	
		    	String time = dados[0];
		    	String timeReal = dados[1];
		    	String id = dados[2];
		    	String lat = dados[3];
		    	String lon = dados[4];
		    	String mac = dados[5];
		    	String numero = dados[6];
		    	
		    	System.out.println(time);
		    	
		    	StringBuilder builder = new StringBuilder();
		    	builder.append(time).append(";").
		    		append(timeReal).append(";").
		    		append(id).append(";").
		    		append(lat).append(";").
		    		append(lon).append(";").
		    		append(mac).append(";").
		    		append(numero);
		    	
		       	writer.println(builder.toString());		    			    	    	
		    	
		    }
		    
		    writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}	    
		
		

	}
}
