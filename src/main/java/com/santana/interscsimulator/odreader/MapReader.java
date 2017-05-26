package com.santana.interscsimulator.odreader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.santana.interscsimulator.db.Connector;
import com.santana.interscsimulator.entity.MapPoint;

/**
 * 
 * This file reads an open street maps file and saves all the nodes and links to a PostgreSQL database.
 * It is done to use the find the closest point in a graph method from the database.
 * 
 * @author ezamb
 *
 */

public class MapReader {
	
	public static void main(String args[])
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		// Maph file
		
		String mapFile = "c:/dev/map.xml";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document docMap = builder.parse(mapFile);
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();

		docMap.getDocumentElement().normalize();
		NodeList nList = docMap.getElementsByTagName("node");
		List<MapPoint> points = new ArrayList<MapPoint>();

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				
				System.out.println(temp);
				

				MapPoint mapPoint = new MapPoint();
				Element eElement = (Element) nNode;

				String id = eElement.getAttribute("id");
				String x = eElement.getAttribute("x");
				String y = eElement.getAttribute("y");
				
				XPathExpression expr = xpath.compile("//link[@from='" + id + "'][1]");
				Element nl = (Element) expr.evaluate(docMap, XPathConstants.NODE);
				mapPoint.setId(Long.parseLong(id));
				mapPoint.setLat(Float.parseFloat(y));
				mapPoint.setLon(Float.parseFloat(x));
				mapPoint.setIdLink(Long.parseLong(nl.getAttribute("id")));
				Connector.insertPoint(mapPoint);
				
			}

		}
		

	}

}
