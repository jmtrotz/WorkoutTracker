package com.jeff.workouttracker;

import java.util.Date;
import java.util.UUID;

public class Workout
{
    private UUID mUUID;
    private String mTitle;
    private String mDescription;
    private Date mDate;
    private boolean mEffort;

    public Workout()
    {
        this(UUID.randomUUID());
    }

    public Workout(UUID uuid)
    {
        mUUID = uuid;
        mDate = new Date();
    }

    public UUID getUUID()
    {
        return mUUID;
    }

    public String getTitle()
    {
        return mTitle;
    }

    public void setTitle(String title)
    {
        mTitle = title;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public void setDescription(String description)
    {
        mDescription = description;
    }

    public Date getDate()
    {
        return mDate;
    }

    public void setDate(Date date)
    {
        mDate = date;
    }

    public boolean getEffort()
    {
        return mEffort;
    }

    public void setEffort(boolean effort)
    {
        mEffort = effort;
    }

    public String getPhotoFilename()
    {
        return "IMG_" + getUUID().toString() + ".jpg";
    }
}