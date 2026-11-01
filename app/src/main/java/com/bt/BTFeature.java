package com.bt;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.bt.BTPIMCallback;
import com.bt.hd.link.HIDKeyboardStatus;
import com.bt.hd.link.HIDMouseStatus;
import com.bt.hd.link.SppDataCallBack;

/* loaded from: classes.dex */
public interface BTFeature extends IInterface {

    public static abstract class Stub extends Binder implements BTFeature {
        private static final String DESCRIPTOR = "com.bt.BTFeature";
        static final int TRANSACTION_HIDConnect = 42;
        static final int TRANSACTION_HIDDisConnect = 43;
        static final int TRANSACTION_HIDKeyboardInput = 45;
        static final int TRANSACTION_HIDMouseInput = 46;
        static final int TRANSACTION_SppConnect = 47;
        static final int TRANSACTION_SppDisConnect = 48;
        static final int TRANSACTION_SppSendData = 51;
        static final int TRANSACTION_answerCall = 14;
        static final int TRANSACTION_btReset = 61;
        static final int TRANSACTION_cancelCall = 15;
        static final int TRANSACTION_changeSoundChannel = 28;
        static final int TRANSACTION_connectA2dp = 53;
        static final int TRANSACTION_connectDevice_BC = 4;
        static final int TRANSACTION_deletePairDeviceRecord = 32;
        static final int TRANSACTION_dial_BC = 13;
        static final int TRANSACTION_disConnectDevice_BC = 5;
        static final int TRANSACTION_disconnectA2dp = 54;
        static final int TRANSACTION_getAutoFeature = 30;
        static final int TRANSACTION_getCallLog_CB = 26;
        static final int TRANSACTION_getCallState = 52;
        static final int TRANSACTION_getConnectDevice = 38;
        static final int TRANSACTION_getContact_CB = 25;
        static final int TRANSACTION_getLocalDeviceAddress = 20;
        static final int TRANSACTION_getLocalDeviceFeature = 21;
        static final int TRANSACTION_getLocalDeviceName = 10;
        static final int TRANSACTION_getMaxSpeakerVolume = 56;
        static final int TRANSACTION_getPairDeviceRecord_BC = 31;
        static final int TRANSACTION_getPinCode = 12;
        static final int TRANSACTION_getRemoteDeviceClass = 17;
        static final int TRANSACTION_getRemoteDeviceName = 16;
        static final int TRANSACTION_getSMS_CB = 27;
        static final int TRANSACTION_getSpeakerVolume = 57;
        static final int TRANSACTION_installMhlSoft = 49;
        static final int TRANSACTION_isBTMusicPlaying = 24;
        static final int TRANSACTION_isBlueToothPowerOn = 3;
        static final int TRANSACTION_isBtMusicMute = 59;
        static final int TRANSACTION_isConnectA2DP = 36;
        static final int TRANSACTION_isConnectAVRCP = 37;
        static final int TRANSACTION_isConnectDevice = 6;
        static final int TRANSACTION_isConnectHFP = 35;
        static final int TRANSACTION_isHIDConnect = 44;
        static final int TRANSACTION_isSppConnect = 50;
        static final int TRANSACTION_loadContactFromPhone = 62;
        static final int TRANSACTION_playControl = 23;
        static final int TRANSACTION_queryA2dpId3Info = 60;
        static final int TRANSACTION_registPIMCallback = 33;
        static final int TRANSACTION_registSppDataCallback = 40;
        static final int TRANSACTION_sendDTMF = 22;
        static final int TRANSACTION_setAutoFeature_BC = 29;
        static final int TRANSACTION_setBtMusicMute = 39;
        static final int TRANSACTION_setDiscoverable = 18;
        static final int TRANSACTION_setLocalDeviceName = 9;
        static final int TRANSACTION_setMute = 58;
        static final int TRANSACTION_setPairable = 19;
        static final int TRANSACTION_setPinCode = 11;
        static final int TRANSACTION_setSpeakerVolume = 55;
        static final int TRANSACTION_startBlueTooth_BC = 1;
        static final int TRANSACTION_startSearchDevice_BC = 7;
        static final int TRANSACTION_stopBlueTooth_BC = 2;
        static final int TRANSACTION_stopSearchDevice_BC = 8;
        static final int TRANSACTION_unregistPIMCallback = 34;
        static final int TRANSACTION_unregistSppDataCallback = 41;

