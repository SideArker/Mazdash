package com.bt.hd.link;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class HIDMouseStatus implements Parcelable {
    public static final Parcelable.Creator<HIDMouseStatus> CREATOR = new Parcelable.Creator<HIDMouseStatus>() { // from class: com.bt.hd.link.HIDMouseStatus.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HIDMouseStatus createFromParcel(Parcel parcel) {
            return new HIDMouseStatus(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HIDMouseStatus[] newArray(int i) {
            return new HIDMouseStatus[i];
        }
    };
    private int mousePosX;
    private int mousePosY;
    private byte pressStatus;

    public HIDMouseStatus() {
    }

    private HIDMouseStatus(Parcel parcel) {
        this.pressStatus = parcel.readByte();
        this.mousePosX = parcel.readInt();
        this.mousePosY = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getMousePosX() {
        return this.mousePosX;
    }

    public int getMousePosY() {
        return this.mousePosY;
    }

    public byte getPressStatus() {
        return this.pressStatus;
    }

    public void setMousePosX(int i) {
        this.mousePosX = i;
    }

    public void setMousePosY(int i) {
        this.mousePosY = i;
    }

    public void setPressStatus(byte b) {
        this.pressStatus = b;
    }

    public String toString() {
        new StringBuilder().append("pressStatus").append((int) this.pressStatus).append("mousePosX").append(this.mousePosX).append("mousePosY").append(this.mousePosY);
        return super.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.pressStatus);
        parcel.writeInt(this.mousePosX);
        parcel.writeInt(this.mousePosY);
    }
}
