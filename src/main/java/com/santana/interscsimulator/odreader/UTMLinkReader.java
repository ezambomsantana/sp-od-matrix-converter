package com.santana.interscsimulator.odreader;

import java.io.IOException;

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

public class UTMLinkReader {

	public static void main(String args[])
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {

		// Maph file
		
		String mapFile = "/home/eduardo/map.xml";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document docMap = builder.parse(mapFile);

		docMap.getDocumentElement().normalize();
		NodeList nList = docMap.getElementsByTagName("link");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				
				System.out.println(temp);
				
				Element eElement = (Element) nNode;

				String id = eElement.getAttribute("id");
				String from = eElement.getAttribute("from");
				
				Connector.insertLinkUTM(id, from);
				
			}

		}
		

	}

}
