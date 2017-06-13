package com.santana.interscsimulator.odreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.santana.interscsimulator.db.Connector;
import com.santana.interscsimulator.entity.Bus;

public class BusStopReader {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		File file = new File("/home/eduardo/Simulador/dados/Pontos_Parada.txt");
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    
		    reader.readLine();
		    
    		PrintWriter writer = new PrintWriter("/home/eduardo/entrada/hospital/stops.xml", "UTF-8");
    	    writer.println("<scsimulator_stops>");
		    
		    
    	    HashMap<String, Long> stops = new HashMap<String, Long>();

		    while ((text = reader.readLine()) != null) {
		    	
		    	
		    	String [] dados = text.split(";");		
		    	
		    	if (stops.get(dados[4]) == null) {
		    		
		    		long [] idsOrigin = null;
		    		int dist = 1000;
					while (idsOrigin == null) {
						idsOrigin = Connector.selectNearestPoint(Double.parseDouble(dados[10].replace(',', '.')), Double.parseDouble(dados[11].replace(',', '.')), dist);
						dist = dist * 5;						
					}
		    		writeStop(idsOrigin[0], writer);
		    		stops.put(dados[4], idsOrigin[0]);
		    		Connector.insertBusStations(Long.parseLong(dados[4]), idsOrigin[0]);
		    	}
		    	
		    	
		    	
		    }
		    
		    writer.println("</scsimulator_stops>");			
		    writer.close();
		    
		} catch (Exception e) {
			e.printStackTrace();
		}	    
		
		

	}

	private static void writeStop(long id, PrintWriter writer) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("<stop node_id=\"");
		sb.append(id);
		sb.append("\" />");
		
		writer.println(sb.toString());
		
	}


}
