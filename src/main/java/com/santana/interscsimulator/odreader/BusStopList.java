package com.santana.interscsimulator.odreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.santana.interscsimulator.entity.Bus;

public class BusStopList {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		File file = new File("/home/eduardo/Pontos_Parada/buses.txt");
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    
		    reader.readLine();		    
    		
		    while ((text = reader.readLine()) != null) {
		    	
		    	String [] dados = text.split(";");
		    	String busId = dados[1]; // bus id
		    	String stopId = dados[4]; // stop_id		
		    	System.out.println(busId + "     " + stopId);
		    }    	
		    	
		    	

		} catch (Exception e) {
			e.printStackTrace();
		}	 
	}
		
	
}
