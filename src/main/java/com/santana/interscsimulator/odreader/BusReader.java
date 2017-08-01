package com.santana.interscsimulator.odreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.santana.interscsimulator.db.Connector;
import com.santana.interscsimulator.entity.Bus;

public class BusReader {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		File file = new File("/home/eduardo/Pontos_Parada/buses.txt");
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    
		    reader.readLine();
		    
		    String busId = "";
    		Bus bus = new Bus();
    		
    		PrintWriter writer = new PrintWriter("/home/eduardo/entrada/hospital/buses.xml", "UTF-8");
    	    writer.println("<scsimulator_buses>");

		    while ((text = reader.readLine()) != null) {
		    	
		    	String [] dados = text.split(";");
		    	
		    	if (busId.equals(dados[1])) {	
			    	bus.getStops().add(Long.parseLong(dados[4])); // stop_id		    		
		    	} else {   
		    		
		    		if (!busId.equals("")) {
		    			writeBus(bus, writer);
		    		}
		    		bus = new Bus();
			    	bus.setId(dados[1]); // bus id
			    	bus.getStops().add(Long.parseLong(dados[4])); // stop_id		
		    		busId = dados[1];		    		
		    	}		    	
		    	
		    }
		    writeBus(bus, writer);
		    
		    writer.println("</scsimulator_buses>");			
		    writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}	    
		
		

	}

	private static void writeBus(Bus bus, PrintWriter writer) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("    <bus id=\"");
		sb.append(bus.getId());
		sb.append("\" interval=\"1800\"");
		sb.append(" start_time=\"300\"");
		sb.append(" stops=\"");		
		StringBuilder sb2 = new StringBuilder();
		long idNode = Connector.getPointByBusStopt(bus.getStops().get(0));
		sb2.append(idNode);
		bus.getStops().remove(0);
		for (Long stop : bus.getStops()) {
			idNode = Connector.getPointByBusStopt(stop);
			sb2.append("," + idNode);
		}
		sb.append(sb2);
		sb.append("\" />");
		
		writer.println(sb.toString());
		
	}


}
