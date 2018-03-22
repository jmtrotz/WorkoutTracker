package com.jeff.workouttracker;

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

public class WorkoutHelper
{
    private static WorkoutHelper sWorkoutHelper;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private WorkoutHelper(Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new WorkoutDatabaseHelper(mContext).getWritableDatabase();
    }

    private WorkoutCursorWrapper queryWorkouts(String whereClause, String[] whereArguments)
    {
        Cursor cursor = mDatabase.query(WorkoutDatabaseSchema.WorkoutTable.NAME, null,
                whereClause, whereArguments, null, null, null);
        return new WorkoutCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Workout workout)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(WorkoutDatabaseSchema.WorkoutTable.Columns.UUID, workout.getUUID().toString());
        contentValues.put(WorkoutDatabaseSchema.WorkoutTable.Columns.TITLE, workout.getTitle());
        contentValues.put(WorkoutDatabaseSchema.WorkoutTable.Columns.DESCRIPTION, workout.getDescription());
        contentValues.put(WorkoutDatabaseSchema.WorkoutTable.Columns.DATE, workout.getDate().getTime());
        contentValues.put(WorkoutDatabaseSchema.WorkoutTable.Columns.EFFORT, workout.getEffort() ? 1 : 0);

        return contentValues;
    }

    public static WorkoutHelper get(Context context)
    {
        if (sWorkoutHelper == null)
        {
            sWorkoutHelper = new WorkoutHelper(context);
        }

        return sWorkoutHelper;
    }

    public List<Workout> getWorkouts()
    {
        List<Workout> workouts = new ArrayList<>();
        WorkoutCursorWrapper cursor = queryWorkouts(null, null);

        try
        {
            cursor.moveToFirst();

            while (!cursor.isAfterLast())
            {
                workouts.add(cursor.getWorkout());
                cursor.moveToNext();
            }
        }

        finally
        {
            cursor.close();
        }

        return workouts;
    }

    public Workout getWorkout(UUID uuid)
    {
        WorkoutCursorWrapper cursor = queryWorkouts(WorkoutDatabaseSchema.WorkoutTable
                .Columns.UUID + " = ?", new String[] {uuid.toString()});

        try
        {
            if (cursor.getCount() == 0)
            {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getWorkout();
        }

        finally
        {
            cursor.close();
        }
    }

    public File getPhotoFile(Workout workout)
    {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, workout.getPhotoFilename());
    }

    public void addWorkout(Workout workout)
    {
        ContentValues contentValues = getContentValues(workout);
        mDatabase.insert(WorkoutDatabaseSchema.WorkoutTable.NAME, null, contentValues);
    }

    public void updateWorkout(Workout workout)
    {
        String uuidString = workout.getUUID().toString();
        ContentValues contentValues = getContentValues(workout);
        mDatabase.update(WorkoutDatabaseSchema.WorkoutTable.NAME, contentValues,
                WorkoutDatabaseSchema.WorkoutTable.Columns.UUID + " = ? ",
                new String[] {uuidString});
    }

    public void deleteWorkout(Workout workout)
    {
        String uuidString = workout.getUUID().toString();
        mDatabase.delete(WorkoutDatabaseSchema.WorkoutTable.NAME, WorkoutDatabaseSchema
                .WorkoutTable.Columns.UUID + " = ?", new String[] {uuidString});
    }
}