        private static class Proxy implements BTFeature {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // com.bt.BTFeature
            public void HIDConnect(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_HIDConnect, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void HIDDisConnect() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_HIDDisConnect, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void HIDKeyboardInput(HIDKeyboardStatus hIDKeyboardStatus) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (hIDKeyboardStatus != null) {
                        obtain.writeInt(1);
                        hIDKeyboardStatus.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_HIDKeyboardInput, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void HIDMouseInput(HIDMouseStatus hIDMouseStatus) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (hIDMouseStatus != null) {
                        obtain.writeInt(1);
                        hIDMouseStatus.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_HIDMouseInput, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean SppConnect(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_SppConnect, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void SppDisConnect() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_SppDisConnect, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void SppSendData(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(Stub.TRANSACTION_SppSendData, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean answerCall() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.bt.BTFeature
            public void btReset() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_btReset, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean cancelCall() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public int changeSoundChannel() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void connectA2dp(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(Stub.TRANSACTION_connectA2dp, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean connectDevice_BC(BTDevice bTDevice) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bTDevice != null) {
                        obtain.writeInt(1);
                        bTDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean deletePairDeviceRecord(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean dial_BC(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean disConnectDevice_BC() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void disconnectA2dp() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_disconnectA2dp, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public int getAutoFeature() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean getCallLog_CB() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public int getCallState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getCallState, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public BTDevice getConnectDevice() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getConnectDevice, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0 ? BTDevice.CREATOR.createFromParcel(obtain2) : null;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean getContact_CB() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.bt.BTFeature
            public String getLocalDeviceAddress() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public int getLocalDeviceFeature() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public String getLocalDeviceName() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public int getMaxSpeakerVolume() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getMaxSpeakerVolume, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean getPairDeviceRecord_BC() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public String getPinCode() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public int getRemoteDeviceClass(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public String getRemoteDeviceName(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean getSMS_CB() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public int getSpeakerVolume() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getSpeakerVolume, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void installMhlSoft() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_installMhlSoft, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean isBTMusicPlaying() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean isBlueToothPowerOn() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean isBtMusicMute() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isBtMusicMute, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean isConnectA2DP() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isConnectA2DP, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean isConnectAVRCP() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isConnectAVRCP, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean isConnectDevice() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean isConnectHFP() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isConnectHFP, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean isHIDConnect() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isHIDConnect, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean isSppConnect() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_isSppConnect, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void loadContactFromPhone() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_loadContactFromPhone, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean playControl(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void queryA2dpId3Info() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_queryA2dpId3Info, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void registPIMCallback(BTPIMCallback bTPIMCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(bTPIMCallback != null ? bTPIMCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_registPIMCallback, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void registSppDataCallback(SppDataCallBack sppDataCallBack) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(sppDataCallBack != null ? sppDataCallBack.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_registSppDataCallback, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean sendDTMF(char c) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(c);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean setAutoFeature_BC(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void setBtMusicMute(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(Stub.TRANSACTION_setBtMusicMute, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean setDiscoverable(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean setLocalDeviceName(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void setMute(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(Stub.TRANSACTION_setMute, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean setPairable(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void setPinCode(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void setSpeakerVolume(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_setSpeakerVolume, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean startBlueTooth_BC() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean startSearchDevice_BC() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean stopBlueTooth_BC() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public boolean stopSearchDevice_BC() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt() != 0;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void unregistPIMCallback(BTPIMCallback bTPIMCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(bTPIMCallback != null ? bTPIMCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_unregistPIMCallback, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.bt.BTFeature
            public void unregistSppDataCallback(SppDataCallBack sppDataCallBack) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(sppDataCallBack != null ? sppDataCallBack.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_unregistSppDataCallback, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static BTFeature asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof BTFeature)) ? new Proxy(iBinder) : (BTFeature) queryLocalInterface;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean startBlueTooth_BC = startBlueTooth_BC();
                    parcel2.writeNoException();
                    parcel2.writeInt(startBlueTooth_BC ? 1 : 0);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean stopBlueTooth_BC = stopBlueTooth_BC();
                    parcel2.writeNoException();
                    parcel2.writeInt(stopBlueTooth_BC ? 1 : 0);
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isBlueToothPowerOn = isBlueToothPowerOn();
                    parcel2.writeNoException();
                    parcel2.writeInt(isBlueToothPowerOn ? 1 : 0);
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean connectDevice_BC = connectDevice_BC(parcel.readInt() != 0 ? BTDevice.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    parcel2.writeInt(connectDevice_BC ? 1 : 0);
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean disConnectDevice_BC = disConnectDevice_BC();
                    parcel2.writeNoException();
                    parcel2.writeInt(disConnectDevice_BC ? 1 : 0);
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isConnectDevice = isConnectDevice();
                    parcel2.writeNoException();
                    parcel2.writeInt(isConnectDevice ? 1 : 0);
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean startSearchDevice_BC = startSearchDevice_BC();
                    parcel2.writeNoException();
                    parcel2.writeInt(startSearchDevice_BC ? 1 : 0);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean stopSearchDevice_BC = stopSearchDevice_BC();
                    parcel2.writeNoException();
                    parcel2.writeInt(stopSearchDevice_BC ? 1 : 0);
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean localDeviceName = setLocalDeviceName(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(localDeviceName ? 1 : 0);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    String localDeviceName2 = getLocalDeviceName();
                    parcel2.writeNoException();
                    parcel2.writeString(localDeviceName2);
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    setPinCode(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    String pinCode = getPinCode();
                    parcel2.writeNoException();
                    parcel2.writeString(pinCode);
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean dial_BC = dial_BC(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(dial_BC ? 1 : 0);
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean answerCall = answerCall();
                    parcel2.writeNoException();
                    parcel2.writeInt(answerCall ? 1 : 0);
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean cancelCall = cancelCall();
                    parcel2.writeNoException();
                    parcel2.writeInt(cancelCall ? 1 : 0);
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    String remoteDeviceName = getRemoteDeviceName(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeString(remoteDeviceName);
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    int remoteDeviceClass = getRemoteDeviceClass(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(remoteDeviceClass);
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean discoverable = setDiscoverable(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(discoverable ? 1 : 0);
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean pairable = setPairable(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(pairable ? 1 : 0);
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    String localDeviceAddress = getLocalDeviceAddress();
                    parcel2.writeNoException();
                    parcel2.writeString(localDeviceAddress);
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    int localDeviceFeature = getLocalDeviceFeature();
                    parcel2.writeNoException();
                    parcel2.writeInt(localDeviceFeature);
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean sendDTMF = sendDTMF((char) parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(sendDTMF ? 1 : 0);
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean playControl = playControl(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(playControl ? 1 : 0);
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isBTMusicPlaying = isBTMusicPlaying();
                    parcel2.writeNoException();
                    parcel2.writeInt(isBTMusicPlaying ? 1 : 0);
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean contact_CB = getContact_CB();
                    parcel2.writeNoException();
                    parcel2.writeInt(contact_CB ? 1 : 0);
                    return true;
                case 26:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean callLog_CB = getCallLog_CB();
                    parcel2.writeNoException();
                    parcel2.writeInt(callLog_CB ? 1 : 0);
                    return true;
                case 27:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean sms_cb = getSMS_CB();
                    parcel2.writeNoException();
                    parcel2.writeInt(sms_cb ? 1 : 0);
                    return true;
                case 28:
                    parcel.enforceInterface(DESCRIPTOR);
                    int changeSoundChannel = changeSoundChannel();
                    parcel2.writeNoException();
                    parcel2.writeInt(changeSoundChannel);
                    return true;
                case 29:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean autoFeature_BC = setAutoFeature_BC(parcel.readInt(), parcel.readInt() != 0);
                    parcel2.writeNoException();
                    parcel2.writeInt(autoFeature_BC ? 1 : 0);
                    return true;
                case 30:
                    parcel.enforceInterface(DESCRIPTOR);
                    int autoFeature = getAutoFeature();
                    parcel2.writeNoException();
                    parcel2.writeInt(autoFeature);
                    return true;
                case 31:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean pairDeviceRecord_BC = getPairDeviceRecord_BC();
                    parcel2.writeNoException();
                    parcel2.writeInt(pairDeviceRecord_BC ? 1 : 0);
                    return true;
                case 32:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean deletePairDeviceRecord = deletePairDeviceRecord(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(deletePairDeviceRecord ? 1 : 0);
                    return true;
                case TRANSACTION_registPIMCallback /* 33 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    registPIMCallback(BTPIMCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_unregistPIMCallback /* 34 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregistPIMCallback(BTPIMCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_isConnectHFP /* 35 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isConnectHFP = isConnectHFP();
                    parcel2.writeNoException();
                    parcel2.writeInt(isConnectHFP ? 1 : 0);
                    return true;
                case TRANSACTION_isConnectA2DP /* 36 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isConnectA2DP = isConnectA2DP();
                    parcel2.writeNoException();
                    parcel2.writeInt(isConnectA2DP ? 1 : 0);
                    return true;
                case TRANSACTION_isConnectAVRCP /* 37 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isConnectAVRCP = isConnectAVRCP();
                    parcel2.writeNoException();
                    parcel2.writeInt(isConnectAVRCP ? 1 : 0);
                    return true;
                case TRANSACTION_getConnectDevice /* 38 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    BTDevice connectDevice = getConnectDevice();
                    parcel2.writeNoException();
                    if (connectDevice == null) {
                        parcel2.writeInt(0);
                        return true;
                    }
                    parcel2.writeInt(1);
                    connectDevice.writeToParcel(parcel2, 1);
                    return true;
                case TRANSACTION_setBtMusicMute /* 39 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    setBtMusicMute(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_registSppDataCallback /* 40 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    registSppDataCallback(SppDataCallBack.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_unregistSppDataCallback /* 41 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    unregistSppDataCallback(SppDataCallBack.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_HIDConnect /* 42 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    HIDConnect(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_HIDDisConnect /* 43 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    HIDDisConnect();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_isHIDConnect /* 44 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isHIDConnect = isHIDConnect();
                    parcel2.writeNoException();
                    parcel2.writeInt(isHIDConnect ? 1 : 0);
                    return true;
                case TRANSACTION_HIDKeyboardInput /* 45 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    HIDKeyboardInput(parcel.readInt() != 0 ? HIDKeyboardStatus.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_HIDMouseInput /* 46 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    HIDMouseInput(parcel.readInt() != 0 ? HIDMouseStatus.CREATOR.createFromParcel(parcel) : null);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_SppConnect /* 47 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean SppConnect = SppConnect(parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeInt(SppConnect ? 1 : 0);
                    return true;
                case TRANSACTION_SppDisConnect /* 48 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    SppDisConnect();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_installMhlSoft /* 49 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    installMhlSoft();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_isSppConnect /* 50 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isSppConnect = isSppConnect();
                    parcel2.writeNoException();
                    parcel2.writeInt(isSppConnect ? 1 : 0);
                    return true;
                case TRANSACTION_SppSendData /* 51 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    SppSendData(parcel.createByteArray());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_getCallState /* 52 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    int callState = getCallState();
                    parcel2.writeNoException();
                    parcel2.writeInt(callState);
                    return true;
                case TRANSACTION_connectA2dp /* 53 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    connectA2dp(parcel.readString());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_disconnectA2dp /* 54 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    disconnectA2dp();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_setSpeakerVolume /* 55 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    setSpeakerVolume(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_getMaxSpeakerVolume /* 56 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    int maxSpeakerVolume = getMaxSpeakerVolume();
                    parcel2.writeNoException();
                    parcel2.writeInt(maxSpeakerVolume);
                    return true;
                case TRANSACTION_getSpeakerVolume /* 57 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    int speakerVolume = getSpeakerVolume();
                    parcel2.writeNoException();
                    parcel2.writeInt(speakerVolume);
                    return true;
                case TRANSACTION_setMute /* 58 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    setMute(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_isBtMusicMute /* 59 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean isBtMusicMute = isBtMusicMute();
                    parcel2.writeNoException();
                    parcel2.writeInt(isBtMusicMute ? 1 : 0);
                    return true;
                case TRANSACTION_queryA2dpId3Info /* 60 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    queryA2dpId3Info();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_btReset /* 61 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    btReset();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_loadContactFromPhone /* 62 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    loadContactFromPhone();
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }
    }

    void HIDConnect(String str) throws RemoteException;

    void HIDDisConnect() throws RemoteException;

    void HIDKeyboardInput(HIDKeyboardStatus hIDKeyboardStatus) throws RemoteException;

    void HIDMouseInput(HIDMouseStatus hIDMouseStatus) throws RemoteException;

    boolean SppConnect(String str) throws RemoteException;

    void SppDisConnect() throws RemoteException;

    void SppSendData(byte[] bArr) throws RemoteException;

    boolean answerCall() throws RemoteException;

    void btReset() throws RemoteException;

    boolean cancelCall() throws RemoteException;

    int changeSoundChannel() throws RemoteException;

    void connectA2dp(String str) throws RemoteException;

    boolean connectDevice_BC(BTDevice bTDevice) throws RemoteException;

    boolean deletePairDeviceRecord(int i) throws RemoteException;

    boolean dial_BC(String str) throws RemoteException;

    boolean disConnectDevice_BC() throws RemoteException;

    void disconnectA2dp() throws RemoteException;

    int getAutoFeature() throws RemoteException;

    boolean getCallLog_CB() throws RemoteException;

    int getCallState() throws RemoteException;

    BTDevice getConnectDevice() throws RemoteException;

    boolean getContact_CB() throws RemoteException;

    String getLocalDeviceAddress() throws RemoteException;

    int getLocalDeviceFeature() throws RemoteException;

    String getLocalDeviceName() throws RemoteException;

    int getMaxSpeakerVolume() throws RemoteException;

    boolean getPairDeviceRecord_BC() throws RemoteException;

    String getPinCode() throws RemoteException;

    int getRemoteDeviceClass(String str) throws RemoteException;

    String getRemoteDeviceName(String str) throws RemoteException;

    boolean getSMS_CB() throws RemoteException;

    int getSpeakerVolume() throws RemoteException;

    void installMhlSoft() throws RemoteException;

    boolean isBTMusicPlaying() throws RemoteException;

    boolean isBlueToothPowerOn() throws RemoteException;

    boolean isBtMusicMute() throws RemoteException;

    boolean isConnectA2DP() throws RemoteException;

    boolean isConnectAVRCP() throws RemoteException;

    boolean isConnectDevice() throws RemoteException;

    boolean isConnectHFP() throws RemoteException;

    boolean isHIDConnect() throws RemoteException;

    boolean isSppConnect() throws RemoteException;

    void loadContactFromPhone() throws RemoteException;

    boolean playControl(int i) throws RemoteException;

    void queryA2dpId3Info() throws RemoteException;

    void registPIMCallback(BTPIMCallback bTPIMCallback) throws RemoteException;

    void registSppDataCallback(SppDataCallBack sppDataCallBack) throws RemoteException;

    boolean sendDTMF(char c) throws RemoteException;

    boolean setAutoFeature_BC(int i, boolean z) throws RemoteException;

    void setBtMusicMute(boolean z) throws RemoteException;

    boolean setDiscoverable(boolean z) throws RemoteException;

    boolean setLocalDeviceName(String str) throws RemoteException;

    void setMute(boolean z) throws RemoteException;

    boolean setPairable(boolean z) throws RemoteException;

    void setPinCode(String str) throws RemoteException;

    void setSpeakerVolume(int i) throws RemoteException;

    boolean startBlueTooth_BC() throws RemoteException;

    boolean startSearchDevice_BC() throws RemoteException;

    boolean stopBlueTooth_BC() throws RemoteException;

    boolean stopSearchDevice_BC() throws RemoteException;

    void unregistPIMCallback(BTPIMCallback bTPIMCallback) throws RemoteException;

    void unregistSppDataCallback(SppDataCallBack sppDataCallBack) throws RemoteException;
}
