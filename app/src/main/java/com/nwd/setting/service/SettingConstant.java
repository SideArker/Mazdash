package com.nwd.setting.service;

/* loaded from: classes.dex */
public class SettingConstant {
    public static final String ACTION_AUX_OPEN_STATE = "com.nwd.action.ACTION_AUX_OPEN_STATE";
    public static final String ACTION_CANCEL_MCU_UPDATE = "com.nwd.ACTION_CANCEL_MCU_UPDATE";
    public static final String ACTION_EXIT_MCU_UPDATE = "com.nwd.ACTION_EXIT_MCU_UPDATE";
    public static final String ACTION_FIXED_GAIN_SWITCH = "com.nwd.ACTION_FIXED_GAIN_SWITCH";
    public static final String ACTION_FORCED_CALIBRATION_RESOLUTION = "com.nwd.action.ACTION_FORCED_CALIBRATION_RESOLUTION";
    public static final String ACTION_IMPORT_RESOLUTION_CONFIG = "com.nwd.action.ACTION_IMPORT_RESOLUTION_CONFIG";
    public static final String ACTION_MCU_UPDATE_PROGRESS = "com.nwd.ACTION_MCU_UPDATE_PROGRESS";
    public static final String ACTION_MCU_UPDATE_STATE_CHANGE = "com.nwd.ACTION_MCU_UPDATE_STATE_CHANGE";
    public static final String ACTION_REQUEST_CAN_UPDATE = "com.nwd.action.ACTION_REQUEST_CAN_UPDATE";
    public static final String ACTION_REQUEST_SHORT_MUTE = "com.nwd.ACTION_REQUEST_SHORT_MUTE";
    public static final String ACTION_SETTING_SERVICE = "com.nwd.setting.service.ACTION_SETTING_SERVICE";
    public static final String EXTRA_AUX_OPEN_STATE = "extra_aux_open_state";
    public static final String EXTRA_FIXED_GAIN_STATE = "extra_fixed_gain_state";
    public static final String EXTRA_FIXED_GAIN_VALUE = "extra_fixed_gain_value";
    public static final String EXTRA_MCU_UPDATE_PATH = "extra_mcu_update_path";
    public static final String EXTRA_MCU_UPDATE_PROGRESS = "extra_mcu_update_progress";
    public static final String EXTRA_MCU_UPDATE_STATE = "extra_mcu_update_state";
    public static final String EXTRA_RESOLUTION_MODE = "extra_resolution_mode";
    public static final String EXTRA_SHORT_MUTE_TIME = "extra_short_mute_time";
    public static final String KEY_GSENSOR_SENSITIVITY = "key_gsensor_sensitivity";
    public static final String KEY_IMPORT_RESOLUTION_CONFIG = "key_import_resolution_config";

