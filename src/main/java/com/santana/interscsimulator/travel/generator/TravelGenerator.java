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

			File mapFile = new File("/home/eduardo/map.xml");
			//File hospitalFile = new File("/home/eduardo/entrada/hospital/hospitals.xml");
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document docMap = dBuilder.parse(mapFile);
			//Document docHospital = dBuilder.parse(hospitalFile);

			StringBuilder sb = new StringBuilder();
			sb.append("<scsimulator_matrix>\n");

			docMap.getDocumentElement().normalize();
			//docHospital.getDocumentElement().normalize();

			NodeList nList = docMap.getElementsByTagName("link");

			for (int temp = 0; temp < 1000; temp++) {

				int nOrigin = (int) (Math.random() * nList.getLength());
				int nDestination = (int) (Math.random() * nList.getLength());

				int count = (int) (Math.random() * 2);
				int start = (int) (Math.random() * 500);

				Node from = nList.item(nOrigin);
				Node to = nList.item(nDestination);

				Element eElement = (Element) from;
				Element eElementTo = (Element) to;

				String idLinkOrigin = eElement.getAttribute("id");
				String idFrom = eElement.getAttribute("from");
				String idTo = eElementTo.getAttribute("to");
				
				if (temp < 1000) {
					sb.append("<trip name=\"");
					sb.append(temp + 1);
					sb.append("\" origin=\"");
					sb.append(idFrom);
					sb.append("\" link_origin=\"");
					sb.append(idLinkOrigin);
					sb.append("\" destination=\"");
					sb.append(idTo);
					sb.append("\" count=\"");
					sb.append(count + 1);
					sb.append("\" start=\"");
					sb.append(start + 1);
					sb.append("\" type=\"work\"");
					sb.append(" mode=\"car\"");
					sb.append("/>\n");
				} else if (temp < -1) {
					sb.append("<trip origin=\"");
					sb.append(idFrom);
					sb.append("\" link_origin=\"");
					sb.append(idLinkOrigin);
					sb.append("\" destination=\"");
					sb.append(idTo);
					sb.append("\" count=\"");
					sb.append(count + 1);
					sb.append("\" start=\"");
					sb.append(start + 1);
					sb.append("\" type=\"home\"");
					sb.append(" mode=\"car\"");
					sb.append("/>\n");				
				} else if (temp < -1) {
															
					MapPoint ponto = Connector.getPointById(idFrom);
					long idHospital = Connector.selectNearestHospital(ponto.getLat(), ponto.getLon(), 10000);
					
					sb.append("<trip origin=\"");
					sb.append(idFrom);
					sb.append("\" link_origin=\"");
					sb.append(idLinkOrigin);
					sb.append("\" destination=\"");
					sb.append(idHospital);
					sb.append("\" count=\"");
					sb.append(count + 1);
					sb.append("\" start=\"");
					sb.append(start + 1);
					sb.append("\" type=\"hospital\"");
					sb.append(" mode=\"car\"");
					sb.append("/>\n");
								
				} else if (temp < 1050) {
					
					MapPoint pontoOrigin = Connector.getPointById(idFrom);
					MapPoint pontoDestination = Connector.getPointById(idTo);
					long idMetroOrigin = Connector.selectNearestMetroStation(pontoOrigin.getLat(), pontoOrigin.getLon(), 10000);
					long idMetroDestination = Connector.selectNearestMetroStation(pontoDestination.getLat(), pontoDestination.getLon(), 10000);

					MapPoint pointOrigin = Connector.getPointById(String.valueOf(idMetroOrigin));
					MapPoint pointDestination = Connector.getPointById(String.valueOf(idMetroDestination));
					if (pointDestination == null) {
						continue;
					}
					
					if (pointOrigin == null) {
						continue;
					}
					
					if (idMetroDestination == idMetroOrigin) {
						continue;
					}
					
					sb.append("<multi_trip ");
					sb.append(" count=\"");
					sb.append(count + 1);
					sb.append("\" start=\"");
					sb.append(start + 1);
					sb.append("\" type=\"hospital\"");
					sb.append(">\n");
					
					sb.append("    <trip origin=\"");
					sb.append(idFrom);
					sb.append("\" link_origin=\"");
					sb.append(idLinkOrigin);
					sb.append("\" destination=\"");
					sb.append(idMetroOrigin);
					sb.append("\" mode=\"walk\"");
					sb.append("/>\n");				
					
					sb.append("    <trip origin=\"");
					sb.append(idMetroOrigin);
					sb.append("\" link_origin=\"");
					sb.append(pointOrigin.getIdLink());
					sb.append("\" destination=\"");
					sb.append(idMetroDestination);
					sb.append("\" link_destination=\"");
					sb.append(pointDestination.getIdLink());
					sb.append("\" mode=\"metro\"");
					sb.append("/>\n");			
					
					sb.append("    <trip origin=\"");
					sb.append(idMetroDestination);
					sb.append("\" link_origin=\"");
					sb.append(pointDestination.getIdLink());
					sb.append("\" destination=\"");
					sb.append(idTo);
					sb.append("\" mode=\"walk\"");
					sb.append("/>\n");			
					
					sb.append("</multi_trip>\n");			
										
					
				}

			}
			sb.append("</scsimulator_matrix>");

			PrintWriter out = new PrintWriter("/home/eduardo/trips.xml");
			out.write(sb.toString());
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
