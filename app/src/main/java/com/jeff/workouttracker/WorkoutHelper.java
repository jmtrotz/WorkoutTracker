package com.jeff.workouttracker;

// Import packages
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jeff.workouttracker.database.WorkoutCursorWrapper;
import com.jeff.workouttracker.database.WorkoutDatabaseHelper;
import com.jeff.workouttracker.database.WorkoutDatabaseSchema;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Helper class to access workout information stored in the database
 *
 * @author Jeffrey Trotz
 * Date: 3/27/18
 * Class: CS 305
 * @version 1.0
 */
public class WorkoutHelper
{
    // Declare fields used in this class
    private static WorkoutHelper sWorkoutHelper;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    /**
     * Constructor
     * @param context Application context
     */
    private WorkoutHelper(Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new WorkoutDatabaseHelper(mContext).getWritableDatabase();
    }

    /**
     * Creates a cursor object
     * @param whereClause SQL statement to be executed
     * @param whereArguments Arguments for the SQL statement
     * @return Returns a new cursor object
     */
    private WorkoutCursorWrapper queryWorkouts(String whereClause, String[] whereArguments)
    {
        Cursor cursor = mDatabase.query(WorkoutDatabaseSchema.WorkoutTable.NAME, null,
                whereClause, whereArguments, null, null, null);
        return new WorkoutCursorWrapper(cursor);
    }

    /**
     * Returns
     * @param workout Workout selected by the user
     * @return Returns ContentValues for the workout
     */
    private static ContentValues getContentValues(Workout workout)
    {
        // Create the ContentValues object
        ContentValues contentValues = new ContentValues();

        // Get information from the database and store it in the ContentValues object
        contentValues.put(WorkoutDatabaseSchema.WorkoutTable.Columns.UUID, workout.getUUID().toString());
        contentValues.put(WorkoutDatabaseSchema.WorkoutTable.Columns.TITLE, workout.getTitle());
        contentValues.put(WorkoutDatabaseSchema.WorkoutTable.Columns.DESCRIPTION, workout.getDescription());
        contentValues.put(WorkoutDatabaseSchema.WorkoutTable.Columns.DATE, workout.getDate().getTime());
        contentValues.put(WorkoutDatabaseSchema.WorkoutTable.Columns.HOUR, workout.getHour());
        contentValues.put(WorkoutDatabaseSchema.WorkoutTable.Columns.MINUTE, workout.getMinute());
        contentValues.put(WorkoutDatabaseSchema.WorkoutTable.Columns.EFFORT, workout.getEffort() ? 1 : 0);

        // Return the ContentValues object
        return contentValues;
    }

    /**
     * Method to get a WorkoutHelper object
     * @param context Application context
     * @return Returns a WorkoutHelper object
     */
    public static WorkoutHelper get(Context context)
    {
        // If the object doesn't exist, create a new one
        if (sWorkoutHelper == null)
        {
            sWorkoutHelper = new WorkoutHelper(context);
        }

        // Return the WorkoutHelper object
        return sWorkoutHelper;
    }

    /**
     * Gets a list of all workouts in the database
     * @return Returns a list of all workout objects stored in the database
     */
    public List<Workout> getWorkouts()
    {
        List<Workout> workouts = new ArrayList<>();
        WorkoutCursorWrapper cursor = queryWorkouts(null, null);

        try
        {
            // Move to the first workout in the database
            cursor.moveToFirst();

            // Adds a workout to the list and moves to the next one
            while (!cursor.isAfterLast())
            {
                workouts.add(cursor.getWorkout());
                cursor.moveToNext();
            }
        }

        // Close the cursor
        finally
        {
            cursor.close();
        }

        // Return the list
        return workouts;
    }

    /**
     * Gets a workout object from the database
     * @param uuid UUID of the workout
     * @return Returns a workout object
     */
    public Workout getWorkout(UUID uuid)
    {
        WorkoutCursorWrapper cursor = queryWorkouts(WorkoutDatabaseSchema.WorkoutTable
                .Columns.UUID + " = ?", new String[] {uuid.toString()});

        try
        {
            // Returns null if the database is empty
            if (cursor.getCount() == 0)
            {
                return null;
            }

            // Move the cursor to the first position and return the workout at that position
            cursor.moveToFirst();
            return cursor.getWorkout();
        }

        // Close the cursor
        finally
        {
            cursor.close();
        }
    }

    /**
     * Returns the photo for the workout the user is viewing
     * @param workout Workout object
     * @return Returns the photo file
     */
    public File getPhotoFile(Workout workout)
    {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, workout.getPhotoFilename());
    }

    /**
     * Adds a workout to the database
     * @param workout Workout object to be saved
     */
    public void addWorkout(Workout workout)
    {
        ContentValues contentValues = getContentValues(workout);
        mDatabase.insert(WorkoutDatabaseSchema.WorkoutTable.NAME, null, contentValues);
    }

    /**
     * Updates an existing workout in the database
     * @param workout Workout object to be updated
     */
    public void updateWorkout(Workout workout)
    {
        String uuidString = workout.getUUID().toString();
        ContentValues contentValues = getContentValues(workout);
        mDatabase.update(WorkoutDatabaseSchema.WorkoutTable.NAME, contentValues,
                WorkoutDatabaseSchema.WorkoutTable.Columns.UUID + " = ? ",
                new String[] {uuidString});
    }

    /**
     * Deletes a workout from the database
     * @param workout Workout object to be deleted
     */
    public void deleteWorkout(Workout workout)
    {
        String uuidString = workout.getUUID().toString();
        mDatabase.delete(WorkoutDatabaseSchema.WorkoutTable.NAME, WorkoutDatabaseSchema
                .WorkoutTable.Columns.UUID + " = ?", new String[] {uuidString});
    }
}