    public interface AudioType {
        public static final byte AUDIO_PARA_5_1 = 4;
        public static final byte AUDIO_PARA_BALANCE = 13;
        public static final byte AUDIO_PARA_BASS = 2;
        public static final byte AUDIO_PARA_BBE = 3;
        public static final byte AUDIO_PARA_BT_VOLUME = 16;
        public static final byte AUDIO_PARA_CENTER = 6;
        public static final byte AUDIO_PARA_DSP_CENTER = 21;
        public static final byte AUDIO_PARA_DSP_DEFINITION = 20;
        public static final byte AUDIO_PARA_DSP_FOCUS = 22;
        public static final byte AUDIO_PARA_DSP_SPACE = 23;
        public static final byte AUDIO_PARA_DSP_SPEAKER = 19;
        public static final byte AUDIO_PARA_DSP_SRS = 24;
        public static final byte AUDIO_PARA_DSP_SRS_RESET = 25;
        public static final byte AUDIO_PARA_DSP_TRUBASS = 18;
        public static final byte AUDIO_PARA_EQ = 5;
        public static final byte AUDIO_PARA_EQUAL = 17;
        public static final byte AUDIO_PARA_FADER = 12;
        public static final byte AUDIO_PARA_FRONT_LEFT = 8;
        public static final byte AUDIO_PARA_FRONT_RIGHT = 9;
        public static final byte AUDIO_PARA_GPS_VOLUME = 15;
        public static final byte AUDIO_PARA_HEAVY_BASS = 7;
        public static final byte AUDIO_PARA_REAR_LEFT = 10;
        public static final byte AUDIO_PARA_REAR_RIGHT = 11;
        public static final byte AUDIO_PARA_SYSTEM_VOLUME = 14;
        public static final byte AUDIO_PARA_TENOR = 1;
        public static final byte AUDIO_PARA_TREBLE = 0;
        public static final byte EQ_CLASSES = 4;
        public static final byte EQ_CUSTOM = 0;
        public static final byte EQ_DANCE = 6;
        public static final byte EQ_HALL = 8;
        public static final byte EQ_JAZZ = 5;
        public static final byte EQ_MOVIE = 7;
        public static final byte EQ_POP = 2;
        public static final byte EQ_ROCK = 3;
        public static final byte EQ_STANDARY = 1;
        public static final byte GAIN_PARA_AUX = 6;
        public static final byte GAIN_PARA_AUX2 = 10;
        public static final byte GAIN_PARA_BT = 2;
        public static final byte GAIN_PARA_CANBUS_USB = 11;
        public static final byte GAIN_PARA_CDC = 8;
        public static final byte GAIN_PARA_DVD = 3;
        public static final byte GAIN_PARA_GPS = 1;
        public static final byte GAIN_PARA_IPOD = 5;
        public static final byte GAIN_PARA_MP5 = 9;
        public static final byte GAIN_PARA_RADIO = 7;
        public static final byte GAIN_PARA_TV = 4;
        public static final int STATE_EQUAL = 2;
    }

    public interface AutoWakeup {
        public static final String ACTION_AUTO_WAKEUP_CHANGE = "com.nwd.ACTION_AUTO_WAKEUP_CHANGE";
        public static final String PROPERTIES_AUTO_WAKEUP = "persist.nwd.autowakeup";
        public static final String VALUE_AUTO_WAKEUP_DISABLE = "disable";
        public static final String VALUE_AUTO_WAKEUP_ENABLE = "enable";
        public static final String VALUE_AUTO_WAKEUP_NONE = "none";
    }

    public interface Canbus {
        public static final byte CAN_ENTER_UPDATE = 3;
        public static final byte CAN_UPDATE_FAILD = 2;
        public static final byte CAN_UPDATE_SUCCESS = 1;
    }

    @Deprecated
    public interface Canbus_Door {
        public static final byte LEFT_BACK_DOOR = 32;
        public static final byte LEFT_FRONT_DOOR = Byte.MIN_VALUE;
        public static final byte RIGHT_BACK_DOOR = 16;
        public static final byte RIGHT_FRONT_DOOR = 64;
        public static final byte TAIL_DOOR = 8;
    }

    public interface DisplayDriverUpdateState {
        public static final byte STATE_ENTER_UPDATE = 3;
        public static final byte STATE_UPDATE_FAILD = 2;
        public static final byte STATE_UPDATE_SUCCESS = 1;
    }

    public interface DspAudioType {
        public static final int DC_BASS_FREQ = 5;
        public static final int DC_BASS_GAIN = 6;
        public static final int DC_HIGH_FREQ_EXTENSION = 10;
        public static final int DC_LOUDNESS_GAIN = 28;
        public static final int DC_LOUDNESS_SWITCH = 27;
        public static final int DC_LOW_FREQ_COMPRESSION = 9;
        public static final int DC_RESET = 22;
        public static final int DC_SWITCH = 21;
        public static final int DC_TREBLE_FREQ = 7;
        public static final int DC_TREBLE_GAIN = 8;
        public static final int DC_TREBLE_STRENGTHEN_GAIN = 26;
        public static final int DC_TREBLE_STRENGTHEN_SWITCH = 25;
        public static final int EQ_11 = 30;
        public static final int EQ_12 = 31;
        public static final int EQ_125HZ = 13;
        public static final int EQ_13 = 32;
        public static final int EQ_16KHZ = 20;
        public static final int EQ_1KHZ = 16;
        public static final int EQ_250HZ = 14;
        public static final int EQ_2KHZ = 17;
        public static final int EQ_32HZ = 11;
        public static final int EQ_4KHZ = 18;
        public static final int EQ_500HZ = 15;
        public static final int EQ_64HZ = 12;
        public static final int EQ_8KHZ = 19;
        public static final int HB_CUTOFF_FREQ = 3;
        public static final int HB_GAIN = 2;
        public static final int HB_PHASE = 1;
        public static final int HB_RESET = 24;
        public static final int HB_SWITCH = 23;
        public static final int SURROUND_3D = 4;
    }

