package com.nwd.setting.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.nwd.setting.service.AudioSettingCallback;
import com.nwd.setting.service.CanCallback;
import com.nwd.setting.service.SettingCallback;

/* loaded from: classes.dex */
public interface SettingFeature extends IInterface {

    public static abstract class Stub extends Binder implements SettingFeature {
        private static final String DESCRIPTOR = "com.nwd.setting.service.SettingFeature";
        static final int TRANSACTION_getAudioParam = 7;
        static final int TRANSACTION_getDspAudio = 32;
        static final int TRANSACTION_getGSensorSensitivity = 36;
        static final int TRANSACTION_getGainParam = 9;
        static final int TRANSACTION_getSteerWheelLearningState = 26;
        static final int TRANSACTION_getTriColor = 35;
        static final int TRANSACTION_registAudioCallback = 24;
        static final int TRANSACTION_registCallback = 22;
        static final int TRANSACTION_registCanCallback = 29;
        static final int TRANSACTION_requestOSFactoryReset = 28;
        static final int TRANSACTION_responseWatchDog = 2;
        static final int TRANSACTION_setATVStandard = 15;
        static final int TRANSACTION_setAmplifierSwitch = 33;
        static final int TRANSACTION_setAudioParam = 6;
        static final int TRANSACTION_setAutoWakeup = 38;
        static final int TRANSACTION_setBackLightSwitch = 19;
        static final int TRANSACTION_setBackStageManageFunction = 4;
        static final int TRANSACTION_setDayBackLight = 18;
        static final int TRANSACTION_setDefaultVolume = 13;
        static final int TRANSACTION_setDspAudio = 31;
        static final int TRANSACTION_setGPSPromptVolumeWay = 14;
        static final int TRANSACTION_setGSensorSensitivity = 37;
        static final int TRANSACTION_setGainParam = 8;
        static final int TRANSACTION_setLanguageIndex = 3;
        static final int TRANSACTION_setMixedRatio = 17;
        static final int TRANSACTION_setMute = 10;
        static final int TRANSACTION_setRadarSwitcher = 27;
        static final int TRANSACTION_setRadioRegion = 16;
        static final int TRANSACTION_setSmartFanSwitch = 34;
        static final int TRANSACTION_setSystemFeature = 11;
        static final int TRANSACTION_setTirePressureFunctionManage = 5;
        static final int TRANSACTION_setTriColor = 12;
        static final int TRANSACTION_setVideoMode = 21;
        static final int TRANSACTION_setVideoParam = 20;
        static final int TRANSACTION_setWatchDogSwtich = 1;
        static final int TRANSACTION_unRegistAudioCallback = 25;
        static final int TRANSACTION_unRegistCallback = 23;
        static final int TRANSACTION_unRegistCanCallback = 30;

