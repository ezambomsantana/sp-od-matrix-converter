package com.santana.interscsimulator.odreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.santana.interscsimulator.db.Connector;
import com.santana.interscsimulator.entity.MapPoint;
import com.santana.interscsimulator.entity.Point;

/**
 * This file reads the OD matrix created by the subway company of Sao Paulo. You can 
 * find this matrix on the company's site. http://www.metro.sp.gov.br/
 * 
 * Version used to SBRC 2018 paper
 * 
 * @author ezambomsantana
 *
 */
public class ODReaderExperiments {

	private static final String inputFileName = "/home/eduardo/Doutorado/od/od.csv";
	private static final String outputFileName = "/home/eduardo/Doutorado/od/trips.xml";

	public static void main(String[] args) throws IOException {
		

		BusTravelGenerator.init();
		File file = new File(ODReaderExperiments.inputFileName);
		BufferedReader reader = null;

		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    
		    reader.readLine();  				
    		
    	    int i = 0;
    	    
		    int countBus = 0;
		    int countCar = 0;
		    int countMetro = 0;
		    int countWalk = 0;	
		    int countErros = 0;
		    int countCertos = 0;
    	    
			PrintWriter writer = new PrintWriter(ODReaderExperiments.outputFileName, "UTF-8");

		    writer.println("<scsimulator_matrix>");

		    while ((text = reader.readLine()) != null) {
				Point point = new Point();
		    	
		    	String [] dados = text.split(";");
		    	
		    	if (dados[103] == null || dados[103].equals("")) {
		    		continue;
		    	}
		    	
				int modeNumero = Integer.parseInt(dados[103]);
				
				String mode = "";
				if (modeNumero == 9 || modeNumero == 1) {
					mode = "bus"; // bus
				} else if (modeNumero == 16) {
					mode = "walk";
				} else if (modeNumero == 12) {
					mode = "subway"; //subway
				} else {
					mode = "car";
				} 
				
			    int multiplicador = 0;					
				double latOrigin = 0;
				double lonOrigin = 0;
				double latDestination = 0;
				double lonDestination = 0;
				int hora = 0;
				int minuto = 0;
				
			    try {
			    	
			    	multiplicador = (int) Double.parseDouble(dados[76]);	
			    	latOrigin = Double.parseDouble(dados[81]);
			    	lonOrigin = Double.parseDouble(dados[82]);
			    	latDestination = Double.parseDouble(dados[85]);
			    	lonDestination = Double.parseDouble(dados[86]);
			    	hora = Integer.parseInt(dados[107]); 
			    	minuto = Integer.parseInt(dados[108]); 
				
					if (mode.equals("car") || mode.equals("walk")) {
						
						
						point.setHourStart(hora);
						
						point.setMinuteStart(minuto);
												
						double[] coordsOrigin = UTM2Deg(23, latOrigin, lonOrigin);
						double[] coordsDestination = UTM2Deg(23, latDestination, lonDestination);
						
						point.setLatOrigin(coordsOrigin[0]);
						point.setLonOrigin(coordsOrigin[1]);
						
						point.setLatDestination(coordsDestination[0]);
						point.setLonDestination(coordsDestination[1]);	
						
						long [] idsOrigin = null;
						int dist = 1000;
						while (idsOrigin == null) {
							idsOrigin = Connector.selectNearestPoint(point.getLatOrigin() , point.getLonOrigin() , dist);
							dist = dist * 5;
						}
						
						long [] idDestination = null;
						dist = 1000;
						while (idDestination == null) {
							idDestination = Connector.selectNearestPoint(point.getLatDestination() , point.getLonDestination(), dist);
							dist = dist * 5;
						}
						
						if (idsOrigin[0] == idDestination[0]) {
							System.out.println("mesmo lugar");
							continue;
						}
	
						countCar = countCar + multiplicador;
					
	
						StringBuilder sb = new StringBuilder();
						
						sb.append("   <trip name=\"");
						sb.append(i);
						sb.append("\" origin=\"");
						sb.append(idsOrigin[0]);
						sb.append("\" destination=\"");
						sb.append(idDestination[0]);
						sb.append("\" link_origin=\"");
						sb.append(idsOrigin[1]);
						sb.append("\" count=\"");
						sb.append(50);
						sb.append("\" start=\"");
						sb.append(point.getTimeStart());
						sb.append("\" mode=\"");
						sb.append(mode);
						sb.append("\" />");
					    writer.println(sb.toString());
					    i++;
					} else if (mode.equals("subway")) {
						countMetro = countMetro + multiplicador;
										
						point.setHourStart(hora);					
						point.setMinuteStart(minuto);
						
						double[] coordsOrigin = UTM2Deg(23, latOrigin, lonOrigin);
						double[] coordsDestination = UTM2Deg(23, latDestination, lonDestination);
						
						point.setLatOrigin(coordsOrigin[0]);
						point.setLonOrigin(coordsOrigin[1]);
						
						point.setLatDestination(coordsDestination[0]);
						point.setLonDestination(coordsDestination[1]);	
						
						long [] idsOrigin = null;
						int dist = 1000;
						while (idsOrigin == null) {
							idsOrigin = Connector.selectNearestPoint(point.getLatOrigin() , point.getLonOrigin() , dist);
							dist = dist * 5;
						}
						
						long [] idDestination = null;
						dist = 1000;
						while (idDestination == null) {
							idDestination = Connector.selectNearestPoint(point.getLatDestination() , point.getLonDestination(), dist);
							dist = dist * 5;
						}						
																	
						long idMetroOrigin = Connector.selectNearestMetroStation(point.getLatOrigin() , point.getLonOrigin(), 100000);
						long idMetroDestination = Connector.selectNearestMetroStation(point.getLatDestination(), point.getLonDestination(), 100000);
	
						MapPoint pointOrigin = Connector.getPointById(String.valueOf(idMetroOrigin));
						MapPoint pointDestination = Connector.getPointById(String.valueOf(idMetroDestination));					
	
						StringBuilder sb = new StringBuilder();
							
						sb.append("   <multi_trip name=\"");
						sb.append(i);
						sb.append("\" count=\"");
						sb.append(multiplicador);
						sb.append("\" start=\"");
						sb.append(point.getTimeStart());
						sb.append("\" mode=\"metro\"");
						sb.append(" type=\"hospital\"");
						sb.append(">\n");
						
						sb.append("      <trip origin=\"");
						sb.append(idsOrigin[0]);
						sb.append("\" link_origin=\"");
						sb.append(idsOrigin[1]);
						sb.append("\" destination=\"");
						sb.append(idMetroOrigin);
						sb.append("\" mode=\"walk\"");
						sb.append("/>\n");				
						
						sb.append("      <trip origin=\"");
						sb.append(idMetroOrigin);
						sb.append("\" link_origin=\"");
						sb.append(pointOrigin.getIdLink());
						sb.append("\" destination=\"");
						sb.append(idMetroDestination);
						sb.append("\" link_destination=\"");
						sb.append(pointDestination.getIdLink());
						sb.append("\" mode=\"metro\"");
						sb.append("/>\n");			
						
						sb.append("      <trip origin=\"");
						sb.append(idMetroDestination);
						sb.append("\" link_origin=\"");
						sb.append(pointDestination.getIdLink());
						sb.append("\" destination=\"");
						sb.append(idDestination[0]);
						sb.append("\" mode=\"walk\"");
						sb.append("/>\n");			
						
						sb.append("   </multi_trip>");			
					    writer.println(sb.toString());
					    i++;
					    
					} else if (mode.equals("bus")) {
						
						point.setHourStart(hora);
												
						point.setMinuteStart(minuto);
						
						double[] coordsOrigin = UTM2Deg(23, latOrigin, lonOrigin);
						double[] coordsDestination = UTM2Deg(23, latDestination, lonDestination);
						
						point.setLatOrigin(coordsOrigin[0]);
						point.setLonOrigin(coordsOrigin[1]);
						
						point.setLatDestination(coordsDestination[0]);
						point.setLonDestination(coordsDestination[1]);	
						
						long [] idsOrigin = null;
						int dist = 1000;
						while (idsOrigin == null) {
							idsOrigin = Connector.selectNearestPoint(point.getLatOrigin() , point.getLonOrigin() , dist);
							dist = dist * 5;
						}
						
						long [] idDestination = null;
						dist = 1000;
						while (idDestination == null) {
							idDestination = Connector.selectNearestPoint(point.getLatDestination() , point.getLonDestination(), dist);
							dist = dist * 5;
						}	
											
						countBus = countBus + multiplicador;
																	
						List<long[]> idBusStationOriginList = Connector.selectNearestBusStop(point.getLatOrigin() , point.getLonOrigin(), 10000);
						List<long[]> idBusStationDestinationList = Connector.selectNearestBusStop(point.getLatDestination(), point.getLonDestination(), 10000);
							
						long [] idBusStationOrigin = null; 
						long [] idBusStationDestination = null;
							
						List<String> selectedBuses = null;
						List<String> selectedPath = null;
						for (int h = 0; h < idBusStationOriginList.size(); h++) {
							for (int z = 0; z < idBusStationDestinationList.size(); z++) {
								long [] ro = idBusStationOriginList.get(h);
								long [] rd = idBusStationDestinationList.get(z);
								BusTravelGenerator.getShortestPath(String.valueOf(ro[1]), String.valueOf(rd[1]));
								List<String> buses = BusTravelGenerator.buses;
								if (buses != null && buses.size() > 0 && !buses.get(0).equals("")) {
									if (selectedBuses == null || selectedBuses.size() > buses.size()) {
										selectedBuses = buses;
										selectedPath = BusTravelGenerator.path;
										idBusStationOrigin = ro;
										idBusStationDestination = rd;
									}
								}								
							}
						}
						
						MapPoint pointOrigin = Connector.getPointById(String.valueOf(idBusStationOrigin[0]));
						MapPoint pointDestination = Connector.getPointById(String.valueOf(idBusStationDestination[0]));					
	
						StringBuilder sb = new StringBuilder();
								
						sb.append("   <multi_trip name=\"");
						sb.append(i);
						sb.append("\"  count=\"");
						sb.append(multiplicador);
						sb.append("\" start=\"");
						sb.append(point.getTimeStart());
						sb.append("\" mode=\"bus\"");
						sb.append(" type=\"hospital\"");
						sb.append(">\n");
													
						sb.append("      <trip origin=\"");
						sb.append(idsOrigin[0]);
						sb.append("\" link_origin=\"");
						sb.append(idsOrigin[1]);
						sb.append("\" destination=\"");
						sb.append(idBusStationOrigin[0]);
						sb.append("\" mode=\"walk\"");
						sb.append("/>\n");				
							
							
						if (selectedBuses != null) {
							
							long inicio = idBusStationOrigin[0];
							long linkInicio = Connector.getPointById(String.valueOf(idBusStationOrigin[0])).getIdLink();
							
							for (int o = 0; o < selectedBuses.size(); o++) {
						
								String bus = selectedBuses.get(o);
								String path[] = selectedPath.get(o).split(":");
								
								long [] idNodeLink = Connector.getPointAndLinkByBusStopt(Long.valueOf(path[0]));
									
								sb.append("      <trip origin=\"");
								sb.append(inicio);
								sb.append("\" link_origin=\"");
								sb.append(linkInicio);
								
								if (o != selectedBuses.size() -1) {
									sb.append("\" destination=\"");
									sb.append(idNodeLink[0]);
									sb.append("\" link_destination=\"");
									sb.append(idNodeLink[1]);									
								} else {
									idNodeLink = Connector.getPointAndLinkByBusStopt(Long.valueOf(path[1]));
									sb.append("\" destination=\"");
									sb.append(idNodeLink[0]);
									sb.append("\" link_destination=\"");
									sb.append(idNodeLink[1]);											
								}
								
								sb.append("\" line=\"");
								sb.append(bus);
								sb.append("\" mode=\"bus\"");
								sb.append("/>\n");	
																	
								inicio = idNodeLink[0];
								linkInicio = idNodeLink[1];
								
							}
						
						} else {
							sb.append("      <trip origin=\"");
							sb.append(idBusStationOrigin[0]);
							sb.append("\" link_origin=\"");
							sb.append(pointOrigin.getIdLink());
							sb.append("\" destination=\"");
							sb.append(idBusStationDestination[0]);
							sb.append("\" line=\"");
							sb.append("nd");
							sb.append("\" mode=\"bus\"");
							sb.append("/>\n");	
						}
							
						sb.append("      <trip origin=\"");
						sb.append(idBusStationDestination[0]);
						sb.append("\" link_origin=\"");
						sb.append(pointDestination.getIdLink());
						sb.append("\" destination=\"");
						sb.append(idDestination[0]);
						sb.append("\" mode=\"walk\"");
						sb.append("/>\n");			
						
						sb.append("   </multi_trip>");			
					    writer.println(sb.toString());
					    i++;
						  
						
					}
					countCertos++;
			    } catch (Exception e) {
			    	countErros++;
			    	System.out.println("erro");
			    	continue;
			    }
				System.out.println(i);
				
			    
		    }
		    
			System.out.println("Bus: " + countBus);
			System.out.println("Car: " + countCar);
			System.out.println("Metro: " + countMetro);
			System.out.println("Walk: " + countWalk);
			System.out.println("Certos: " + countCertos);
			System.out.println("Erros: " + countErros);
			
		    writer.println("</scsimulator_matrix>");
			
		    writer.close();

		    
		} catch (Exception e) {
			e.printStackTrace();
		}	    
	}

	private static double[] UTM2Deg(int zone, double eas, double nort){
    	
		double[] coords = new double[2];
		double Easting=eas;
		double Northing=nort;    
		double north = Northing - 10000000;
		
		coords[0] = (north/6366197.724/0.9996+(1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)-0.006739496742*Math.sin(north/6366197.724/0.9996)*Math.cos(north/6366197.724/0.9996)*(Math.atan(Math.cos(Math.atan(( Math.exp((Easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*( 1 -  0.006739496742*Math.pow((Easting - 500000) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996 )/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*6399593.625*(north/6366197.724/0.9996 - 0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996 )*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))-north/6366197.724/0.9996)*3/2)*(Math.atan(Math.cos(Math.atan((Math.exp((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996)))*Math.tan((north-0.9996*6399593.625*(north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3))/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))-north/6366197.724/0.9996))*180/Math.PI;
	    coords[0]=Math.round(coords[0]*10000000);
	    coords[0]=coords[0]/10000000;
	    coords[1] =Math.atan((Math.exp((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3))-Math.exp(-(Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2)/3)))/2/Math.cos((north-0.9996*6399593.625*( north/6366197.724/0.9996-0.006739496742*3/4*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.pow(0.006739496742*3/4,2)*5/3*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2* north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4-Math.pow(0.006739496742*3/4,3)*35/27*(5*(3*(north/6366197.724/0.9996+Math.sin(2*north/6366197.724/0.9996)/2)+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/4+Math.sin(2*north/6366197.724/0.9996)*Math.pow(Math.cos(north/6366197.724/0.9996),2)*Math.pow(Math.cos(north/6366197.724/0.9996),2))/3)) / (0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2))))*(1-0.006739496742*Math.pow((Easting-500000)/(0.9996*6399593.625/Math.sqrt((1+0.006739496742*Math.pow(Math.cos(north/6366197.724/0.9996),2)))),2)/2*Math.pow(Math.cos(north/6366197.724/0.9996),2))+north/6366197.724/0.9996))*180/Math.PI+zone*6-183;
	    coords[1]=Math.round(coords[1]*10000000);
	    coords[1]=coords[1]/10000000; 
	    return coords;
	    
	} 
	

}
