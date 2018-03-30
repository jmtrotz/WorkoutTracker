package com.jeff.workouttracker.database;

// Import packages
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helper class to create the SQLite database used by this application
 *
 * @author Jeffrey Trotz
 * Date: 3/27/18
 * Class: CS 305
 * @version 1.0
 */
public class WorkoutDatabaseHelper extends SQLiteOpenHelper
{
    // Declare fields used in this class
    private static final String DATABASE_NAME = "workoutDatabase.db";
    private static final int VERSION = 1;

    /**
     * Constructor
     * @param context Application context
     */
    public WorkoutDatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    /**
     * Creates a new database table
     * @param database SQLite database to be created
     */
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL("CREATE TABLE " + WorkoutDatabaseSchema.WorkoutTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                WorkoutDatabaseSchema.WorkoutTable.Columns.UUID + ", " +
                WorkoutDatabaseSchema.WorkoutTable.Columns.TITLE + ", " +
                WorkoutDatabaseSchema.WorkoutTable.Columns.DESCRIPTION + ", " +
                WorkoutDatabaseSchema.WorkoutTable.Columns.DATE + ", " +
                WorkoutDatabaseSchema.WorkoutTable.Columns.HOUR + ", " +
                WorkoutDatabaseSchema.WorkoutTable.Columns.MINUTE + ", " +
                WorkoutDatabaseSchema.WorkoutTable.Columns.EFFORT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Not used
    }
}