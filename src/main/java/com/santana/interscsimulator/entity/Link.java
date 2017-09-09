/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teste;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ezamb
 */
public class Link {
    
    private String origin;
    private String destination;
    private List<String> buses = new ArrayList<String>();

    /**
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * @param destination the destination to set
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * @return the buses
     */
    public List<String> getBuses() {
        return buses;
    }

    /**
     * @param buses the buses to set
     */
    public void setBuses(List<String> buses) {
        this.buses = buses;
    }
    
}
