package com.santana.interscsimulator.entity;

public class Point {
	
	private double latOrigin;
	private double lonOrigin;
	private double latDestination;
	private double lonDestination;
	private int hourStart;
	private int minuteStart;
	
	public double getLatOrigin() {
		return latOrigin;
	}
	public void setLatOrigin(double latOrigin) {
		this.latOrigin = latOrigin;
	}
	public double getLonOrigin() {
		return lonOrigin;
	}
	public void setLonOrigin(double lonOrigin) {
		this.lonOrigin = lonOrigin;
	}
	public double getLatDestination() {
		return latDestination;
	}
	public void setLatDestination(double latDestination) {
		this.latDestination = latDestination;
	}
	public double getLonDestination() {
		return lonDestination;
	}
	public void setLonDestination(double lonDestination) {
		this.lonDestination = lonDestination;
	}
	public int getHourStart() {
		return hourStart;
	}
	public void setHourStart(int hourStart) {
		this.hourStart = hourStart;
	}
	public int getMinuteStart() {
		return minuteStart;
	}
	public void setMinuteStart(int minuteStart) {
		this.minuteStart = minuteStart;
	}
	
	public int getTimeStart() {
		return ((this.hourStart * 60 * 60) + this.minuteStart * 60) + 1;
	}
	
	@Override
	public String toString() {
		
		return "" + latOrigin + " " + lonOrigin + " " + latDestination + " " + lonDestination + " " + hourStart + " " + minuteStart;
	}

}
