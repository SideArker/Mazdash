package com.bt;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class BTDevice implements Parcelable {
    public static final Parcelable.Creator<BTDevice> CREATOR = new Parcelable.Creator<BTDevice>() { // from class: com.bt.BTDevice.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BTDevice createFromParcel(Parcel parcel) {
            return new BTDevice(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BTDevice[] newArray(int i) {
            return new BTDevice[i];
        }
    };
    private boolean isPair;
    private String mAddress;
    private int mDeviceClass;
    private String mName;
    private String mObdPassword;
    private int mPairRecordIndex;
    private int mService;

    public BTDevice() {
        this((String) null, (String) null);
    }

    private BTDevice(Parcel parcel) {
        this.mAddress = parcel.readString();
        this.mName = parcel.readString();
        this.mDeviceClass = parcel.readInt();
        this.mService = parcel.readInt();
        this.isPair = parcel.readInt() != 0;
        this.mPairRecordIndex = parcel.readInt();
        this.mObdPassword = parcel.readString();
    }

    public BTDevice(String str, String str2) {
        this.mAddress = str;
        this.mName = str2;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return (obj == null || !(obj instanceof BTDevice)) ? super.equals(obj) : this.mAddress.equalsIgnoreCase(((BTDevice) obj).getAddress());
    }

    public String getAddress() {
        return this.mAddress;
    }

    public int getDeviceClass() {
        return this.mDeviceClass;
    }

    public String getName() {
        return this.mName;
    }

    public String getObdPassword() {
        return this.mObdPassword;
    }

    public int getPairRecordIndex() {
        return this.mPairRecordIndex;
    }

    public int getService() {
        return this.mService;
    }

    public int hashCode() {
        return this.mAddress != null ? this.mAddress.hashCode() : super.hashCode();
    }

    public boolean isPair() {
        return this.isPair;
    }

    public void setAddress(String str) {
        this.mAddress = str;
    }

    public void setDeviceClass(int i) {
        this.mDeviceClass = i;
    }

    public void setName(String str) {
        this.mName = str;
    }

    public void setObdPassword(String str) {
        this.mObdPassword = str;
    }

    public void setPair(boolean z) {
        this.isPair = z;
    }

    public void setPairRecordIndex(int i) {
        this.mPairRecordIndex = i;
    }

    public void setService(int i) {
        this.mService = i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("address = ").append(this.mAddress).append(",name = ").append(this.mName).append(",isPair = ").append(this.isPair).append(",class = ").append(this.mDeviceClass).append(",service = ").append(this.mService);
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mAddress);
        parcel.writeString(this.mName);
        parcel.writeInt(this.mDeviceClass);
        parcel.writeInt(this.mService);
        parcel.writeInt(this.isPair ? 1 : 0);
        parcel.writeInt(this.mPairRecordIndex);
        parcel.writeString(this.mObdPassword);
    }
}
