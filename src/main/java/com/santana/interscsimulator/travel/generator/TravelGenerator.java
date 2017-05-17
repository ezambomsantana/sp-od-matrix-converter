package com.santana.interscsimulator.travel.generator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.PrintWriter;

public class TravelGenerator {

	public static void main(String argv[]) {

		try {

			File fXmlFile = new File("c:/dev/map.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			StringBuilder sb = new StringBuilder();
			sb.append("<scsimulator_matrix>\n");

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("link");

			for (int temp = 0; temp < 30000; temp++) {

				int nOrigin = (int) (Math.random() * nList.getLength());
				int nDestination = (int) (Math.random() * nList.getLength());

				int count = (int) (Math.random() * 2);
				int start = (int) (Math.random() * 500);

				Node from = nList.item(nOrigin);
				Node to = nList.item(nDestination);

				Element eElement = (Element) from;
				Element eElementTo = (Element) to;

				String id = eElement.getAttribute("id");
				String idFrom = eElement.getAttribute("from");
				String idTo = eElementTo.getAttribute("to");

				sb.append("<trip origin=\"");
				sb.append(idFrom);
				sb.append("\" link_origin=\"");
				sb.append(id);
				sb.append("\" destination=\"");
				sb.append(idTo);
				sb.append("\" count=\"");
				sb.append(count + 1);
				sb.append("\" start=\"");
				sb.append(start + 1);
				if (temp < 15000) {
					sb.append("\" type=\"work\"");
				} else if (temp < 29800) {
					sb.append("\" type=\"home\"");					
				} else if (temp < 30100) {
					sb.append("\" type=\"hospital\"");					
				}
				sb.append("/>\n");

			}
			sb.append("</scsimulator_matrix>");

			PrintWriter out = new PrintWriter("c:/dev/trips.xml");
			out.write(sb.toString());
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
