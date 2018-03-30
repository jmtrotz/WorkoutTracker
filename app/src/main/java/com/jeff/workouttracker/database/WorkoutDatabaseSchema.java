package com.jeff.workouttracker.database;

/**
 * Schema for the SQLite database used by this application
 *
 * @author Jeffrey Trotz
 * Date: 3/27/18
 * Class: CS 305
 * @version 1.0
 */
public class WorkoutDatabaseSchema
{
    /**
     * Nested class to store the name of the database table
     */
    public static final class WorkoutTable
    {
        public static final String NAME = "workouts";

        /**
         * Nested class to store the column names for the database table
         */
        public static final class Columns
        {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String DATE = "date";
            public static final String HOUR = "hour";
            public static final String MINUTE = "minute";
            public static final String EFFORT = "effort";
        }
    }
}