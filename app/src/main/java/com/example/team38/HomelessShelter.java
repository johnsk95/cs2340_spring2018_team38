package com.example.team38;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by anish on 3/3/18.
 *
 * Stores information about a homeless shelter
 */

public class HomelessShelter implements Parcelable {
    // 0,My Sister's House,264,Women/Children,-84.410142,33.780174,"921 Howell Mill Road, Atlanta,
    // Georgia 30318","Temporary, Emergency, Residential Recovery",(404) 367-2465
    private int id;
    private String name;
    private long capacity;
    private String allowed;
    private double latitude;
    private double longitude;
    private String address;
    private String shelterType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public String getAllowed() {
        return allowed;
    }

    public void setAllowed(String allowed) {
        this.allowed = allowed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShelterType() {
        return shelterType;
    }

    public void setShelterType(String shelterType) {
        this.shelterType = shelterType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String phoneNumber;

    // Regex for getting info from string
    // String infoMatcher = "([0-9]+?),(.*?),(.*?),(.*?),(.*?),(.*?),\"(.*?)\",\"(.*?)\",(.*)";
    // Pattern infoPattern = Pattern.compile(infoMatcher);

    HomelessShelter() {
        //throw new Exception();
    }
    HomelessShelter(String infostring) throws CouldNotParseInfoException {
        String[] info_parts = infostring.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        if (info_parts.length != 9) {
            throw new CouldNotParseInfoException("Error on parsing: " + infostring);
        }

        for(int i = 0; i < info_parts.length; ++i) {
            if (!info_parts[i].isEmpty() && (info_parts[i].charAt(0) == '\"') &&
                    (info_parts[i].charAt(info_parts[i].length() - 1) == '\"')) {
                info_parts[i] = info_parts[i].substring(1, info_parts[i].length() - 1);
            }
        }

        id = Integer.parseInt(info_parts[0]);
        name = info_parts[1];
        capacity = Long.parseLong(info_parts[2]);
        allowed = info_parts[3];
        latitude = Double.parseDouble(info_parts[4]);
        longitude = Double.parseDouble(info_parts[5]);
        address = info_parts[6];
        shelterType = info_parts[7];
        phoneNumber = info_parts[8];

    }

    HomelessShelter(Parcel in) {

        id = in.readInt();
        name = in.readString();
//        String c = in.readString();
//        if(c == null) {
//            capacity = 0;
//        } else {
//            capacity = Long.parseLong(c);
//        }
        capacity = in.readLong();
        allowed = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        shelterType = in.readString();
        phoneNumber = in.readString();
    }

    /**
     * @param shelter_dictionary take in a dictionary/HashMap to populate from
     */
    public HomelessShelter(HashMap<String, Object> shelter_dictionary) {

        id = (int) ((long) shelter_dictionary.get("id"));
        name = (String) shelter_dictionary.get("name");
        Object c = shelter_dictionary.get("capacity");
        if(c instanceof String) {
            capacity = Long.parseLong((String) c);
        } else {
            capacity = (Long) shelter_dictionary.get("capacity");
        }
        allowed = (String) shelter_dictionary.get("allowed");
        latitude = (Double) shelter_dictionary.get("latitude");
        longitude = (Double) shelter_dictionary.get("longitude");
        address = (String) shelter_dictionary.get("address");
        shelterType = (String) shelter_dictionary.get("services");
        phoneNumber = (String) shelter_dictionary.get("phone");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(name);
        dest.writeLong(capacity);
        dest.writeString(allowed);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
        dest.writeString(shelterType);
        dest.writeString(phoneNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unused")
    public static final Creator<HomelessShelter> CREATOR = new Creator<HomelessShelter>() {
        @Override
        public HomelessShelter createFromParcel(Parcel in) {
            return new HomelessShelter(in);
        }

        @Override
        public HomelessShelter[] newArray(int size) {
            return new HomelessShelter[size];
        }
    };

    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof HomelessShelter) {
            HomelessShelter s = (HomelessShelter) o;
            return (id == s.id) && name.equals(s.name) && (latitude == s.latitude)
                    && (longitude == s.longitude) && (address.equals(s.address)) &&
                    (shelterType.equals(s.shelterType)) && (phoneNumber.equals(s.phoneNumber));
        }
        return false;
    }
    class CouldNotParseInfoException extends Exception {
        public CouldNotParseInfoException(String err) {
            super(err);
        }
    }
}

