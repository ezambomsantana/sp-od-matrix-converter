package com.santana.interscsimulator.odreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import com.santana.interscsimulator.entity.Hospital;

public class HospitalReader {

	public static void main(String[] args) {
		
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
		
		try {
		    reader = new BufferedReader(new FileReader(fileHospitals));
		    String text = null;
		    
		    reader.readLine();
		    int count = 0;
		    while ((text = reader.readLine()) != null) {
		    	
		    	String [] dados = text.split(",");
		    	if (hospitalList.get(dados[0]) != null) {
		    		System.out.println("achou!");
		    		count++;
		    	}

		    
		    }
		    System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}

		

	}

}
