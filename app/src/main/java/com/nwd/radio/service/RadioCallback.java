package com.nwd.radio.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.nwd.radio.service.data.Frequency;
import com.nwd.radio.service.data.RadioPoint;

/* loaded from: classes.dex */
public interface RadioCallback extends IInterface {
    void notifyCurrentFrequency(byte b, int i, String str, int i2) throws RemoteException;

    void notifyCurrentIsTA(boolean z) throws RemoteException;

    void notifyCurrentPTYType(byte b) throws RemoteException;

    void notifyNearOn(boolean z) throws RemoteException;

    void notifyPrefabFrequency(Frequency[] frequencyArr) throws RemoteException;

    void notifyPrefabPTYType(byte b) throws RemoteException;

    void notifyRDSStateChange() throws RemoteException;

    void notifyRadioPoint(RadioPoint[] radioPointArr) throws RemoteException;

    void notifyRdsShowState(boolean z) throws RemoteException;

    void notifyRtMessage(String str) throws RemoteException;

    void notifyState(byte b) throws RemoteException;

    void notifyStereo(boolean z) throws RemoteException;

    void notifyStereoOn(boolean z) throws RemoteException;

    public static abstract class Stub extends Binder implements RadioCallback {
        private static final String DESCRIPTOR = "com.nwd.radio.service.RadioCallback";
        static final int TRANSACTION_notifyCurrentFrequency = 2;
        static final int TRANSACTION_notifyCurrentIsTA = 11;
        static final int TRANSACTION_notifyCurrentPTYType = 7;
        static final int TRANSACTION_notifyNearOn = 3;
        static final int TRANSACTION_notifyPrefabFrequency = 8;
        static final int TRANSACTION_notifyPrefabPTYType = 9;
        static final int TRANSACTION_notifyRDSStateChange = 6;
        static final int TRANSACTION_notifyRadioPoint = 10;
        static final int TRANSACTION_notifyRdsShowState = 12;
        static final int TRANSACTION_notifyRtMessage = 13;
        static final int TRANSACTION_notifyState = 1;
        static final int TRANSACTION_notifyStereo = 4;
        static final int TRANSACTION_notifyStereoOn = 5;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static RadioCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof RadioCallback)) {
                return (RadioCallback) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    notifyState(data.readByte());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg02 = data.readByte();
                    int _arg1 = data.readInt();
                    String _arg2 = data.readString();
                    int _arg3 = data.readInt();
                    notifyCurrentFrequency(_arg02, _arg1, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    notifyNearOn(_arg0);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    notifyStereo(_arg0);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    notifyStereoOn(_arg0);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    notifyRDSStateChange();
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    notifyCurrentPTYType(data.readByte());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    notifyPrefabFrequency((Frequency[]) data.createTypedArray(Frequency.CREATOR));
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    notifyPrefabPTYType(data.readByte());
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    notifyRadioPoint((RadioPoint[]) data.createTypedArray(RadioPoint.CREATOR));
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    notifyCurrentIsTA(_arg0);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    notifyRdsShowState(_arg0);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    notifyRtMessage(data.readString());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements RadioCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyState(byte aState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(aState);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyCurrentFrequency(byte aBandType, int aFrequency, String aPSName, int isPrefabFrequency) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(aBandType);
                    _data.writeInt(aFrequency);
                    _data.writeString(aPSName);
                    _data.writeInt(isPrefabFrequency);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyNearOn(boolean isOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isOn ? 1 : 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyStereo(boolean isOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isOn ? 1 : 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyStereoOn(boolean isOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isOn ? 1 : 0);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyRDSStateChange() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyCurrentPTYType(byte aPTYType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(aPTYType);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyPrefabFrequency(Frequency[] aFrequencyPrefab) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(aFrequencyPrefab, 0);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyPrefabPTYType(byte aPTYType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(aPTYType);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyRadioPoint(RadioPoint[] aRadioPoint) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(aRadioPoint, 0);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyCurrentIsTA(boolean aIsTA) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(aIsTA ? 1 : 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyRdsShowState(boolean isNeedShowRds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isNeedShowRds ? 1 : 0);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioCallback
            public void notifyRtMessage(String rtMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rtMessage);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
