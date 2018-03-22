package com.jeff.workouttracker.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.jeff.workouttracker.Workout;

import java.util.Date;
import java.util.UUID;

public class WorkoutCursorWrapper extends CursorWrapper
{
    public WorkoutCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public Workout getWorkout()
    {
        String uuidString = getString(getColumnIndex(WorkoutDatabaseSchema.WorkoutTable.Columns.UUID));
        String title =  getString(getColumnIndex(WorkoutDatabaseSchema.WorkoutTable.Columns.TITLE));
        String description = getString(getColumnIndex(WorkoutDatabaseSchema.WorkoutTable.Columns.DESCRIPTION));
        long date = getLong(getColumnIndex(WorkoutDatabaseSchema.WorkoutTable.Columns.DATE));
        int effort = getInt(getColumnIndex(WorkoutDatabaseSchema.WorkoutTable.Columns.EFFORT));

        Workout workout = new Workout (UUID.fromString(uuidString));
        workout.setTitle(title);
        workout.setDescription(description);
        workout.setDate(new Date(date));
        workout.setEffort(effort != 0);

        return workout;
    }
}