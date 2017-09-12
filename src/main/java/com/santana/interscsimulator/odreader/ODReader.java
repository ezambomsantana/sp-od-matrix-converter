package com.santana.interscsimulator.odreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.santana.interscsimulator.db.Connector;
import com.santana.interscsimulator.entity.MapPoint;
import com.santana.interscsimulator.entity.Point;

/**
 * This file reads the OD matrix created by the subway company of Sao Paulo. You can 
 * find this matrix on the company's site. http://www.metro.sp.gov.br/
 * 
 * @author ezambomsantana
 *
 */
public class ODReader {

	private static final String inputFileName = "/home/eduardo/entrada/paraisopolis/matriz.xlsx";
	private static final String outputFileName = "/home/eduardo/entrada/paraisopolis/trips.xml";

	public static void main(String[] args) throws IOException {
		
		try {
			
			BusTravelGenerator.init();
			
			FileInputStream arquivo = new FileInputStream(new File(ODReader.inputFileName));

			System.out.println("lido!");
			XSSFWorkbook workbook = new XSSFWorkbook(arquivo);

			System.out.println("lido!");
			XSSFSheet sheetAlunos = workbook.getSheetAt(0);

			System.out.println("lido!");
			Iterator<Row> rowIterator = sheetAlunos.iterator();

			int i = 0;
			System.out.println("lido!");
			rowIterator.next();
			

			PrintWriter writer = new PrintWriter(ODReader.outputFileName, "UTF-8");

		    writer.println("<scsimulator_matrix>");
						
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();				
				
				if (row.getCell(0) != null && row.getCell(4) != null) {
					Point point = new Point();

					double multiplicador = row.getCell(0).getNumericCellValue();					
					double latOrigin = row.getCell(2).getNumericCellValue();
					double lonOrigin = row.getCell(3).getNumericCellValue();
					double latDestination = row.getCell(5).getNumericCellValue();
					double lonDestination = row.getCell(6).getNumericCellValue();
					double modeNumero = row.getCell(14).getNumericCellValue();
					
					
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
					
					if (mode.equals("car") || mode.equals("walk")) {
					
						int hourStart = (int)row.getCell(9).getNumericCellValue();
						point.setHourStart(hourStart);
						
						int minuteStart = (int)row.getCell(10).getNumericCellValue();
						point.setMinuteStart(minuteStart);
						
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
						sb.append(1);
						sb.append("\" start=\"");
						sb.append(point.getTimeStart());
						sb.append("\" mode=\"");
						sb.append(mode);
						sb.append("\" />");
					    writer.println(sb.toString());
					    
					} else if (mode.equals("subway")) {
						
						int hourStart = (int)row.getCell(9).getNumericCellValue();
						point.setHourStart(hourStart);
						
						int minuteStart = (int)row.getCell(10).getNumericCellValue();
						point.setMinuteStart(minuteStart);
						
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
							
						sb.append("   <multi_trip ");
						sb.append(" count=\"");
						sb.append(1);
						sb.append("\" start=\"");
						sb.append(point.getTimeStart());
						sb.append("\" type=\"hospital\"");
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
					    
					} else if (mode.equals("bus")) {
						
						int hourStart = (int)row.getCell(9).getNumericCellValue();
						point.setHourStart(hourStart);
						
						int minuteStart = (int)row.getCell(10).getNumericCellValue();
						point.setMinuteStart(minuteStart);
						
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
								if (buses != null) {
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
							
						sb.append("   <multi_trip ");
						sb.append(" count=\"");
						sb.append(1);
						sb.append("\" start=\"");
						sb.append(point.getTimeStart());
						sb.append("\" type=\"hospital\"");
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
								

							//	idNodeLink = Connector.getPointAndLinkByBusStopt(Long.valueOf(path[1]));
								
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
						
					}
					
					i++;				

					System.out.println(i);
				}
				
				if (i % 1000 == 0) {
					System.out.println(i);
				}
				
			}
			arquivo.close();
		    writer.println("</scsimulator_matrix>");
			
		    writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Arquivo Excel não encontrado!");
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