        private static class Proxy implements SettingFeature {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.nwd.setting.service.SettingFeature
            public int getAudioParam(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public int getDspAudio(byte b) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByte(b);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public int getGSensorSensitivity() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getGSensorSensitivity, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public int getGainParam(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.nwd.setting.service.SettingFeature
            public boolean[] getSteerWheelLearningState() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createBooleanArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public int[] getTriColor() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_getTriColor, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createIntArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void registAudioCallback(AudioSettingCallback audioSettingCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(audioSettingCallback != null ? audioSettingCallback.asBinder() : null);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void registCallback(SettingCallback settingCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(settingCallback != null ? settingCallback.asBinder() : null);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void registCanCallback(CanCallback canCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(canCallback != null ? canCallback.asBinder() : null);
                    this.mRemote.transact(29, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void requestOSFactoryReset(byte b) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByte(b);
                    this.mRemote.transact(28, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void responseWatchDog(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setATVStandard(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setAmplifierSwitch(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(Stub.TRANSACTION_setAmplifierSwitch, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setAudioParam(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setAutoWakeup(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(Stub.TRANSACTION_setAutoWakeup, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setBackLightSwitch(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setBackStageManageFunction(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setDayBackLight(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setDefaultVolume(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setDspAudio(byte b, byte b2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByte(b);
                    obtain.writeByte(b2);
                    this.mRemote.transact(31, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setGPSPromptVolumeWay(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setGSensorSensitivity(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_setGSensorSensitivity, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setGainParam(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setLanguageIndex(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setMixedRatio(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setMute(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setRadarSwitcher(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setRadioRegion(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setSmartFanSwitch(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(Stub.TRANSACTION_setSmartFanSwitch, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setSystemFeature(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setTirePressureFunctionManage(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setTriColor(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setVideoMode(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setVideoParam(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void setWatchDogSwtich(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void unRegistAudioCallback(AudioSettingCallback audioSettingCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(audioSettingCallback != null ? audioSettingCallback.asBinder() : null);
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void unRegistCallback(SettingCallback settingCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(settingCallback != null ? settingCallback.asBinder() : null);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nwd.setting.service.SettingFeature
            public void unRegistCanCallback(CanCallback canCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(canCallback != null ? canCallback.asBinder() : null);
                    this.mRemote.transact(30, obtain, obtain2, 0);
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

        public static SettingFeature asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof SettingFeature)) ? new Proxy(iBinder) : (SettingFeature) queryLocalInterface;
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
                    setWatchDogSwtich(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    responseWatchDog(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    setLanguageIndex(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 4:
                    parcel.enforceInterface(DESCRIPTOR);
                    setBackStageManageFunction(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 5:
                    parcel.enforceInterface(DESCRIPTOR);
                    setTirePressureFunctionManage(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 6:
                    parcel.enforceInterface(DESCRIPTOR);
                    setAudioParam(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 7:
                    parcel.enforceInterface(DESCRIPTOR);
                    int audioParam = getAudioParam(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(audioParam);
                    return true;
                case 8:
                    parcel.enforceInterface(DESCRIPTOR);
                    setGainParam(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    int gainParam = getGainParam(parcel.readInt());
                    parcel2.writeNoException();
                    parcel2.writeInt(gainParam);
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    setMute(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 11:
                    parcel.enforceInterface(DESCRIPTOR);
                    setSystemFeature(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 12:
                    parcel.enforceInterface(DESCRIPTOR);
                    setTriColor(parcel.readInt(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 13:
                    parcel.enforceInterface(DESCRIPTOR);
                    setDefaultVolume(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 14:
                    parcel.enforceInterface(DESCRIPTOR);
                    setGPSPromptVolumeWay(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 15:
                    parcel.enforceInterface(DESCRIPTOR);
                    setATVStandard(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 16:
                    parcel.enforceInterface(DESCRIPTOR);
                    setRadioRegion(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 17:
                    parcel.enforceInterface(DESCRIPTOR);
                    setMixedRatio(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 18:
                    parcel.enforceInterface(DESCRIPTOR);
                    setDayBackLight(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 19:
                    parcel.enforceInterface(DESCRIPTOR);
                    setBackLightSwitch(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case 20:
                    parcel.enforceInterface(DESCRIPTOR);
                    setVideoParam(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 21:
                    parcel.enforceInterface(DESCRIPTOR);
                    setVideoMode(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 22:
                    parcel.enforceInterface(DESCRIPTOR);
                    registCallback(SettingCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 23:
                    parcel.enforceInterface(DESCRIPTOR);
                    unRegistCallback(SettingCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 24:
                    parcel.enforceInterface(DESCRIPTOR);
                    registAudioCallback(AudioSettingCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 25:
                    parcel.enforceInterface(DESCRIPTOR);
                    unRegistAudioCallback(AudioSettingCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 26:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean[] steerWheelLearningState = getSteerWheelLearningState();
                    parcel2.writeNoException();
                    parcel2.writeBooleanArray(steerWheelLearningState);
                    return true;
                case 27:
                    parcel.enforceInterface(DESCRIPTOR);
                    setRadarSwitcher(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 28:
                    parcel.enforceInterface(DESCRIPTOR);
                    requestOSFactoryReset(parcel.readByte());
                    parcel2.writeNoException();
                    return true;
                case 29:
                    parcel.enforceInterface(DESCRIPTOR);
                    registCanCallback(CanCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 30:
                    parcel.enforceInterface(DESCRIPTOR);
                    unRegistCanCallback(CanCallback.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 31:
                    parcel.enforceInterface(DESCRIPTOR);
                    setDspAudio(parcel.readByte(), parcel.readByte());
                    parcel2.writeNoException();
                    return true;
                case 32:
                    parcel.enforceInterface(DESCRIPTOR);
                    int dspAudio = getDspAudio(parcel.readByte());
                    parcel2.writeNoException();
                    parcel2.writeInt(dspAudio);
                    return true;
                case TRANSACTION_setAmplifierSwitch /* 33 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    setAmplifierSwitch(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_setSmartFanSwitch /* 34 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    setSmartFanSwitch(parcel.readInt() != 0);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_getTriColor /* 35 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    int[] triColor = getTriColor();
                    parcel2.writeNoException();
                    parcel2.writeIntArray(triColor);
                    return true;
                case TRANSACTION_getGSensorSensitivity /* 36 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    int gSensorSensitivity = getGSensorSensitivity();
                    parcel2.writeNoException();
                    parcel2.writeInt(gSensorSensitivity);
                    return true;
                case TRANSACTION_setGSensorSensitivity /* 37 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    setGSensorSensitivity(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_setAutoWakeup /* 38 */:
                    parcel.enforceInterface(DESCRIPTOR);
                    setAutoWakeup(parcel.readInt() != 0);
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

    int getAudioParam(int i) throws RemoteException;

    int getDspAudio(byte b) throws RemoteException;

    int getGSensorSensitivity() throws RemoteException;

    int getGainParam(int i) throws RemoteException;

    boolean[] getSteerWheelLearningState() throws RemoteException;

    int[] getTriColor() throws RemoteException;

    void registAudioCallback(AudioSettingCallback audioSettingCallback) throws RemoteException;

    void registCallback(SettingCallback settingCallback) throws RemoteException;

    void registCanCallback(CanCallback canCallback) throws RemoteException;

    void requestOSFactoryReset(byte b) throws RemoteException;

    void responseWatchDog(int i) throws RemoteException;

    void setATVStandard(int i) throws RemoteException;

    void setAmplifierSwitch(boolean z) throws RemoteException;

    void setAudioParam(int i, int i2) throws RemoteException;

    void setAutoWakeup(boolean z) throws RemoteException;

    void setBackLightSwitch(boolean z) throws RemoteException;

    void setBackStageManageFunction(int i) throws RemoteException;

    void setDayBackLight(int i) throws RemoteException;

    void setDefaultVolume(int i) throws RemoteException;

    void setDspAudio(byte b, byte b2) throws RemoteException;

    void setGPSPromptVolumeWay(int i) throws RemoteException;

    void setGSensorSensitivity(int i) throws RemoteException;

    void setGainParam(int i, int i2) throws RemoteException;

    void setLanguageIndex(int i) throws RemoteException;

    void setMixedRatio(int i) throws RemoteException;

    void setMute(boolean z) throws RemoteException;

    void setRadarSwitcher(int i) throws RemoteException;

    void setRadioRegion(int i) throws RemoteException;

    void setSmartFanSwitch(boolean z) throws RemoteException;

    void setSystemFeature(int i, int i2) throws RemoteException;

    void setTirePressureFunctionManage(int i) throws RemoteException;

    void setTriColor(int i, int i2, int i3) throws RemoteException;

    void setVideoMode(int i) throws RemoteException;

    void setVideoParam(int i, int i2) throws RemoteException;

    void setWatchDogSwtich(boolean z) throws RemoteException;

    void unRegistAudioCallback(AudioSettingCallback audioSettingCallback) throws RemoteException;

    void unRegistCallback(SettingCallback settingCallback) throws RemoteException;

    void unRegistCanCallback(CanCallback canCallback) throws RemoteException;
}
