package com.jeff.workouttracker.database;

public class WorkoutDatabaseSchema
{
    public static final class WorkoutTable
    {
        public static final String NAME = "workouts";

        public static final class Columns
        {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String DATE = "date";
            public static final String EFFORT = "effort";
        }
    }
}