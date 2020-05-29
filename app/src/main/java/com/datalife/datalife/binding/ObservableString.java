package com.datalife.datalife.binding;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by LG on 2018/1/18.
 */

public class ObservableString extends BaseObservable implements Parcelable, Serializable {

    static final long serialVersionUID = 1L;
    private String mValue = "";

    /**
     * Creates an ObservableBoolean with the given initial value.
     *
     * @param value the initial value for the ObservableString
     */
    public ObservableString(String value) {
        mValue = value == null ? "" : value;
    }

    /**
     * Creates an ObservableBoolean with the initial value of <code>false</code>.
     */
    public ObservableString() {
    }

    /**
     * @return the stored value.
     */
    public String get() {
        return mValue;
    }

    /**
     * Set the stored value.
     */
    public void set(String value) {
        if (!mValue.equals(value)) {
            mValue = value == null ? "" : value;
            notifyChange();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mValue);
    }

    public static final Creator<ObservableString> CREATOR
            = new Creator<ObservableString>() {

        @Override
        public ObservableString createFromParcel(Parcel source) {
            return new ObservableString(source.readString());
        }

        @Override
        public ObservableString[] newArray(int size) {
            return new ObservableString[size];
        }
    };
}
