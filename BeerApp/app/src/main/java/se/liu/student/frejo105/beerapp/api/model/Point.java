package se.liu.student.frejo105.beerapp.api.model;

import android.location.Location;

/**
 * Created by vakz on 2016-06-19.
 */
public class Point {
    public double latitude;
    public double longitude;

    @Override
    public String toString() {
        return "Latitude: " + String.valueOf(latitude) + ", Longitude: " + String.valueOf(longitude);
    }

    public Point() {}

    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Point(Location location) {
        if (location == null) {
            System.out.println("Big issues");
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    public android.location.Location toAndroidLocation() {
        android.location.Location loc = new android.location.Location("");
        loc.setLongitude(longitude);
        loc.setLongitude(latitude);
        return loc;
    }

    public double distanceTo(Point dest) {
        return toAndroidLocation().distanceTo(dest.toAndroidLocation());
    }
}
