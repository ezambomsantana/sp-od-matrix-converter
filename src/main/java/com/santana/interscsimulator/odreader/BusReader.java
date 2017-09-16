package com.santana.interscsimulator.odreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

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
    		
    		String [] buses =
    			{ "8020-10-0","675N-10-0","8020-10-1","809J-10-0","N706-11-0","N836-11-0","809U-10-0","746P-10-0","746R-10-0","N705-11-0","637A-10-1","N205-11-1","8020-10-0","677Y-10-1","609F-10-1","807J-10-1","746P-10-0","6042-10-0","7040-10-0","6412-21-0","N633-11-0","476A-10-1","609J-10-0","5106-21-0","807P-10-0","746K-10-0","5154-10-0","707A-10-0","746P-10-0","746R-10-0","609F-10-0","746P-10-0","746R-10-0","609F-10-0","647A-10-0","967A-10-0","746K-10-0","5154-10-0","647C-10-0","807P-10-0","746K-10-0","6414-10-0","N701-11-0","5104-10-1","702C-10-0","2182-10-0","746P-10-0","675A-10-0","5154-10-1","6412-21-0","5701-10-1","5175-10-0","6412-21-0","6418-31-0","746H-10-1","N842-11-0","748A-41-0","647A-10-0","7181-10-0","647A-10-0","7245-10-0","778R-21-0","875A-10-0","775F-10-0","746H-10-1","N842-11-0","702C-10-1","647A-10-0","746K-10-0","5701-10-1","5010-10-0","7040-21-0","746K-10-0","5701-10-1","5010-10-0","7040-10-0","647A-10-0","967A-10-0","746P-10-0","746R-10-0","609F-10-0","7040-10-0","807P-10-0","807P-10-0","746K-10-0","6414-10-0","807P-10-0","746K-10-0","5154-10-0","675X-10-0","746K-10-0","6414-10-0","637P-10-0","7040-10-0","756A-10-1","807J-10-0","5154-10-0","7040-10-0","6412-10-0","647A-10-0","746K-10-0","746K-10-0","807J-10-1","5119-10-0","807P-10-0","746K-10-0","5154-10-0","6455-10-0","695V-10-0","807P-10-0","746K-10-0","6414-10-0","5111-10-0","746K-10-0","746P-10-0","746H-10-0","675A-10-0","5154-10-1","775F-10-0","8075-10-1","746H-10-0","6042-10-0","746H-10-1","746H-10-0","6042-10-0","647P-10-0","8605-10-0","6422-10-0","775F-10-0","8605-10-0","N701-11-0","4111-10-0","7040-10-0","746P-10-0","647P-10-1","807J-10-1","6837-10-0","807J-10-1","6837-10-0","746P-10-0","746R-10-0","N705-11-0","N705-11-1","701U-10-0","8022-10-0" };

    		List<String> lista =
    				Arrays.asList(buses);
    				
    		
    		PrintWriter writer = new PrintWriter("/home/eduardo/entrada/hospital/buses.xml", "UTF-8");
    	    writer.println("<scsimulator_buses>");

		    while ((text = reader.readLine()) != null) {
		    	
		    	String [] dados = text.split(";");
		    	
		    	if (busId.equals(dados[1])) {	
			    	bus.getStops().add(Long.parseLong(dados[4])); // stop_id		    		
		    	} else {   
		    		
		    		if (!busId.equals("") && lista.contains(bus.getId())) {
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
		

		StringBuilder sb2 = new StringBuilder();
		long idNode = Connector.getPointByBusStopt(bus.getStops().get(0));
		sb2.append(idNode);
		bus.getStops().remove(0);
		
		int count = 1;
		long lastNode = idNode;
		for (Long stop : bus.getStops()) {
			idNode = Connector.getPointByBusStopt(stop);
			if (lastNode != idNode) {
				sb2.append("," + idNode);
				lastNode = idNode;
				count++;
			}
		}
		
		if (count < 5) {
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("    <bus id=\"");
		sb.append(bus.getId());
		sb.append("\" interval=\"3600\"");
		sb.append(" start_time=\"18000\"");
		sb.append(" stops=\"");				
		sb.append(sb2);
		sb.append("\" />");
		
		writer.println(sb.toString());
		
	}


}
