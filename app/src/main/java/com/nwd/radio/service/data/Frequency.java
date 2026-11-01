package com.nwd.radio.service.data;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class Frequency implements Parcelable {
    public static final Parcelable.Creator<Frequency> CREATOR = new Parcelable.Creator<Frequency>() { // from class: com.nwd.radio.service.data.Frequency.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Frequency createFromParcel(Parcel in) {
            return new Frequency(in, null);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Frequency[] newArray(int size) {
            return new Frequency[size];
        }
    };
    private byte mBandType;
    private int mFrequency;
    private String mPSName;

    public Frequency(byte mBandType, int mFrequency, String mPSName) {
        this.mBandType = mBandType;
        this.mFrequency = mFrequency;
        this.mPSName = mPSName;
    }

    public byte getBandType() {
        return this.mBandType;
    }

    public void setBandType(byte mBandType) {
        this.mBandType = mBandType;
    }

    public int getFrequency() {
        return this.mFrequency;
    }

    public void setFrequency(int mFrequency) {
        this.mFrequency = mFrequency;
    }

    public void copy(Frequency another) {
        this.mFrequency = another.getFrequency();
        this.mBandType = another.getBandType();
        this.mPSName = another.getPSName();
    }

    public boolean equals(Object o) {
        if (o instanceof Frequency) {
            Frequency obj = (Frequency) o;
            if (this.mFrequency == obj.getFrequency() && this.mBandType == obj.getBandType()) {
                return true;
            }
            return false;
        }
        return super.equals(o);
    }

    public String getPSName() {
        return this.mPSName;
    }

    public void setPSName(String mPSName) {
        this.mPSName = mPSName;
    }

    public String toString() {
        return "frequency = " + this.mFrequency + ",band = " + ((int) this.mBandType) + ",psname=" + this.mPSName;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.mBandType);
        dest.writeString(this.mPSName);
        dest.writeInt(this.mFrequency);
    }

    private Frequency(Parcel in) {
        this.mBandType = in.readByte();
        this.mPSName = in.readString();
        this.mFrequency = in.readInt();
    }

    /* synthetic */ Frequency(Parcel parcel, Frequency frequency) {
        this(parcel);
    }
}
