package com.santana.interscsimulator.odreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.santana.interscsimulator.db.Connector;
import com.santana.interscsimulator.entity.MapPoint;
import com.santana.interscsimulator.entity.MetroStation;

public class MetroStationReader {
	

	private static final String fileName = "C:/dev/simulator/metro_verde.xlsx";

	public static void main(String [] args) throws IOException {
		
		FileInputStream arquivo = new FileInputStream(new File(fileName));

		XSSFWorkbook workbook = new XSSFWorkbook(arquivo);
		XSSFSheet sheetAlunos = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheetAlunos.iterator();
		

		PrintWriter writer = new PrintWriter("c:/dev/metro.xml", "UTF-8");

	    writer.println("<metro>");	    

		StringBuilder stations = new StringBuilder();
		StringBuilder links = new StringBuilder();
		
		String lastLine = "";
		String lastStation = "";
		long idLastStation = 0;
		int cont = 1;
		
					
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();		
			String line = row.getCell(1).getStringCellValue();
			if (!line.equals(lastLine)) {
				lastLine = line;
				
				String stationName = row.getCell(8).getStringCellValue();
											
				String lat = row.getCell(9).getStringCellValue().replace(',', '.');
				String lon = row.getCell(10).getStringCellValue().replace(',', '.');;
				
				long [] idsOrigin = null;
				int dist = 1000;
				while (idsOrigin == null) {
					idsOrigin = Connector.selectNearestPoint(Double.parseDouble(lat), Double.parseDouble(lon) , dist);
					dist = dist * 5;
				}
								
				stations.append("    <station name=\"");
				stations.append(stationName);
				stations.append("\" idNode=\"");
				stations.append(idsOrigin[0]);
				stations.append("\" />\n");		
				
				MetroStation metro = new MetroStation();
				metro.setId(cont);
				metro.setLat(Double.parseDouble(lat));
				metro.setLon(Double.parseDouble(lon));
				metro.setIdNode(idsOrigin[0]);
				metro.setName(stationName);
				
				Connector.insertMetroStation(metro);
				
				lastStation = stationName;
				idLastStation = idsOrigin[0];
				
				
			} else {
				
				String stationName = row.getCell(8).getStringCellValue();
				
				String lat = row.getCell(9).getStringCellValue().replace(',', '.');
				String lon = row.getCell(10).getStringCellValue().replace(',', '.');;
				
				long [] idsOrigin = null;
				int dist = 1000;
				while (idsOrigin == null) {
					idsOrigin = Connector.selectNearestPoint(Double.parseDouble(lat), Double.parseDouble(lon) , dist);
					dist = dist * 5;
				}
				
				stations.append("    <station name=\"");
				stations.append(stationName);
				stations.append("\" idNode=\"");
				stations.append(idsOrigin[0]);
				stations.append("\" />\n");
				
				links.append("    <link nameOrigin=\"");
				links.append(stationName);
				links.append("\" nameDestination=\"");
				links.append(lastStation);
				links.append("\" idOrigin=\"");
				links.append(idsOrigin[0]);
				links.append("\" idDestination=\"");
				links.append(idLastStation);
				links.append("\" />\n");

				MetroStation metro = new MetroStation();
				metro.setId(cont);
				metro.setLat(Double.parseDouble(lat));
				metro.setLon(Double.parseDouble(lon));
				metro.setIdNode(idsOrigin[0]);
				metro.setName(stationName);
				
				Connector.insertMetroStation(metro);
				
				lastStation = stationName;
				idLastStation = idsOrigin[0];
				
			}
			cont++;
			
		}
	    writer.println(stations.toString());
	    writer.println(links.toString());
	    writer.println("</metro>");
	    writer.close();
		
	}
	
	
}
