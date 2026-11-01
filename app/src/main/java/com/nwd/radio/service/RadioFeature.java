package com.nwd.radio.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.nwd.radio.service.RadioCallback;
import com.nwd.radio.service.data.Frequency;
import com.nwd.radio.service.data.RadioPoint;

/* loaded from: classes.dex */
public interface RadioFeature extends IInterface {
    void AMS() throws RemoteException;

    void INTRO() throws RemoteException;

    void changeBand() throws RemoteException;

    Frequency getCurrentFrequency() throws RemoteException;

    byte getPTYType() throws RemoteException;

    Frequency[] getPrefabFrequency() throws RemoteException;

    byte getPrefabPTYType() throws RemoteException;

    boolean getRDSState(int i) throws RemoteException;

    RadioPoint[] getRadioPoint() throws RemoteException;

    byte getRadioState() throws RemoteException;

    String getRtMessage() throws RemoteException;

    boolean isHasStrero() throws RemoteException;

    boolean isNearOn() throws RemoteException;

    boolean isRadioBackServiceOn() throws RemoteException;

    boolean isStreroOn() throws RemoteException;

    void prefeb(boolean z) throws RemoteException;

    void registCallback(RadioCallback radioCallback) throws RemoteException;

    void saveCurrentFrequency(byte b) throws RemoteException;

    void search(boolean z) throws RemoteException;

    void seek(boolean z) throws RemoteException;

    void sendRadioCommand(byte b, byte b2) throws RemoteException;

    void setCurrentFrequency(int i, byte b, int i2) throws RemoteException;

    void setNearOn(boolean z) throws RemoteException;

    void setPTYType(byte b) throws RemoteException;

    void setRDSState(byte b, boolean z) throws RemoteException;

    void setRadioBackServiceOn(boolean z) throws RemoteException;

    void setStreroOn(boolean z) throws RemoteException;

    void unRegistCallback(RadioCallback radioCallback) throws RemoteException;

