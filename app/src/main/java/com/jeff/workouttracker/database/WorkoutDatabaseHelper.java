package com.jeff.workouttracker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutDatabaseHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "workoutDatabase.db";
    private static final int VERSION = 1;

    public WorkoutDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL("CREATE TABLE " + WorkoutDatabaseSchema.WorkoutTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                WorkoutDatabaseSchema.WorkoutTable.Columns.UUID + ", " +
                WorkoutDatabaseSchema.WorkoutTable.Columns.TITLE + ", " +
                WorkoutDatabaseSchema.WorkoutTable.Columns.DESCRIPTION + ", " +
                WorkoutDatabaseSchema.WorkoutTable.Columns.DATE + ", " +
                WorkoutDatabaseSchema.WorkoutTable.Columns.EFFORT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Not used
    }
}