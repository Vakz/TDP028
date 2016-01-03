package se.liu.student.frejo105.beerapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vakz on 12/27/15.
 */
public class Beer implements Parcelable {
    public String name;
    public String _id;
    public Brewery brewery;
    public BeerType beerType;
    public String desc;

    public Beer() { }

    protected Beer(Parcel in) {
        name = in.readString();
        _id = in.readString();
        brewery = in.readParcelable(Brewery.class.getClassLoader());
        beerType = in.readParcelable(BeerType.class.getClassLoader());
        desc = in.readString();
    }

    public static final Creator<Beer> CREATOR = new Creator<Beer>() {
        @Override
        public Beer createFromParcel(Parcel in) {
            return new Beer(in);
        }

        @Override
        public Beer[] newArray(int size) {
            return new Beer[size];
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
        dest.writeParcelable(brewery, flags);
        dest.writeParcelable(beerType, flags);
        dest.writeString(desc);
    }
}
