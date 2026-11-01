package com.nwd.radio.service;

/* loaded from: classes.dex */
public class RadioConstant {
    public static final String ACTION_RADIO_SERVICE = "com.nwd.radio.service.ACTION_RADIO_SERVICE";
    public static final String ACTION_SEND_RADIO_FREQUENCE = "com.nwd.action.ACTION_SEND_RADIO_FREQUENCE";
    public static final String ACTION_SET_RADIO_FREQUENCE = "com.nwd.action.ACTION_SET_RADIO_FREQUENCE";
    public static final String EXTRA_IS_START_FROM_RDS = "extra_start_by_rds";
    public static final String EXTRA_IS_START_TOP_NAVI = "extra_start_top_navi";
    public static final String EXTRA_RADIO_FREQUENCE = "extra_radio_frequence";
    public static final String EXTRA_RADIO_PRESTORE_NUM = "extra_radio_prestore_num";

    public static final class BandType {
        public static final byte AM1 = 3;
        public static final byte AM2 = 4;
        public static final byte AM3 = 5;
        public static final byte FM1 = 0;
        public static final byte FM2 = 1;
        public static final byte FM3 = 2;
        public static final byte FML1 = 6;
        public static final byte FML2 = 7;
        public static final byte FML3 = 8;
    }

    public static final class PTY {
        public static final byte AFFAIRS = 2;
        public static final byte ALARM = 31;
        public static final byte CHILDREN = 18;
        public static final byte CLASSICS = 14;
        public static final byte COUNTRY = 25;
        public static final byte CULTURE = 7;
        public static final String[] DESTRIBUTION = {"NONE", "NEWS", "AFFAIRS", "INFO", "SPORT", "EDUCATE", "DRAMA", "CULTURE", "SCIENCE", "VARIED", "POP_M", "ROCK_M", "EASY_M", "LIGHT_M", "CLASSICS", "OTHER_M", "WEATHER", "FINANCE", "CHILDREN", "SOCIAL", "RELIGION", "PHONE_IN", "TRAVEL", "LEISURE", "JAZZ", "COUNTRY", "NATION_M", "OLDIES", "FOLK_M", "DOCUMENT", "TEST", "ALARM", "PTYSEEK"};
        public static final byte DOCUMENT = 29;
        public static final byte DRAMA = 6;
        public static final byte EASY_M = 12;
        public static final byte EDUCATE = 5;
        public static final byte FINANCE = 17;
        public static final byte FOLK_M = 28;
        public static final byte INFO = 3;
        public static final byte JAZZ = 24;
        public static final byte LEISURE = 23;
        public static final byte LIGHT_M = 13;
        public static final byte NATION_M = 26;
        public static final byte NEWS = 1;
        public static final byte NONE = 0;
        public static final byte OLDIES = 27;
        public static final byte OTHER_M = 15;
        public static final byte PHONE_IN = 21;
        public static final byte POP_M = 10;
        public static final byte PTYSEEK = 32;
        public static final byte RELIGION = 20;
        public static final byte ROCK_M = 11;
        public static final byte SCIENCE = 8;
        public static final byte SOCIAL = 19;
        public static final byte SPORT = 4;
        public static final byte TEST = 30;
        public static final byte TRAVEL = 22;
        public static final byte VARIED = 9;
        public static final byte WEATHER = 16;
    }

    public static final class RDSType {
        public static final int AF = 1;
        public static final int EON = 16;
        public static final int PTY = 4;
        public static final int RDS = 128;
        public static final int TA = 2;
        public static final int TP = 8;
    }

    public static final class RadioActionType {
        public static final byte AMS = 6;
        public static final byte BAND = 5;
        public static final byte INTRO = 7;
        public static final byte NEARON = 8;
        public static final byte PREFEB_NEXT = 10;
        public static final byte PREFEB_PREV = 11;
        public static final byte SEARCH_ADD = 3;
        public static final byte SEARCH_LOSE = 4;
        public static final byte SEEK_ADD = 1;
        public static final byte SEEK_LOSE = 2;
        public static final byte SET_AM = 13;
        public static final byte SET_FM = 12;
        public static final byte STEREOON = 9;
    }

    public static final class RadioState {
        public static final byte STATE_CLOSE = 0;
        public static final byte STATE_INTRO = 4;
        public static final byte STATE_SCAN = 3;
        public static final byte STATE_SEARCH = 2;
        public static final byte STATE_START = 1;
    }
}
