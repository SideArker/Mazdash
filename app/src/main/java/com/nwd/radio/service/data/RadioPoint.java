package com.nwd.radio.service.data;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class RadioPoint implements Parcelable {
    public static final Parcelable.Creator<RadioPoint> CREATOR = new Parcelable.Creator<RadioPoint>() { // from class: com.nwd.radio.service.data.RadioPoint.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RadioPoint createFromParcel(Parcel in) {
            return new RadioPoint(in, null);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RadioPoint[] newArray(int size) {
            return new RadioPoint[size];
        }
    };
    private int mFrequencyMax;
    private int mFrequencyMin;
    private int mFrequencyStep;

    public RadioPoint(int aFrequencyMin, int aFrequencyMax, int aFrequencyStep) {
        this.mFrequencyMax = aFrequencyMax;
        this.mFrequencyMin = aFrequencyMin;
        this.mFrequencyStep = aFrequencyStep;
    }

    public int getFrequencyMin() {
        return this.mFrequencyMin;
    }

    public int getFrequencyMax() {
        return this.mFrequencyMax;
    }

    public int getFrequencyStep() {
        return this.mFrequencyStep;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("max = ").append(this.mFrequencyMax).append(",min = ").append(this.mFrequencyMin).append(",step = ").append(this.mFrequencyStep);
        return sb.toString();
    }

    public static Parcelable.Creator<RadioPoint> getCreator() {
        return CREATOR;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mFrequencyMin);
        dest.writeInt(this.mFrequencyMax);
        dest.writeInt(this.mFrequencyStep);
    }

    private RadioPoint(Parcel in) {
        this.mFrequencyMin = in.readInt();
        this.mFrequencyMax = in.readInt();
        this.mFrequencyStep = in.readInt();
    }

    /* synthetic */ RadioPoint(Parcel parcel, RadioPoint radioPoint) {
        this(parcel);
    }
}
