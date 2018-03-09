package com.example.team38.utils;

import android.os.Parcel;
import android.os.Parcelable;
import com.example.team38.HomelessShelter;

/**
 * Created by anish on 3/8/18.
 * Ghetto way to pass around lambdas
 */

public class ShelterSelector implements Parcelable {
    public ShelterSelector() {

    }

    public ShelterSelector(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShelterSelector> CREATOR = new Creator<ShelterSelector>() {
        @Override
        public ShelterSelector createFromParcel(Parcel in) {
            return new ShelterSelector(in);
        }

        @Override
        public ShelterSelector[] newArray(int size) {
            return new ShelterSelector[size];
        }
    };

    public boolean should_select(HomelessShelter shelter) {
        return true;
    }
}
