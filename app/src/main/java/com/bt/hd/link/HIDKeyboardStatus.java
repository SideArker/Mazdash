package com.bt.hd.link;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class HIDKeyboardStatus implements Parcelable {
    public static final Parcelable.Creator<HIDKeyboardStatus> CREATOR = new Parcelable.Creator<HIDKeyboardStatus>() { // from class: com.bt.hd.link.HIDKeyboardStatus.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HIDKeyboardStatus createFromParcel(Parcel parcel) {
            return new HIDKeyboardStatus(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public HIDKeyboardStatus[] newArray(int i) {
            return new HIDKeyboardStatus[i];
        }
    };
    private byte ctrlID;
    private int key0_ID;
    private byte key1_ID;
    private byte key2_ID;
    private byte key3_ID;
    private byte key4_ID;
    private byte key5_ID;
    private byte reportID;

    public HIDKeyboardStatus() {
    }

    private HIDKeyboardStatus(Parcel parcel) {
        this.reportID = parcel.readByte();
        this.ctrlID = parcel.readByte();
        this.key0_ID = parcel.readInt();
        this.key1_ID = parcel.readByte();
        this.key2_ID = parcel.readByte();
        this.key3_ID = parcel.readByte();
        this.key4_ID = parcel.readByte();
        this.key5_ID = parcel.readByte();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public byte getCtrlID() {
        return this.ctrlID;
    }

    public int getKey0_ID() {
        return this.key0_ID;
    }

    public byte getKey1_ID() {
        return this.key1_ID;
    }

    public byte getKey2_ID() {
        return this.key2_ID;
    }

    public byte getKey3_ID() {
        return this.key3_ID;
    }

    public byte getKey4_ID() {
        return this.key4_ID;
    }

    public byte getKey5_ID() {
        return this.key5_ID;
    }

    public byte getReportID() {
        return this.reportID;
    }

    public void setCtrlID(byte b) {
        this.ctrlID = b;
    }

    public void setKey0_ID(int i) {
        this.key0_ID = i;
    }

    public void setKey1_ID(byte b) {
        this.key1_ID = b;
    }

    public void setKey2_ID(byte b) {
        this.key2_ID = b;
    }

    public void setKey3_ID(byte b) {
        this.key3_ID = b;
    }

    public void setKey4_ID(byte b) {
        this.key4_ID = b;
    }

    public void setKey5_ID(byte b) {
        this.key5_ID = b;
    }

    public void setReportID(byte b) {
        this.reportID = b;
    }

    public String toString() {
        new StringBuilder().append("reportID").append((int) this.reportID).append("ctrlID").append((int) this.ctrlID).append("key0_ID").append(this.key0_ID).append("key1_ID").append((int) this.key1_ID).append("key2_ID").append((int) this.key2_ID).append("key3_ID").append((int) this.key3_ID).append("key4_ID").append((int) this.key4_ID).append("key5_ID").append((int) this.key5_ID);
        return super.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte(this.reportID);
        parcel.writeByte(this.ctrlID);
        parcel.writeInt(this.key0_ID);
        parcel.writeByte(this.key1_ID);
        parcel.writeByte(this.key2_ID);
        parcel.writeByte(this.key3_ID);
        parcel.writeByte(this.key4_ID);
        parcel.writeByte(this.key5_ID);
    }
}
