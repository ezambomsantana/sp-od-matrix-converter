package com.santana.interscsimulator.travel.generator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.santana.interscsimulator.db.Connector;
import com.santana.interscsimulator.entity.MapPoint;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.PrintWriter;

public class TravelGenerator {

	public static void main(String argv[]) {

		try {

			File mapFile = new File("c:/dev/map.xml");
			File hospitalFile = new File("c:/dev/hospitals.xml");
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document docMap = dBuilder.parse(mapFile);
			Document docHospital = dBuilder.parse(hospitalFile);

			StringBuilder sb = new StringBuilder();
			sb.append("<scsimulator_matrix>\n");

			docMap.getDocumentElement().normalize();
			docHospital.getDocumentElement().normalize();

			NodeList nList = docMap.getElementsByTagName("link");
			NodeList nListHospital = docHospital.getElementsByTagName("hospital");

			for (int temp = 0; temp < 1000; temp++) {

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
				
				if (temp < 500) {
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
					sb.append("\" type=\"work\"");
					sb.append("/>\n");
				} else if (temp < 900) {
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
					sb.append("\" type=\"home\"");
					sb.append("/>\n");				
				} else if (temp < 1010) {
															
					MapPoint ponto = Connector.getPointById(idFrom);
					long idHospital = Connector.selectNearestHospital(ponto.getLat(), ponto.getLon(), 10000);
					
					sb.append("<trip origin=\"");
					sb.append(idFrom);
					sb.append("\" link_origin=\"");
					sb.append(id);
					sb.append("\" destination=\"");
					sb.append(idHospital);
					sb.append("\" count=\"");
					sb.append(count + 1);
					sb.append("\" start=\"");
					sb.append(start + 1);
					sb.append("\" type=\"hospital\"");
					sb.append("/>\n");
								
				}

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
