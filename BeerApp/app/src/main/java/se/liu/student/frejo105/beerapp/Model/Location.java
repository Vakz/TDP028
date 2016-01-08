package se.liu.student.frejo105.beerapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vakz on 12/27/15.
 */
public class Location implements Parcelable {
    public String type;
    public double longitude;
    public double latitude;

    public Location() { }

    protected Location(Parcel in) {
        type = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public android.location.Location toAndroidLocation() {
        android.location.Location loc = new android.location.Location("");
        loc.setLongitude(longitude);
        loc.setLongitude(latitude);
        return loc;
    }

    public double distanceTo(Location dest) {
        return toAndroidLocation().distanceTo(dest.toAndroidLocation());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }

    @Override
    public String toString() {
        return "Latitude: " + String.valueOf(latitude) + ", Longitude: " + String.valueOf(longitude);
    }
}
