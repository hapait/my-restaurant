package com.mcc.restaurant.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mcc.restaurant.model.FoodItem;

import java.util.ArrayList;

public class FavouriteDbController {

    private SQLiteDatabase db;

    public FavouriteDbController(Context context) {
        db = DbHelper.getInstance(context).getWritableDatabase();
    }

    public int insertData(int foodId, String foodTitle, String foodDesc, String foodImg) {

        ContentValues values = new ContentValues();
        values.put(DbConstants.FOOD_ID, foodId);
        values.put(DbConstants.FOOD_TITLE, foodTitle);
        values.put(DbConstants.FOOD_DESC, foodDesc);
        values.put(DbConstants.FOOD_IMG, foodImg);

        // Insert the new row, returning the primary key value of the new row
        return (int) db.insert(
                DbConstants.FAVOURITE_TABLE_NAME,
                DbConstants.COLUMN_NAME_NULLABLE,
                values);
    }

    public ArrayList<FoodItem> getAllData() {


        String[] projection = {
                DbConstants._ID,
                DbConstants.FOOD_ID,
                DbConstants.FOOD_TITLE,
                DbConstants.FOOD_DESC,
                DbConstants.FOOD_IMG,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DbConstants._ID + " DESC";

        Cursor c = db.query(
                DbConstants.FAVOURITE_TABLE_NAME,         // The table name to query
                projection,                               // The columns to return
                null,                             // The columns for the WHERE clause
                null,                          // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return fetchData(c);
    }

    private ArrayList<FoodItem> fetchData(Cursor c) {
        ArrayList<FoodItem> favDataArray = new ArrayList<>();

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    // get  the  data into array,or class variable
                    int itemId = c.getInt(c.getColumnIndexOrThrow(DbConstants._ID));
                    int foodId = c.getInt(c.getColumnIndexOrThrow(DbConstants.FOOD_ID));
                    String foodTitle = c.getString(c.getColumnIndexOrThrow(DbConstants.FOOD_TITLE));
                    String foodDesc = c.getString(c.getColumnIndexOrThrow(DbConstants.FOOD_DESC));
                    String foodImage=c.getString(c.getColumnIndexOrThrow(DbConstants.FOOD_IMG));


                    // wrap up data list and return
                    favDataArray.add(new FoodItem(foodId,  new FoodItem.Title(foodTitle), new FoodItem.Content(foodDesc), foodImage));
                } while (c.moveToNext());
            }
            c.close();
        }
        return favDataArray;
    }

    public void deleteFav(int foodId) {
        // Which row to update, based on the ID
        String selection = DbConstants.FOOD_ID + "=?";
        String[] selectionArgs = {String.valueOf(foodId)};

        db.delete(
                DbConstants.FAVOURITE_TABLE_NAME,
                selection,
                selectionArgs);
    }

    public void deleteAllFav() {
        db.delete(
                DbConstants.FAVOURITE_TABLE_NAME,
                null,
                null);
    }

}