    public interface FactoryResetType {
        public static final byte TYPE_FACTORY_RESET = 0;
        public static final byte TYPE_SYSTEM_REBOOT = 2;
        public static final byte TYPE_SYSTEM_UPDATE = 1;
    }

    public interface McuUpdateState {
        public static final byte CMD_ENTER_UPDATE = 2;
        public static final byte CMD_HAVE_UPDATE = 3;
        public static final byte CMD_HAVE_UPDATE_VERIFY = 1;
        public static final byte ERROR_TYPE_CLIENT = 2;
        public static final byte ERROR_TYPE_MACHINE = 4;
        public static final byte ERROR_TYPE_MCU = 1;
        public static final byte KEY_CANCEL = 2;
        public static final byte KEY_CONFIRM = 1;
        public static final byte KEY_EXIT = 3;
        public static final byte STATE_ENTER_UPDATE = 2;
        public static final byte STATE_REQUEST_ENTER_UPDATE = 1;
        public static final byte STATE_REQUEST_INSERT_SDCARD = 4;
        public static final byte STATE_UPDATE_ERROR = 16;
        public static final byte STATE_UPDATE_FINISH = 3;
    }

    public interface MhlUpdateState {
        public static final byte STATE_ENTER_UPDATE = 3;
        public static final byte STATE_UPDATE_FAILD = 2;
        public static final byte STATE_UPDATE_SUCCESS = 1;
    }

    public interface Radar {
        public static final byte RADAR_WORK_STATE = 1;
    }

    public interface SettingFunctionType {
        public static final byte BACKCAR_MIRROR = 50;
        public static final byte BACKCAR_VOLUME = 45;
        public static final byte CANBUS_BITERATE = 43;
        public static final byte SEND_FM = 44;
        public static final byte STREEWHEEL_BEGIN_STUDY = 4;
        public static final byte STREEWHEEL_END_STUDY = 5;
        public static final byte STREEWHEEL_KEY_A2DP = 58;
        public static final byte STREEWHEEL_KEY_AUDIO = 28;
        public static final byte STREEWHEEL_KEY_AUX = 54;
        public static final byte STREEWHEEL_KEY_BACK = 47;
        public static final byte STREEWHEEL_KEY_BT = 40;
        public static final byte STREEWHEEL_KEY_CALL = 14;
        public static final byte STREEWHEEL_KEY_DVD = 27;
        public static final byte STREEWHEEL_KEY_ECAR = 56;
        public static final byte STREEWHEEL_KEY_ENABLE = 16;
        public static final byte STREEWHEEL_KEY_ENTER = 55;
        public static final byte STREEWHEEL_KEY_HANDUP = 15;
        public static final byte STREEWHEEL_KEY_HOME = 46;
        public static final byte STREEWHEEL_KEY_MENU = 54;
        public static final byte STREEWHEEL_KEY_MODE = 12;
        public static final byte STREEWHEEL_KEY_MUSIC = 36;
        public static final byte STREEWHEEL_KEY_MUTE = 8;
        public static final byte STREEWHEEL_KEY_NAVI = 24;
        public static final byte STREEWHEEL_KEY_NEXT = 10;
        public static final byte STREEWHEEL_KEY_NEXT_OR_CANCEL_CALL = 53;
        public static final byte STREEWHEEL_KEY_PLAY = 11;
        public static final byte STREEWHEEL_KEY_POWER = 13;
        public static final byte STREEWHEEL_KEY_PREVIEW_OR_ACCEPT_CALL = 52;
        public static final byte STREEWHEEL_KEY_PREVIOUS = 9;
        public static final byte STREEWHEEL_KEY_RADIO = 26;
        public static final byte STREEWHEEL_KEY_SCREEN_OFF = 39;
        public static final byte STREEWHEEL_KEY_SPEECH = 51;
        public static final byte STREEWHEEL_KEY_SYNC = 57;
        public static final byte STREEWHEEL_KEY_TPMS = 38;
        public static final byte STREEWHEEL_KEY_TV = 41;
        public static final byte STREEWHEEL_KEY_VIDEO = 37;
        public static final byte STREEWHEEL_KEY_VOL_DECREASE = 7;
        public static final byte STREEWHEEL_KEY_VOL_INCREASE = 6;
        public static final byte SYSTEM_AUTOSTART_BY_ANYKEY = 22;
        public static final byte SYSTEM_AUTOSTART_BY_BACKCAR = 18;
        public static final byte SYSTEM_AUTOSTART_BY_BT = 20;
        public static final byte SYSTEM_BACKCAR_CHANGE_AUTO = 1;
        public static final byte SYSTEM_DEFAULT_VOLUME = 21;
        public static final byte SYSTEM_DVR = 25;
        public static final byte SYSTEM_GPS_LISTENER = 2;
        public static final byte SYSTEM_HAND_BREAK = 17;
        public static final byte SYSTEM_IPOD = 23;
        public static final byte SYSTEM_KEY_TONE = 42;
        public static final byte SYSTEM_LIGHT = 19;
    }

