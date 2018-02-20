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
import com.santana.interscsimulator.entity.MapPoint;

public class TripsReader {
	
	public static void main(String args[])
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		
		String mapFile = "/home/eduardo/trips.xml";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document docMap = builder.parse(mapFile);

		docMap.getDocumentElement().normalize();
		NodeList nList = docMap.getElementsByTagName("trip");

		PrintWriter writer = new PrintWriter("/home/eduardo/trips.csv", "UTF-8");
		
		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				
				System.out.println(temp);
				
				Element eElement = (Element) nNode;

				String destination = eElement.getAttribute("destination");
				int count = Integer.parseInt(eElement.getAttribute("count"));
				
				MapPoint point = Connector.getPointById(destination);
				
				for (int i = 0; i < count; i++) {
			    	StringBuilder builderString = new StringBuilder();
			    	builderString.append(destination).append(";").
			    		append(point.getLat()).append(";").
			    		append(point.getLon());
	
			       	writer.println(builderString.toString());	
				}
			}

		}
		writer.close();
		

	}


}
