package com.mcc.restaurant.data.sqlite;

import android.provider.BaseColumns;


public class DbConstants implements BaseColumns{
    // commons
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    public static final String COLUMN_NAME_NULLABLE = null;


    // favourites
    public static final String FAVOURITE_TABLE_NAME = "favourite";

    public static final String FOOD_ID = "food_id";
    public static final String FOOD_TITLE = "food_title";
    public static final String FOOD_DESC = "food_desc";
    public static final String FOOD_IMG="food_img";

    public static final String SQL_CREATE_FAVOURITE_ENTRIES =
            "CREATE TABLE " + FAVOURITE_TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    FOOD_ID + INTEGER_TYPE + COMMA_SEP +
                    FOOD_TITLE + TEXT_TYPE + COMMA_SEP +
                    FOOD_DESC + TEXT_TYPE + COMMA_SEP +
                    FOOD_IMG + TEXT_TYPE+ " )";


    public static final String SQL_DELETE_FAVOURITE_ENTRIES =
            "DROP TABLE IF EXISTS " + FAVOURITE_TABLE_NAME;
}
