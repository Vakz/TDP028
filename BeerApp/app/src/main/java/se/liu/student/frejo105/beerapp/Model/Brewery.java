package se.liu.student.frejo105.beerapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vakz on 12/27/15.
 */
public class Brewery implements Parcelable {
    public String name;
    public String _id;

    public Brewery () { }

    protected Brewery(Parcel in) {
        name = in.readString();
        _id = in.readString();
    }

    public static final Creator<Brewery> CREATOR = new Creator<Brewery>() {
        @Override
        public Brewery createFromParcel(Parcel in) {
            return new Brewery(in);
        }

        @Override
        public Brewery[] newArray(int size) {
            return new Brewery[size];
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
    }
}
