package com.jeff.workouttracker;

// Import packages
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Jeffrey Trotz
 * Date: 3/27/18
 * Class: CS 305
 * @version 1.0
 */
public class Workout
{
    // Declare fields used in this class
    private UUID mUUID;
    private Date mDate;
    private boolean mEffort;
    private int mHour;
    private int mMinute;
    private String mTitle;
    private String mDescription;

    /**
     * Default constructor
     */
    public Workout()
    {
        this(UUID.randomUUID());
    }

    /**
     * Overloaded constructor
     * @param uuid UUID for the workout object
     */
    public Workout(UUID uuid)
    {
        mUUID = uuid;
        mDate = new Date();
        mHour = 0;
    }

    /**
     * The default output of the Java Date() method is REALLY ugly,
     * so this method makes it a little more pleasing to the eye
     * @return Returns a much cleaner, easier to read date as a String
     */
    public String getWorkoutTime()
    {
        // Formats the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
        String formattedDate = dateFormat.format(this.getDate());
        String timeOfDay;

        // Decide if "AM" or "PM" should be displayed at the end of the date/time string
        if (mHour == 0)
        {
            mHour += 12;
            timeOfDay = "AM";
        }

        else if (mHour == 12)
        {
            timeOfDay = "PM";
        }

        else if (mHour > 12)
        {
            mHour -= 12;
            timeOfDay = "PM";
        }

        else
        {
            timeOfDay = "AM";
        }

        // Return the new date/time string in a format that's much easier to read
        if (mMinute == 0)
        {
            return formattedDate + " at " + this.getHour() + timeOfDay;
        }

        else
        {
            return formattedDate + " at " + this.getHour() + ":" + this.getMinute() + timeOfDay;
        }
    }

    /**
     * Returns the filename for the photo taken by the user in activity_workout_details
     * @return Returns the filename of the photo as a String
     */
    public String getPhotoFilename()
    {
        return "IMG_" + getUUID().toString() + ".jpg";
    }

    /**
     * Getter for mUUID
     * @return Returns the UUID for the workout
     */
    public UUID getUUID()
    {
        return mUUID;
    }

    /**
     * Getter for mDate
     * @return Returns mDate as a date object
     */
    public Date getDate()
    {
        return mDate;
    }

    /**
     * Setter for mDate
     * @param date Date of the workout
     */
    public void setDate(Date date)
    {
        mDate = date;
    }

    /**
     * Getter for mEffort
     * @return Returns mEffort as a boolean
     */
    public boolean getEffort()
    {
        return mEffort;
    }

    /**
     * Setter for mEffort
     * @param effort Boolean value of the checkbox in activity_workout_details
     *               (0 = not checked, 1 = checked)
     */
    public void setEffort(boolean effort)
    {
        mEffort = effort;
    }

    /**
     * Setter for mHour
     * @param hour Hour of the the day the workout took place
     */
    public void setHour(int hour)
    {
        mHour = hour;
    }

    /**
     * Getter for mHour
     * @return Returns mHour as an integer
     */
    public int getHour()
    {
        return mHour;
    }

    /**
     * Setter for mMinute
     * @param minute Minute of the the day the workout took place
     */
    public void setMinute(int minute)
    {
        mMinute = minute;
    }

    /**
     * Getter for mMinute
     * @return Returns mMinute as an integer
     */
    public int getMinute()
    {
        return mMinute;
    }

    /**
     * Setter for mTitle
     * @param title Title for the workout
     */
    public void setTitle(String title)
    {
        mTitle = title;
    }

    /**
     * Getter for mTitle
     * @return Returns mTitle as a String
     */
    public String getTitle()
    {
        return mTitle;
    }

    /**
     * Setter for mDescription
     * @param description Description of the workout
     */
    public void setDescription(String description)
    {
        mDescription = description;
    }

    /**
     * Getter for mDescription
     * @return Returns mDescription as a String
     */
    public String getDescription()
    {
        return mDescription;
    }
}