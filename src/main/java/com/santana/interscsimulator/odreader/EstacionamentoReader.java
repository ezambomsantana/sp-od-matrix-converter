package com.santana.interscsimulator.odreader;

import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.santana.interscsimulator.db.Connector;

/**
 * 
 * This file reads an open street maps file and saves all the nodes and links to a PostgreSQL database.
 * It is done to use the find the closest point in a graph method from the database.
 * 
 * @author ezamb
 *
 */

public class EstacionamentoReader {
	
	private static final String inputFile = "/home/eduardo/entrada/estacionamentos.xml";
	private static final String inputFile2 = "/home/eduardo/entrada/estacionamentos2.xml";
	private static final String outputFile = "/home/eduardo/entrada/park.xml";
	
	public static void main(String args[])
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		// Maph file
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document docMap = builder.parse(inputFile);
		
		PrintWriter writer = new PrintWriter(outputFile, "UTF-8");

	    writer.println("<park>");	 
	    
	    StringBuilder sb = new StringBuilder();
		
		docMap.getDocumentElement().normalize();
		NodeList nList = docMap.getElementsByTagName("spot");

		for (int i = 0; i < nList.getLength(); i++) {

			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				
				System.out.println(i);
								
				Element eElement = (Element) nNode;

				String id = eElement.getElementsByTagName("uuid").item(0).getTextContent();
				Node coordinates = eElement.getElementsByTagName("coordinates").item(0);
				
				String lat = ((Element) coordinates).getElementsByTagName("lat").item(0).getTextContent();
				String lon = ((Element) coordinates).getElementsByTagName("lon").item(0).getTextContent();
				

				long [] idsOrigin = null;
	    		int dist = 1000;
				while (idsOrigin == null) {
					idsOrigin = Connector.selectNearestPoint(Double.parseDouble(lat), Double.parseDouble(lon), dist);
					dist = dist * 5;						
				}
				
				
				
				sb.append("    <spot uuid=\"");
				sb.append(id);
				sb.append("\" node=\"");
				sb.append(idsOrigin[0]);
				sb.append("\" lat=\"");
				sb.append(lat);
				sb.append("\" lon=\"");
				sb.append(lon);
				sb.append("\" />\n");
				
				
				
			}

		}
		
		docMap = builder.parse(inputFile2);		
		
		docMap.getDocumentElement().normalize();
		nList = docMap.getElementsByTagName("spot");
		
		for (int i = 0; i < nList.getLength(); i++) {

			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				
				System.out.println(i);
								
				Element eElement = (Element) nNode;

				String id = eElement.getElementsByTagName("uuid").item(0).getTextContent();
				Node coordinates = eElement.getElementsByTagName("coordinates").item(0);
				
				String lat = ((Element) coordinates).getElementsByTagName("lat").item(0).getTextContent();
				String lon = ((Element) coordinates).getElementsByTagName("lon").item(0).getTextContent();
				

				long [] idsOrigin = null;
	    		int dist = 1000;
				while (idsOrigin == null) {
					idsOrigin = Connector.selectNearestPoint(Double.parseDouble(lat), Double.parseDouble(lon), dist);
					dist = dist * 5;						
				}
				
				
				
				sb.append("    <spot uuid=\"");
				sb.append(id);
				sb.append("\" node=\"");
				sb.append(idsOrigin[0]);
				sb.append("\" lat=\"");
				sb.append(lat);
				sb.append("\" lon=\"");
				sb.append(lon);
				sb.append("\" />\n");							
				
			}

		}
		
		writer.println(sb.toString());

	    writer.println("</park>");
	    writer.close();	
		

	}

}
