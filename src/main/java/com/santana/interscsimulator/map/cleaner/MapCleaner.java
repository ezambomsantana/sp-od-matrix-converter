package com.santana.interscsimulator.map.cleaner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
 * This file reads an open street maps file and saves all the nodes and links to
 * a PostgreSQL database. It is done to use the find the closest point in a
 * graph method from the database.
 * 
 * @author ezamb
 *
 */

public class MapCleaner {

	public static void main(String args[])
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerException {

		// Maph file

		String mapFile = "/home/eduardo/map.xml";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document docMap = builder.parse(mapFile);
		docMap.getDocumentElement().normalize();
		
		NodeList nodes = docMap.getElementsByTagName("link");	
			
		
		List<Element> remove = new ArrayList<Element>();
		docMap.getElementsByTagName("link").getLength();

		for (int i = 0; i < nodes.getLength(); i++) {
			Element link = (Element) nodes.item(i);
			// <name>
			String type = link.getAttribute("type");
			if (type.equals("unclassified")) {
				remove.add(link);
			}
		}		
		
		
		for (Element e : remove) {			
			e.getParentNode().removeChild(e);			
		}		
				
		docMap.getElementsByTagName("link").getLength();
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
	    transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );
	        
		Result output = new StreamResult(new File("/home/eduardo/map2.xml"));
		Source input = new DOMSource(docMap);

		transformer.transform(input, output);
		
		FileReader fr = new FileReader("/home/eduardo/map2.xml"); 
		BufferedReader br = new BufferedReader(fr); 
		FileWriter fw = new FileWriter("/home/eduardo/map3.xml"); 
		String line;

		while((line = br.readLine()) != null)
		{ 
		    if (!line.equals("")) // don't write out blank lines
		    {
		        fw.write(line, 0, line.length());
		    }
		} 
		fr.close();
		fw.close();
		


	}

	public static void deleteLink(Document doc) throws TransformerFactoryConfigurationError, TransformerException {
		int count = 0;
		// <person>

		
	}
	
	public static void deleteNodes(Document doc, XPath xpath) throws XPathExpressionException {
		int count = 0;

		NodeList nodes = doc.getElementsByTagName("node");

		for (int i = 0; i < nodes.getLength(); i++) {
			count++;
			Element node = (Element) nodes.item(i);
			
			String id = node.getAttribute("id");
			
			XPathExpression expr = xpath.compile("//link[@from='" + id + "'|@to='" + id + "'][1]");
			Element nl = (Element) expr.evaluate(doc, XPathConstants.NODE);			
			
			if (nl == null) {
				node.getParentNode().removeChild(node);				
			}	
			System.out.println(count);
			
		}
		
	}

}
