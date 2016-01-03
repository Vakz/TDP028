package se.liu.student.frejo105.beerapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vakz on 12/27/15.
 */
public class BeerType implements Parcelable {
    public String typeName;
    public String _id;

    public BeerType() { }

    protected BeerType(Parcel in) {
        typeName = in.readString();
        _id = in.readString();
    }

    public static final Creator<BeerType> CREATOR = new Creator<BeerType>() {
        @Override
        public BeerType createFromParcel(Parcel in) {
            return new BeerType(in);
        }

        @Override
        public BeerType[] newArray(int size) {
            return new BeerType[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(typeName);
        dest.writeString(_id);
    }
}
