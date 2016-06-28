package se.liu.student.frejo105.beerapp.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by vakz on 2016-06-19.
 */
public class Pub
implements Parcelable{
    public int id;
    public String name;
    public String description;
    public double distance;
    public ArrayList<Beer> serves;
    public Point location;

    public Pub () {}

    protected Pub(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        distance = in.readDouble();
        serves = in.createTypedArrayList(Beer.CREATOR);
        location = in.readParcelable(Point.class.getClassLoader());
    }

    public static final Creator<Pub> CREATOR = new Creator<Pub>() {
        @Override
        public Pub createFromParcel(Parcel in) {
            return new Pub(in);
        }

        @Override
        public Pub[] newArray(int size) {
            return new Pub[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(distance);
        dest.writeTypedList(serves);
        dest.writeParcelable(location, flags);
    }
}
