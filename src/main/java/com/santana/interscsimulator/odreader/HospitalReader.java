package com.santana.interscsimulator.odreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.santana.interscsimulator.db.Connector;
import com.santana.interscsimulator.entity.Hospital;

public class HospitalReader {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		File file = new File("c:/dev/cenario_simulador/estab_saude.csv");
		BufferedReader reader = null;
		
		HashMap<String, Hospital> hospitalList = new HashMap<String, Hospital>();

		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    
		    reader.readLine();

		    while ((text = reader.readLine()) != null) {
		    	
		    	String [] dados = text.split(",");
		    	Hospital hospital = new Hospital();
		    	hospital.setId(dados[5]);
		    	hospital.setLon(Float.parseFloat(dados[0]));
		    	hospital.setLat(Float.parseFloat(dados[1]));
		    	hospital.setNome(dados[3]);
		    	
		    	hospitalList.put(hospital.getId(), hospital);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File fileHospitals = new File("c:/dev/cenario_simulador/health_centres.csv");
		reader = null;
		
		PrintWriter writer = new PrintWriter("c:/dev/hospitals.xml", "UTF-8");
	    writer.println("<scsimulator_hospitals>");
	    
	    List<Hospital> hospitals = new ArrayList<Hospital>();
		
		try {
		    reader = new BufferedReader(new FileReader(fileHospitals));
		    String text = null;
		    
		    reader.readLine();
		    int count = 0;
		    while ((text = reader.readLine()) != null) {
		    	
		    	String [] dados = text.split(",");
		    	if (hospitalList.get(dados[0]) != null) {
		    		
		    		Hospital hospital = hospitalList.get(dados[0]);
		    		
					long [] idsOrigin = null;
					int dist = 1000;
					while (idsOrigin == null) {
						idsOrigin = Connector.selectNearestPoint(hospital.getLat() , hospital.getLon() , dist);
						dist = dist * 5;
					}
					
					hospital.setIdNode(idsOrigin[0]);
		    		
		    		System.out.println("achou!");
		    		count++;
		    		
					StringBuilder sb = new StringBuilder();
					
					sb.append("   <hospital name=\"");
					sb.append(hospital.getNome());
					sb.append("\" location=\"");
					sb.append(hospital.getIdNode());
					sb.append("\" id=\"");
					sb.append(hospital.getId());
					sb.append("\" />");
				    writer.println(sb.toString());
				    
				    hospitals.add(hospital);
		    	}

		    
		    }
		    System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Connector.insertHospitals(hospitals);
		
	    writer.println("</scsimulator_hospitals>");
		
	    writer.close();

		

	}

}
