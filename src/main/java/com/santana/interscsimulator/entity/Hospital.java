package com.santana.interscsimulator.entity;

public class Hospital {
	
	private String id;
	private long idNode;
	private float lat;
	private float lon;
	private String nome;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getIdNode() {
		return idNode;
	}
	public void setIdNode(long idNode) {
		this.idNode = idNode;
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
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

}
