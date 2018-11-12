package server.entities;

import java.util.ArrayList;

public class Intersection {

	private double x;
	private double y;
	
	private ArrayList<User> bikers;
	private ArrayList<User> drivers;
	
	
	public Intersection(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}


	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}


	public ArrayList<User> getBikers() {
		return bikers;
	}


	public void setBikers(ArrayList<User> bikers) {
		this.bikers = bikers;
	}


	public ArrayList<User> getDrivers() {
		return drivers;
	}


	public void setDrivers(ArrayList<User> drivers) {
		this.drivers = drivers;
	}
	
	
	
	
}
