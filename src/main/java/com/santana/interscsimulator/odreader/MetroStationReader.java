package com.santana.interscsimulator.odreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.santana.interscsimulator.db.Connector;
import com.santana.interscsimulator.entity.MapPoint;
import com.santana.interscsimulator.entity.MetroStation;

public class MetroStationReader {
	

	private static final String fileName = "/home/eduardo/entrada/Dados_Paraisopolis/metro.xlsx";

	public static void main(String [] args) throws IOException {
		
		FileInputStream arquivo = new FileInputStream(new File(fileName));

		XSSFWorkbook workbook = new XSSFWorkbook(arquivo);
		XSSFSheet sheetAlunos = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheetAlunos.iterator();
		

		PrintWriter writer = new PrintWriter("/home/eduardo/entrada/Dados_Paraisopolis/metro.xml", "UTF-8");

	    writer.println("<metro>");	    

		StringBuilder stations = new StringBuilder();
		StringBuilder links = new StringBuilder();
		
		String lastLine = "";
		String lastStation = "";
		long idLastStation = 0;
		int cont = 1;
		
		Map<String, MetroStation> estacoes = new  HashMap<String, MetroStation>();
					
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();		
			String line = row.getCell(1).getStringCellValue();
			
			if (!line.equals(lastLine)) {
				
				String stationName = row.getCell(8).getStringCellValue();
											
				String lat = row.getCell(10).getStringCellValue().replace(',', '.');
				String lon = row.getCell(11).getStringCellValue().replace(',', '.');;
				
				long [] idsOrigin = null;
				int dist = 1000;
				while (idsOrigin == null) {
					idsOrigin = Connector.selectNearestPoint(Double.parseDouble(lat), Double.parseDouble(lon) , dist);
					dist = dist * 5;
				}
				
				if (!estacoes.containsKey(stationName)) {	
					
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
					
					estacoes.put(stationName, metro);
					
					links.append("\n");
					
				} else {
					
					MetroStation metroStation = estacoes.get(stationName);
										
					lastStation = stationName;
					idLastStation = metroStation.getIdNode();
					links.append("\n");
					
				}
				
			} else {
				
				String stationName = row.getCell(8).getStringCellValue();

				if (!estacoes.containsKey(stationName)) {	
					
					String lat = row.getCell(10).getStringCellValue().replace(',', '.');
					String lon = row.getCell(11).getStringCellValue().replace(',', '.');;
					
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

					estacoes.put(stationName, metro);

					Connector.insertMetroStation(metro);
					
					links.append("    <link nameOrigin=\"");
					links.append(stationName);
					links.append("\" nameDestination=\"");
					links.append(lastStation);
					links.append("\" idOrigin=\"");
					links.append(idsOrigin[0]);
					links.append("\" idDestination=\"");
					links.append(idLastStation);
					links.append("\" />\n");
					
					lastStation = stationName;
					idLastStation = idsOrigin[0];
				
				} else {
					
					MetroStation metroStation = estacoes.get(stationName);
					
					links.append("    <link nameOrigin=\"");
					links.append(stationName);
					links.append("\" nameDestination=\"");
					links.append(lastStation);
					links.append("\" idOrigin=\"");
					links.append(metroStation.getIdNode());
					links.append("\" idDestination=\"");
					links.append(idLastStation);
					links.append("\" />\n");
					
					lastStation = stationName;
					idLastStation = metroStation.getIdNode();
				
					
				}
				
			}
			lastLine = line;
			cont++;
			
		}
		writer.println("  <stations>");
	    writer.println(stations.toString());
		writer.println("  </stations>");
		writer.println("  <links>");
	    writer.println(links.toString());
		writer.println("  </links>");
	    writer.println("</metro>");
	    writer.close();
		
	}
	
	
}
