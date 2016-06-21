package se.liu.student.frejo105.beerapp.api.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vakz on 2016-06-19.
 */
public class Beer implements Parcelable {
    public int id;
    public String name;
    public String brewery;
    public String beerType;
    public String description;

    public Beer() {}

    protected Beer(Parcel in) {
        id = in.readInt();
        name = in.readString();
        brewery = in.readString();
        beerType = in.readString();
        description = in.readString();
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
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(brewery);
        dest.writeString(beerType);
        dest.writeString(description);
    }
}
