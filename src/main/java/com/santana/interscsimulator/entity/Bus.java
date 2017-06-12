package com.santana.interscsimulator.entity;

import java.util.ArrayList;
import java.util.List;

public class Bus {
	
	private String id;
	private List<Long> stops = new ArrayList<Long>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Long> getStops() {
		return stops;
	}
	public void setStops(List<Long> stops) {
		this.stops = stops;
	}

}
