package se.liu.student.frejo105.beerapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by vakz on 12/27/15.
 */
public class Pub implements Parcelable {
    public String name;
    public String _id;
    public String desc;
    public Location loc;
    public ArrayList<Beer> serves;

    public Pub() { }

    protected Pub(Parcel in) {
        name = in.readString();
        _id = in.readString();
        loc = in.readParcelable(Location.class.getClassLoader());
        serves = in.createTypedArrayList(Beer.CREATOR);
        desc = in.readString();
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
        dest.writeString(name);
        dest.writeString(_id);
        dest.writeParcelable(loc, flags);
        dest.writeTypedList(serves);
        dest.writeString(desc);
    }
}