    public static abstract class Stub extends Binder implements RadioFeature {
        private static final String DESCRIPTOR = "com.nwd.radio.service.RadioFeature";
        static final int TRANSACTION_AMS = 6;
        static final int TRANSACTION_INTRO = 7;
        static final int TRANSACTION_changeBand = 5;
        static final int TRANSACTION_getCurrentFrequency = 2;
        static final int TRANSACTION_getPTYType = 18;
        static final int TRANSACTION_getPrefabFrequency = 21;
        static final int TRANSACTION_getPrefabPTYType = 19;
        static final int TRANSACTION_getRDSState = 16;
        static final int TRANSACTION_getRadioPoint = 22;
        static final int TRANSACTION_getRadioState = 23;
        static final int TRANSACTION_getRtMessage = 28;
        static final int TRANSACTION_isHasStrero = 10;
        static final int TRANSACTION_isNearOn = 9;
        static final int TRANSACTION_isRadioBackServiceOn = 14;
        static final int TRANSACTION_isStreroOn = 12;
        static final int TRANSACTION_prefeb = 26;
        static final int TRANSACTION_registCallback = 24;
        static final int TRANSACTION_saveCurrentFrequency = 20;
        static final int TRANSACTION_search = 4;
        static final int TRANSACTION_seek = 3;
        static final int TRANSACTION_sendRadioCommand = 27;
        static final int TRANSACTION_setCurrentFrequency = 1;
        static final int TRANSACTION_setNearOn = 8;
        static final int TRANSACTION_setPTYType = 17;
        static final int TRANSACTION_setRDSState = 15;
        static final int TRANSACTION_setRadioBackServiceOn = 13;
        static final int TRANSACTION_setStreroOn = 11;
        static final int TRANSACTION_unRegistCallback = 25;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static RadioFeature asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof RadioFeature)) {
                return (RadioFeature) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    byte _arg1 = data.readByte();
                    int _arg2 = data.readInt();
                    setCurrentFrequency(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    Frequency _result = getCurrentFrequency();
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg02 = data.readInt() != 0;
                    seek(_arg02);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg03 = data.readInt() != 0;
                    search(_arg03);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    changeBand();
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    AMS();
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    INTRO();
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg04 = data.readInt() != 0;
                    setNearOn(_arg04);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result2 = isNearOn();
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result3 = isHasStrero();
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg05 = data.readInt() != 0;
                    setStreroOn(_arg05);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result4 = isStreroOn();
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg06 = data.readInt() != 0;
                    setRadioBackServiceOn(_arg06);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result5 = isRadioBackServiceOn();
                    reply.writeNoException();
                    reply.writeInt(_result5 ? 1 : 0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg07 = data.readByte();
                    boolean _arg12 = data.readInt() != 0;
                    setRDSState(_arg07, _arg12);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    boolean _result6 = getRDSState(_arg08);
                    reply.writeNoException();
                    reply.writeInt(_result6 ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg09 = data.readByte();
                    setPTYType(_arg09);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    byte _result7 = getPTYType();
                    reply.writeNoException();
                    reply.writeByte(_result7);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    byte _result8 = getPrefabPTYType();
                    reply.writeNoException();
                    reply.writeByte(_result8);
                    return true;
                case TRANSACTION_saveCurrentFrequency /* 20 */:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg010 = data.readByte();
                    saveCurrentFrequency(_arg010);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_getPrefabFrequency /* 21 */:
                    data.enforceInterface(DESCRIPTOR);
                    Frequency[] _result9 = getPrefabFrequency();
                    reply.writeNoException();
                    reply.writeTypedArray(_result9, 1);
                    return true;
                case TRANSACTION_getRadioPoint /* 22 */:
                    data.enforceInterface(DESCRIPTOR);
                    RadioPoint[] _result10 = getRadioPoint();
                    reply.writeNoException();
                    reply.writeTypedArray(_result10, 1);
                    return true;
                case TRANSACTION_getRadioState /* 23 */:
                    data.enforceInterface(DESCRIPTOR);
                    byte _result11 = getRadioState();
                    reply.writeNoException();
                    reply.writeByte(_result11);
                    return true;
                case TRANSACTION_registCallback /* 24 */:
                    data.enforceInterface(DESCRIPTOR);
                    RadioCallback _arg011 = RadioCallback.Stub.asInterface(data.readStrongBinder());
                    registCallback(_arg011);
                    reply.writeNoException();
                    return true;
                case TRANSACTION_unRegistCallback /* 25 */:
                    data.enforceInterface(DESCRIPTOR);
                    RadioCallback _arg012 = RadioCallback.Stub.asInterface(data.readStrongBinder());
                    unRegistCallback(_arg012);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg013 = data.readInt() != 0;
                    prefeb(_arg013);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    byte _arg014 = data.readByte();
                    byte _arg13 = data.readByte();
                    sendRadioCommand(_arg014, _arg13);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    String _result12 = getRtMessage();
                    reply.writeNoException();
                    reply.writeString(_result12);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements RadioFeature {
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

            @Override // com.nwd.radio.service.RadioFeature
            public void setCurrentFrequency(int aFrequency, byte aBandType, int prefebIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(aFrequency);
                    _data.writeByte(aBandType);
                    _data.writeInt(prefebIndex);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public Frequency getCurrentFrequency() throws RemoteException {
                Frequency _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Frequency.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void seek(boolean isIncrease) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isIncrease ? 1 : 0);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void search(boolean isIncrease) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isIncrease ? 1 : 0);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void changeBand() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void AMS() throws RemoteException {
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

            @Override // com.nwd.radio.service.RadioFeature
            public void INTRO() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void setNearOn(boolean isOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isOn ? 1 : 0);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public boolean isNearOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public boolean isHasStrero() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void setStreroOn(boolean isOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isOn ? 1 : 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public boolean isStreroOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void setRadioBackServiceOn(boolean isOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isOn ? 1 : 0);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public boolean isRadioBackServiceOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void setRDSState(byte aRDSType, boolean isOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(aRDSType);
                    _data.writeInt(isOn ? 1 : 0);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public boolean getRDSState(int aRDSType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(aRDSType);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void setPTYType(byte aType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(aType);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public byte getPTYType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    byte _result = _reply.readByte();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public byte getPrefabPTYType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    byte _result = _reply.readByte();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void saveCurrentFrequency(byte aIndex) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(aIndex);
                    this.mRemote.transact(Stub.TRANSACTION_saveCurrentFrequency, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public Frequency[] getPrefabFrequency() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getPrefabFrequency, _data, _reply, 0);
                    _reply.readException();
                    Frequency[] _result = (Frequency[]) _reply.createTypedArray(Frequency.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public RadioPoint[] getRadioPoint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getRadioPoint, _data, _reply, 0);
                    _reply.readException();
                    RadioPoint[] _result = (RadioPoint[]) _reply.createTypedArray(RadioPoint.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public byte getRadioState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getRadioState, _data, _reply, 0);
                    _reply.readException();
                    byte _result = _reply.readByte();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void registCallback(RadioCallback aCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(aCallback != null ? aCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_registCallback, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void unRegistCallback(RadioCallback aCallback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(aCallback != null ? aCallback.asBinder() : null);
                    this.mRemote.transact(Stub.TRANSACTION_unRegistCallback, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void prefeb(boolean isNext) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isNext ? 1 : 0);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public void sendRadioCommand(byte data0, byte data1) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByte(data0);
                    _data.writeByte(data1);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nwd.radio.service.RadioFeature
            public String getRtMessage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
