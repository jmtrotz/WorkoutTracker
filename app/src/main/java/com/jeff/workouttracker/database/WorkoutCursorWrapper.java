package com.jeff.workouttracker.database;

// Import packages
import android.database.Cursor;
import android.database.CursorWrapper;
import com.jeff.workouttracker.Workout;
import java.util.Date;
import java.util.UUID;

/**
 * Wrapper class for the database to get information about a specific workout
 *
 * @author Jeffrey Trotz
 * Date: 3/27/18
 * Class: CS 305
 * @version 1.0
 */
public class WorkoutCursorWrapper extends CursorWrapper
{
    /**
     * Constrictpr
     * @param cursor Database cursor
     */
    public WorkoutCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    /**
     * Gets information for a specific workout
     * @return Returns a workout object
     */
    public Workout getWorkout()
    {
        // Get the information from the database
        String uuidString = getString(getColumnIndex(WorkoutDatabaseSchema.WorkoutTable.Columns.UUID));
        String title =  getString(getColumnIndex(WorkoutDatabaseSchema.WorkoutTable.Columns.TITLE));
        String description = getString(getColumnIndex(WorkoutDatabaseSchema.WorkoutTable.Columns.DESCRIPTION));
        long date = getLong(getColumnIndex(WorkoutDatabaseSchema.WorkoutTable.Columns.DATE));
        int effort = getInt(getColumnIndex(WorkoutDatabaseSchema.WorkoutTable.Columns.EFFORT));
        int hour = getInt(getColumnIndex(WorkoutDatabaseSchema.WorkoutTable.Columns.HOUR));
        int minute = getInt(getColumnIndex(WorkoutDatabaseSchema.WorkoutTable.Columns.MINUTE));

        // Create a new workout object and set its parameters
        Workout workout = new Workout (UUID.fromString(uuidString));
        workout.setTitle(title);
        workout.setDescription(description);
        workout.setDate(new Date(date));
        workout.setHour(hour);
        workout.setMinute(minute);
        workout.setEffort(effort != 0);

        // Return the workout object
        return workout;
    }
}