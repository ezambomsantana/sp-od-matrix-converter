package com.santana.interscsimulator.entity;

public class MapPoint {
	
	private long id;
	private long idLink;
	private float lat;
	private float lon;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdLink() {
		return idLink;
	}
	public void setIdLink(long idLink) {
		this.idLink = idLink;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLon() {
		return lon;
	}
	public void setLon(float lon) {
		this.lon = lon;
	}
	
	@Override
	public String toString() {
		return id + " " + lat + " " + lon;
	}
}
