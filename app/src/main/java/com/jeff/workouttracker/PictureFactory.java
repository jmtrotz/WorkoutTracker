package com.jeff.workouttracker;

// Import packages
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

/**
 * Resizes pictures taken by the user so it can be displayed in the progress photo ImageView
 *
 * @author Jeffrey Trotz
 * Date: 3/27/18
 * Class: CS 305
 * @version 1.0
 */
public class PictureFactory
{
    /**
     * Scales down the photo taken by the user
     * @param path File path to the image
     * @param activity Activity that called this method
     * @return Returns a scaled down version of the original bitmap
     */
    public static Bitmap getScaledBitmap(String path, Activity activity)
    {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }

    /**
     * Scales down the photo taken by the user
     * @param path File path to the image
     * @param width Width the image will be scaled down to
     * @param height Height the image will be scaled down to
     * @return Returns a decoded file
     */
    public static Bitmap getScaledBitmap(String path, int width, int height)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if (srcHeight > height || srcWidth > width)
        {
            float widthScale = srcWidth / width;
            float heightScale = srcHeight / height;

            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(path, options);
    }
}