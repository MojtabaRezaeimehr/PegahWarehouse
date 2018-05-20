package com.example.mrezaei.pegahwarehouse;

import android.provider.BaseColumns;

public final class LastStatContract {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public LastStatContract() {}

    /* Inner class that defines the table contents */
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "lastStatistics";
        public static final String COLUMN_NAME_DELIVERY_COUNT = "delivery_count";
        public static final String COLUMN_NAME_SHRINK_COUNT = "shrink_count";
        public static final String COLUMN_NAME_PALLET_COUNT = "pallet_count";
        public static final String COLUMN_NAME_DELIVER_ID = "deliver_id";
    }

}