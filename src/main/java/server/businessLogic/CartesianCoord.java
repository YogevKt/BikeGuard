package server.businessLogic;

import server.entities.GpsCoords;

import javax.persistence.Transient;

public class CartesianCoord{
    private double x;
    private double y;
    private double z;
    @Transient
    private GpsCoords coords;

    private final double R = 6371; // in km

    public CartesianCoord() {
    }

    public CartesianCoord(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setCoords(GpsCoords coords){
        this.coords=coords;
    }

    public GpsCoords getGPSCoords() {
        return coords;
    }

    public double distance(CartesianCoord other) {
        double y = ( this.getY() - other.getY() ) * ( this.getY() - other.getY() );
        double x = ( (this.getX() - other.getX()) * (this.getX() - other.getX()));
        return Math.sqrt(y + x);
    }

    @Override
    public String toString() {
        return String.format("[%f,%f,%f]", x,y,z);
    }
}