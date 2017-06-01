package com.santana.interscsimulator.entity;

public class MetroStation {
	
	private int id;
	private String name;
	private double lat;
	private double lon;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public long getIdNode() {
		return idNode;
	}
	public void setIdNode(long idNode) {
		this.idNode = idNode;
	}
	private long idNode;
	

}
