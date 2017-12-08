package com.santana.interscsimulator.sbrc;

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
import com.santana.interscsimulator.entity.Car;

public class VanetGenerator {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		
		File file = new File("/home/eduardo/Doutorado/sbrc/events.csv");
		BufferedReader reader = null;
		
		PrintWriter writer = new PrintWriter("/home/eduardo/Doutorado/sbrc/events.tcl", "UTF-8");
		
		
		HashMap<String, Car> carsId = new HashMap<String, Car>();
		HashMap<String, Car> carsTime = new HashMap<String, Car>();
		
		List<String> listCars = new ArrayList<String>();
		
		int nextCar = 0;
		int currentTimestamp = 1;
		int lastTimestamp = 1;
		
		try {
			
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    
		    while ((text = reader.readLine()) != null) {
		    	
		    	String [] dados = text.split(";");
		    	
		    	String time = dados[0];
		    	String event = dados[1];
		    	String carId = dados[2];
		    	float lat = Float.parseFloat(dados[3]);
		    	float lon = Float.parseFloat(dados[4]);
		    	

		    	float [] point = { lat , lon };
		    	StringBuilder builder = new StringBuilder();
		    	currentTimestamp = Integer.parseInt(time);

	    		Car carToSave = new Car();
	    		carToSave.setCarId(carId);
	    		carToSave.setLat("" + point[0]);
	    		carToSave.setLon("" + point[1]);
	    		carToSave.setTime(time);

		    	// new time
		    	if (currentTimestamp != lastTimestamp) {
		    		
		    		int diferenca = currentTimestamp - lastTimestamp;		    		
		    		
		    		if (currentTimestamp > 28800) {
		    			break;
		    		}
		    		
		    		for (int i = 0; i < diferenca ; i++) {		    		
			    		for (String carTest : listCars) {
	
				    		Car carToStore = carsTime.get(carTest + (lastTimestamp + i));
				    		if (carToStore == null) {
					    		Car car = carsId.get(carTest);
						    	builder.append("$ns_ at ")
					    		.append(lastTimestamp + i)
					    		.append(" \"$node_(")
					    		.append(car.getId())
					    		.append(") setdest ")
					    		.append(car.getLat())
					    		.append(" ")
					    		.append(car.getLon())
					    		.append(" ")
					    		.append(0)
					    		.append("\"\n");
				    		}
			    		}
		    		}
		    		
		    		
		    	}
		    	
		    	
		    	if (event.equals("start")) {
		    		
		    		listCars.add(carId);
		    		int car = nextCar++;
		    		carToSave.setId(car);
		    		
		    		carsId.put(carId, carToSave);
		    		
			    	builder.append("$node_(")
			    		.append(car)
			    		.append(") set X_")
			    		.append(point[0]).append("\n");

			    	builder.append("$node_(")
			    		.append(car)
			    		.append(") set Y_")
			    		.append(point[1])
			    		.append("\n");

			    	builder.append("$node_(")
			    		.append(car)
			    		.append(") set Z_ 0\n");
			    	
			    	builder.append("$ns_ at ")
			    		.append(time)
			    		.append(" \"$node_(")
			    		.append(car)
			    		.append(") setdest ")
			    		.append(point[0])
			    		.append(" ")
			    		.append(point[1])
			    		.append(" ")
			    		.append(0)
			    		.append("\"\n");
			    	
			    	carsTime.put(carId + currentTimestamp, carToSave);
		    			
		    		
		    	} else if (event.equals("move")) {
		 
		    		Car car = carsId.get(carId);
		    		
		    		if (car != null) {
		    		
			    		car.setCarId(carId);
			    		car.setLat("" + point[0]);
			    		car.setLon("" + point[1]);
			    		car.setTime(time);
			    		
				    	builder.append("$ns_ at ")
			    		.append(time)
			    		.append(" \"$node_(")
			    		.append(car.getId())
			    		.append(") setdest ")
			    		.append(point[0])
			    		.append(" ")
			    		.append(point[1])
			    		.append(" ")
			    		.append(0)
			    		.append("\"\n");
				    	carsTime.put(carId + currentTimestamp, car);
				    	
			    	} else {
			    					    		
			    		listCars.add(carId);
			    		int carCount = nextCar++;
			    		carToSave.setId(carCount);
			    		
			    		carsId.put(carId, carToSave);
			    		
				    	builder.append("$node_(")
				    		.append(carCount)
				    		.append(") set X_")
				    		.append(point[0]).append("\n");

				    	builder.append("$node_(")
				    		.append(carCount)
				    		.append(") set Y_")
				    		.append(point[1])
				    		.append("\n");

				    	builder.append("$node_(")
				    		.append(carCount)
				    		.append(") set Z_ 0\n");
				    	
				    	builder.append("$ns_ at ")
				    		.append(time)
				    		.append(" \"$node_(")
				    		.append(carCount)
				    		.append(") setdest ")
				    		.append(point[0])
				    		.append(" ")
				    		.append(point[1])
				    		.append(" ")
				    		.append(0)
				    		.append("\"\n");
				    	
				    	carsTime.put(carId + currentTimestamp, carToSave);
			    		
			    		
			    		
			    	}
		    		
		    		
		    		
		    	} else if (event.equals("arrival")) {
		    		
		    		listCars.remove(carId);

		    		Car car = carsId.get(carId);
		    		
		    		if (car == null) {
		    			continue;
		    		}
		    		
		    		car.setCarId(carId);
		    		car.setLat("" + point[0]);
		    		car.setLon("" + point[1]);
		    		car.setTime(time);

			    	builder.append("$ns_ at ")
		    		.append(time)
		    		.append(" \"$node_(")
		    		.append(car.getId())
		    		.append(") setdest ")
		    		.append(point[0])
		    		.append(" ")
		    		.append(point[1])
		    		.append(" ")
		    		.append(0)
		    		.append("\"\n");
			    	carsTime.put(carId + currentTimestamp, car);	  
			    	
		    	}
		    	
		    	lastTimestamp = currentTimestamp;
		    	
		    	writer.print(builder.toString());
		    	
		    }
		    
		    writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}	    
		
		

	}

}