    public interface StreeWheelState {
        public static final int KEY_AUDIO = 14;
        public static final int KEY_BACK = 22;
        public static final int KEY_BT = 19;
        public static final int KEY_CALL = 8;
        public static final int KEY_DVD = 13;
        public static final int KEY_ENABLE = 10;
        public static final int KEY_HANDUP = 9;
        public static final int KEY_HOME = 21;
        public static final int KEY_MODE = 6;
        public static final int KEY_MUSIC = 15;
        public static final int KEY_MUTE = 2;
        public static final int KEY_NAVI = 11;
        public static final int KEY_NEXT = 4;
        public static final int KEY_NEXT_OR_CANCEL_CALL = 25;
        public static final int KEY_PLAY_PAUSE = 5;
        public static final int KEY_POWER = 7;
        public static final int KEY_PREVIOUS = 3;
        public static final int KEY_PREVIOU_OR_ACCEPT_CALL = 24;
        public static final int KEY_RADIO = 12;
        public static final int KEY_SCREEN_OFF = 18;
        public static final int KEY_SPEECH = 23;
        public static final int KEY_TPMS = 17;
        public static final int KEY_TV = 20;
        public static final int KEY_VIDEO = 16;
        public static final int KEY_VOL_DECREASE = 1;
        public static final int KEY_VOL_INCREASE = 0;
        public static final int STATE_END_STUDY = 0;
        public static final int STATE_SELECT_KEY = 3;
        public static final int STATE_START_STUDY = 1;
        public static final int STATE_STUDYING = 2;
    }

    public interface SystemSettingState {
        public static final int AUTO_BACK_CAR = 128;
        public static final int AUTO_START_BY_ANYKEY = 4;
        public static final int AUTO_START_BY_BT = 16;
        public static final int BACKCAR_MIRROR = 512;
        public static final int BACKCAR_VOLUME = 2048;
        public static final int BACK_CAR_START = 32;
        public static final int DEFAULT_VOLUME = 8;
        public static final int DVR = 1;
        public static final int GPS_LISTENER = 64;
        public static final int HAND_BRAKE_STATE = 16384;
        public static final int IPOD_ELECTRICITY = 2;
        public static final int KEY_TONE = 8192;
        public static final int LIGHT_STATE = 32768;
        public static final int SEND_FM = 4096;
    }

    public interface VideoChannel {
        public static final byte BACKCAR = 0;
        public static final byte FRONT = 1;
    }

    public interface VideoSetting {
        public static final byte BRIGTHNESS = 0;
        public static final byte COLOR = 2;
        public static final byte CONTRAST = 1;
        public static final byte MODE_BRIGTHNESS = 3;
        public static final byte MODE_COLORFUL = 4;
        public static final byte MODE_CUSTOM = 0;
        public static final byte MODE_RESET = 16;
        public static final byte MODE_SOFT = 2;
        public static final byte MODE_STANDARD = 1;
    }
}
