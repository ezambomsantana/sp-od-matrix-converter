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
	
	public static List<String> buses;
	public static List<String> path;

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
            String buses = l.getOrigin() + ":" + l.getDestination() + " ";
            for (String b : l.getBuses()) {
                buses = buses + b + " ";
            }
            g.addEdge(buses + " ", l.getOrigin(), l.getDestination());
        }
        
    }
    
    public static void getShortestPath(String origin, String destionation) {
        
        DijkstraShortestPath<String,String> alg = new DijkstraShortestPath<String,String>(g);
        List<String> l = alg.getPath(origin, destionation);
        
        if (l == null || l.size() == 0) {
        	buses = null;
        	return;
        }
        
        System.out.println("     ");
        for (int i = 0; i < l.size(); i++) {
        	System.out.println(l.get(i));
        }
              
        String caminhoBuses = "";
        String caminhosStops = "";
        
        String lastProximo = "";
        String lastStop = "";
                
        List<String> candidatos = new ArrayList<String>(Arrays.asList(l.get(0).split(" ")));
         
        for (int i = 1; i < l.size(); i++) {
                        
            boolean achouAlgumCandidato = false;
            List<String> candidatosProximo = Arrays.asList(l.get(i).split(" "));   
            List<String> removerCandidatos = new ArrayList<String>(); 

            for (int j = 1; j < candidatos.size(); j++) {                
                String proximo = candidatos.get(j);
                if (!candidatosProximo.contains(proximo)) {  
                    lastProximo = proximo;
                    lastStop = candidatosProximo.get(0);  
                    candidatosProximo.remove(proximo);  
                    removerCandidatos.add(proximo);
                } else {               
                    achouAlgumCandidato = true;
                }           
            }
            candidatos.removeAll(removerCandidatos);
            
            if (!achouAlgumCandidato) {                
            	caminhoBuses = caminhoBuses + lastProximo + " ";
            	caminhosStops = caminhosStops + lastStop + " ";
                candidatos = new ArrayList<String>(candidatosProximo);  
            }        
            
            if (i == l.size() - 1) {  
            	caminhoBuses = caminhoBuses + candidatos.get(1) + " ";
            	caminhosStops = caminhosStops + candidatosProximo.get(0) + " ";
            }
               
        }
        
        
        buses = Arrays.asList(caminhoBuses.split(" "));    
        path = Arrays.asList(caminhosStops.split(" "));    
        for (String b : buses) {
        	System.out.println(b);
        }
        
        for (String p : path) {
        	System.out.println(p);
        }
    
    }
		
	
}
