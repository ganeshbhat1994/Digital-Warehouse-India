package com.example.w.maps.warehouse.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.w.maps.warehouse.WarehouseDetails;

import java.util.ArrayList;

/**
 * Created by ganesh on 26/11/15.
 */
public class MyList extends ArrayList<WarehouseDetails> implements Parcelable {

    private static final long serialVersionUID = 663585476779879096L;

    public MyList(){

    }

    public MyList(Parcel in){

        readFromParcel(in);

    }

    @SuppressWarnings("unchecked")

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public MyList createFromParcel(Parcel in) {

            return new MyList(in);

        }

        public Object[] newArray(int arg0) {

            return null;

        }
    };

    private void readFromParcel(Parcel in) {

        this.clear();
        //First we have to read the list size

        int size = in.readInt();


        //Reading remember that we wrote first the Name and later the Phone Number.

        //Order is fundamental

        for (int i = 0; i < size; i++) {

            WarehouseDetails c = new WarehouseDetails();

            c.setWcapacity(in.readInt());
            c.setWavailability(in.readInt());
            c.setNoRaters(in.readInt());
            c.setuId(in.readInt());
            c.setWname(in.readString());
            c.setPhone(in.readString());
            c.setOwnName(in.readString());
            c.setWaddress1(in.readString());
            c.setWaddress2(in.readString());
            c.setWaddress3(in.readString());
            c.setType(in.readString());
            c.setStorage_type(in.readString());
            c.setRegdate(in.readString());
            c.setRegperiod(in.readString());
            c.setWregno(in.readString());
            c.setRent(in.readFloat());
            c.setRating(in.readFloat());

            this.add(c);
        }
    }
    public int describeContents() {

        return 0;

    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        int size = this.size();
        dest.writeInt(size);
        for (int i = 0; i < size; i++) {
            WarehouseDetails c = this.get(i);
            dest.writeInt(c.getWcapacity());
            dest.writeInt(c.getWavailability());
            dest.writeInt(c.getNoRaters());
            dest.writeInt(c.getuId());
            dest.writeString(c.getWname());
            dest.writeString(c.getPhone());
            dest.writeString(c.getOwnName());
            dest.writeString(c.getWaddress1());
            dest.writeString(c.getWaddress2());
            dest.writeString(c.getWaddress3());
            dest.writeString(c.getType());
            dest.writeString(c.getStorage_type());
            dest.writeString(c.getRegdate());
            dest.writeString(c.getRegperiod());
            dest.writeString(c.getWregno());
            dest.writeFloat(c.getRent());
            dest.writeFloat(c.getRating());

        }
    }

}


