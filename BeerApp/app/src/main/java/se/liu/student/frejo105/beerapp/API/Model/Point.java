package se.liu.student.frejo105.beerapp.API.Model;

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
}
