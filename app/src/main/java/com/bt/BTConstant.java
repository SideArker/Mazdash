package com.bt;

/* loaded from: classes.dex */
public class BTConstant {
    public static final String ACTION_A2DP_MUTE = "com.bt.ACTION_A2DP_MUTE";
    public static final String ACTION_A2DP_ONLYMUTE = "com.bt.ACTION_A2DP_ONLYMUTE";
    public static final String ACTION_A2DP_UNMUTE = "com.bt.ACTION_A2DP_UNMUTE";
    public static final String ACTION_BT_NOT_ALIVE = "com.bt.ACTION_BT_NOT_ALIVE";
    public static final String ACTION_SERVICE = "com.bt.ACTION_START_SERVICE";
    public static final int SOUND_CHANNEL_CAR = 0;
    public static final int SOUND_CHANNEL_PHONE = 1;

    public interface AutoFeature {
        public static final int FEATURE_AUTO_ACCEPT_CALL = 1;
        public static final int FEATURE_AUTO_CONNECT_DEVICE = 2;
    }

    public interface CallLog {
        public static final int TYPE_INCOMING_CALL = 0;
        public static final int TYPE_MISSING_CALL = 2;
        public static final int TYPE_OUTGOING_CALL = 1;
    }

    public interface CallState {
        public static final int CALL_IN = 1;
        public static final int CALL_ONLINE = 3;
        public static final int CALL_OUT = 2;
        public static final int END_CALL = 0;
    }

    public interface DeviceClass {
        public static final int AUDIO = 2;
        public static final int OBD = 3;
        public static final int PHONE = 1;
        public static final int UNKNOWN = 0;
    }

    public interface DeviceFeature {
        public static final int INITIATIVE = 2;
        public static final int PASSIVITY = 1;
    }

    public interface DeviceService {
        public static final int AUDIO_SINK = 2;
        public static final int AUDIO_SOURCE = 1;
        public static final int AVRCP_CT = 4;
        public static final int AVRCP_TG = 3;
        public static final int HANDFREE = 5;
        public static final int UNKNOWN = 0;
    }

    public interface MusicCommand {
        public static final int NEXT = 2;
        public static final int PAUSE = 1;
        public static final int PLAY = 0;
        public static final int PREVIOUS = 3;
    }
}
