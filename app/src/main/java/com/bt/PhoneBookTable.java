package com.bt;

import android.net.Uri;
import android.provider.BaseColumns;

/* loaded from: classes.dex */
public class PhoneBookTable implements BaseColumns {
    private static final String AUTHORITY = "com.nwd.bt.provider";
    public static final String DEFAULT_SORT_ORDER = "_ID DESC";
    public static final String PHONEBOOK_MAC = "phonebook_mac";
    public static final String PHONEBOOK_NAME = "phonebook_name";
    public static final String PHONEBOOK_NUMBER = "phonebook_number";
    public static final Uri PHONEBOOK_URI = Uri.parse("content://com.nwd.bt.provider/phonebook");
    protected static final String TABLE_NAME = "phonebook";
}
