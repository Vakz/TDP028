package se.liu.student.frejo105.beerapp.api.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vakz on 2016-06-19.
 */
public class Point
implements Parcelable{
    public double latitude;
    public double longitude;

    protected Point(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Point> CREATOR = new Creator<Point>() {
        @Override
        public Point createFromParcel(Parcel in) {
            return new Point(in);
        }

        @Override
        public Point[] newArray(int size) {
            return new Point[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
