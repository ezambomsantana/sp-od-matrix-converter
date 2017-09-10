package com.santana.interscsimulator.odreader;

import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.santana.interscsimulator.entity.Link;

public class BusTravelGenerator {
	
	private static final String fileName = "/home/eduardo/Pontos_Parada/buses.txt";
	private static Graph g = null;

	public static void init() throws FileNotFoundException, UnsupportedEncodingException {
		
		File file = new File(fileName);
		BufferedReader reader = null;
		
		Map<String, String> node = new HashMap<String, String>();
		Map<String, Link> link = new HashMap<String, Link>();
		

		try {
		    reader = new BufferedReader(new FileReader(file));
		    String text = null;
		    
		    reader.readLine();	
		    
		    String lastNode = "";
		    String lastBus = "";
    		
		    while ((text = reader.readLine()) != null) {
		    	
		    	String [] dados = text.split(";");
		    	String busId = dados[1]; // bus id
		    	String stopId = dados[4]; // stop_id	
		    	
		    	if (!lastBus.equals(busId)) {
			    	lastBus = busId;
		    	} else {
		    		if (!link.containsKey(lastNode + stopId)) {
                                    Link linkClass = new Link();
                                    linkClass.setOrigin(lastNode);
                                    linkClass.setDestination(stopId);
                                    linkClass.getBuses().add(busId);
   			
                                    link.put(lastNode + stopId, linkClass);
			    	} else {
			    		
                                    Link linkClass = link.get(lastNode + stopId);
                                    linkClass.setOrigin(lastNode);
                                    linkClass.setDestination(stopId);
                                    linkClass.getBuses().add(busId); 	    			
		    		    link.put(lastNode + stopId, linkClass);
			    	}
		    	}
		    	
		    	if (!node.containsKey(stopId)) {
		    		node.put(stopId, stopId);
		    	}
		    	
		    	lastNode = stopId;
		    	lastBus = busId;
		    			    	
		    }    	
		    
		    createGraph(node, link);

		} catch (Exception e) {
			e.printStackTrace();
		}	 
	}

    private static void createGraph(Map<String, String> nodes, Map<String, Link> link) {
        
        g = new DirectedSparseMultigraph<String, String>();
        for  (String node : nodes.values()) {
            g.addVertex(node);
        }
        
        for (Link l : link.values()) {
            String buses = l.getOrigin() + ":" + l.getDestination() + ": ";
            for (String b : l.getBuses()) {
                buses = buses + b + " ";
            }
            g.addEdge(buses + " ", l.getOrigin(), l.getDestination());
        }
        
    }
    
    public static List<String> getShortestPath(String origin, String destionation) {
        
        DijkstraShortestPath<String,String> alg = new DijkstraShortestPath(g);
        List<String> l = alg.getPath("830004113", "410003573");
              
        String caminho = "";
                
        List<String> candidatosAnteriores = Arrays.asList(l.get(0).split(" "));
         
        for (int i = 0; i < l.size(); i++) {
                        
            boolean achouAlgumCandidato = false;
            List<String> candidatosProximo = Arrays.asList(l.get(i).split(" "));    
            String lastProximo = "";
            for (int j = 1; j < candidatosAnteriores.size(); j++) {                
                String proximo = candidatosAnteriores.get(j);
                if (!candidatosProximo.contains(proximo)) {  
                   candidatosProximo.remove(proximo);  
                   lastProximo = proximo;
                } else {                 
                    achouAlgumCandidato = true;
                }           
            }
            
            if (!achouAlgumCandidato) {                
                caminho = caminho + lastProximo + " ";
                candidatosAnteriores = candidatosProximo;  
            }        
            
            if (i == l.size() - 1) {  
                caminho = caminho + candidatosAnteriores.get(1) + " ";
            }
               
        }
        
        List<String> buses = Arrays.asList(caminho.split(" "));
        
        return buses;        
    
    }
		
	
}
