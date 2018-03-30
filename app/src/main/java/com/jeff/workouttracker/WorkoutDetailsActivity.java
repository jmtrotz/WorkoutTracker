package com.jeff.workouttracker;

// Import packages
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

/**
 * Details activity for viewing or entering information about a workout
 *
 * @author Jeffrey Trotz
 * Date: 3/27/18
 * Class: CS 305
 * @version 1.0
 */
public class WorkoutDetailsActivity extends AppCompatActivity
{
    // Declare fields used in this class
    private Workout mWorkout;
    private EditText mTitleView;
    private EditText mDescriptionView;
    private CheckBox mEffortCheckBox;
    private Button mDateButton;
    private ImageButton mCameraButton;
    private ImageView mPhotoView;
    private FloatingActionButton mSaveButton;
    private File mPhotoFile;
    private static final int REQUEST_PHOTO = 2;
    private static final String DELETE_TOAST = "Deleting workout...";
    private static final String SAVE_TOAST = "Saving workout...";
    private static final String DEFAULT_TITLE = "Default workout title";
    private static final String DATE_DIALOG_TITLE = "Workout Date:";
    private static final String TIME_DIALOG_TITLE = "Workout Time:";
    private static final String DEFAULT_DESCRIPTION = "Default workout description";
    private static final String EXTRA_UUID = "details_uuid";
    private static final String CAMERA_AUTHORITY = "com.jeff.workouttracker.fileprovider";
    private static final String KEY_TITLE = "title_data";
    private static final String KEY_DESCRIPTION = "description_data";
    private static final String KEY_CHECKBOX = "checkbox_data";
    private static final String KEY_DATE = "date_data";

    /**
     * Intent to launch the details activity
     * @param context Application context
     * @param uuid UUID for the workout to be viewed
     * @return Returns an intent
     */
    public static Intent newIntent(Context context, UUID uuid)
    {
        // Create a new intent object and add the UUID as an exta
        Intent intent = new Intent(context, WorkoutDetailsActivity.class);
        intent.putExtra(EXTRA_UUID, uuid);

        // Return the new intent object
        return intent;
    }

    /**
     * Updates the PhotoView after the user takes a photo
     */
    private void updatePhotoView()
    {
        // If the photo file string is null or doesn't exit, set the image to null
        if (mPhotoFile == null || !mPhotoFile.exists())
        {
            mPhotoView.setImageDrawable(null);
        }

        // If the photo file isn't null and does exist, resize it and set the photo in the ImageView
        else
        {
            Bitmap bitmap = PictureFactory.getScaledBitmap(mPhotoFile.getPath(),
                    WorkoutDetailsActivity.this);
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    /**
     * This method is called when the activity is launched
     * @param savedInstanceState Saved instance of the application
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set the layout and fish out the ID's for the UI elements
        setContentView(R.layout.activity_details);
        mTitleView = findViewById(R.id.workout_title);
        mDescriptionView = findViewById(R.id.workout_description);
        mEffortCheckBox = findViewById(R.id.effort_check_box);
        mDateButton = findViewById(R.id.date_chooser_button);
        mCameraButton = findViewById(R.id.camera_button);
        mPhotoView = findViewById(R.id.photo_view);
        mSaveButton = findViewById(R.id.save_button);
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_UUID);

        // If savedInstanceState isn't empty, extract the data and load it into the UI
        if (savedInstanceState != null)
        {
            mTitleView.setText(savedInstanceState.getString(KEY_TITLE));
            mDescriptionView.setText(savedInstanceState.getString(KEY_DESCRIPTION));
            mEffortCheckBox.setChecked(savedInstanceState.getBoolean(KEY_CHECKBOX));
            mDateButton.setText(savedInstanceState.getString(KEY_DATE));
        }

        // Get the information for the workout the user chose to view
        if (uuid != null)
        {
            mWorkout = WorkoutHelper.get(WorkoutDetailsActivity.this).getWorkout(uuid);

            /*
             * If the hour hasn't been set, then the user is creating a new workout, so we don't
             * set the text for the date chooser button to avoid having it display "null"
             */
            if (mWorkout.getHour() > 0)
            {
                mDateButton.setText(mWorkout.getWorkoutTime());
            }

            mTitleView.setText(mWorkout.getTitle());
            mDescriptionView.setText(mWorkout.getDescription());
            mEffortCheckBox.setChecked(mWorkout.getEffort());
            mPhotoFile = WorkoutHelper.get(WorkoutDetailsActivity.this).getPhotoFile(mWorkout);
            updatePhotoView();

            // Enable the camera button
            final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            PackageManager packageManager = getPackageManager();
            final boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
            mCameraButton.setEnabled(canTakePhoto);

            // Listener for the title text field
            mTitleView.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
                {
                    // Not used
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count)
                {
                    // Set the title of the workout if the user enters something
                    mWorkout.setTitle(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    // Not used
                }
            });

            // Listener for the description text field
            mDescriptionView.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after)
                {
                    // Not used
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int count)
                {
                    // Set the description of the workout if the user enters something
                    mWorkout.setDescription(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    // Not used
                }
            });

            // Listener for the effort check box
            mEffortCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    // Set the effort if the user selects the checkbox
                    mWorkout.setEffort(isChecked);
                }
            });

            // Listener for the date chooser button. Launches date and time picker dialogs
            mDateButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View datePickerButton)
                {
                    // Create a new calendar instance and get values from it
                    Calendar calendar = Calendar.getInstance();
                    int mYear = calendar.get(Calendar.YEAR);
                    int mMonth = calendar.get(Calendar.MONTH);
                    int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                    int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                    int mMinute = calendar.get(Calendar.MINUTE);

                    // Show the date picker dialog and pass in the calendar values
                    showDatePicker(mYear, mMonth, mDay, mHour, mMinute);
                }
            });

            // Listener for the camera button. Launches the camera
            mCameraButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View cameraButton)
                {
                    // Generate file URI
                    Uri uri = FileProvider.getUriForFile(WorkoutDetailsActivity.this,
                            CAMERA_AUTHORITY, mPhotoFile);
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    List<ResolveInfo> cameraActivities = getPackageManager()
                            .queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                    for (ResolveInfo activity: cameraActivities)
                    {
                        // Grant camera permissions and launch the camera
                        grantUriPermission(activity.activityInfo.packageName, uri,
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivityForResult(captureImage, REQUEST_PHOTO);
                    }
                }
            });

            // Listener for the save button
            mSaveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View saveButton)
                {
                    // Updates the workout in the database then closes the activity
                    WorkoutHelper.get(WorkoutDetailsActivity.this).updateWorkout(mWorkout);
                    Toast toast = Toast.makeText(WorkoutDetailsActivity.this,
                            SAVE_TOAST, Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            });
        }

        // Create a new workout with default values if UUID was null
        else
        {
            mWorkout = new Workout();
            mTitleView.setText(DEFAULT_TITLE);
            mDescriptionView.setText(DEFAULT_DESCRIPTION);
            mEffortCheckBox.setChecked(false);
        }
    }

    /**
     * Stores data when the application is closed or screen is rotated
     * @param outState Bundle of current data stored in the application
     */
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_TITLE, mTitleView.getText().toString());
        outState.putString(KEY_DESCRIPTION, mDescriptionView.getText().toString());
        outState.putBoolean(KEY_CHECKBOX, mEffortCheckBox.isChecked());
        outState.putString(KEY_DATE, mDateButton.getText().toString());
    }

    /**
     * Creates the menu item shown in the top right of the UI
     * @param menu Menu item in the toolbar
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.workout_details, menu);
        return true;
    }

    /**
     * This method is called when the user selects the menu button.
     * It deletes the workout they are currently viewing
     * @param menuItem Chosen menu item
     * @return Returns a boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            // Deletes the workout and closes the activity
            case R.id.delete_button:
            {
                WorkoutHelper.get(WorkoutDetailsActivity.this).deleteWorkout(mWorkout);
                Toast toast = Toast.makeText(WorkoutDetailsActivity.this,
                        DELETE_TOAST, Toast.LENGTH_SHORT);
                toast.show();
                finish();
                return true;
            }

            default:
            {
                return super.onOptionsItemSelected(menuItem);
            }
        }
    }

    /**
     * Method to show the date picker dialog window
     * @param year Year the workout took place
     * @param month Month the workout took place
     * @param day Day the workout took place
     * @param hour Hour the workout took place
     * @param minute Minute the workout took place
     */
    private void showDatePicker(int year, int month, int day, final int hour, final int minute)
    {
        // Create the view for the date picker dialog window and set parameters
        View datePickerView = LayoutInflater.from(WorkoutDetailsActivity.this)
                .inflate(R.layout.dialog_date_picker, null);
        final DatePicker datePicker = datePickerView.findViewById(R.id.date_picker);
        datePicker.init(year, month, day, null);

        // Show the dialog window
        Dialog datePickerDialog = new AlertDialog.Builder(WorkoutDetailsActivity.this)
                .setView(datePickerView).setTitle(DATE_DIALOG_TITLE)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // Get values from the dialog window
                        int year = datePicker.getYear();
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();

                        // Use the values to create a date and store it in the workout object
                        Date date = new GregorianCalendar(year, month, day).getTime();
                        mWorkout.setDate(date);

                        // Launch the time picker
                        showTimePicker(hour, minute);
                    }
                }).create();
        datePickerDialog.show();
    }

    /**
     * Method to show the time picker dialog window. Requires
     * Android 6.0 Marshmallow or newer to function properly...
     * @param hour Hour the workout took place
     * @param minute Minute the workout took place
     */
    private void showTimePicker(int hour, int minute)
    {
        // Create the view for the time picker dialog window
        View timePickerView = LayoutInflater.from(WorkoutDetailsActivity.this)
                .inflate(R.layout.dialog_time_picker, null);
        final TimePicker timePicker = timePickerView.findViewById(R.id.time_picker);

        // If the user is running Android 6.0 (Marshmallow) or newer, set the hour and minute
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        }

        // Show the dialog window
        Dialog timePickerDialog = new AlertDialog.Builder(WorkoutDetailsActivity.this)
                .setView(timePickerView).setTitle(TIME_DIALOG_TITLE)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        /*
                         * If the user is running Android 6.0 (Marshmallow) or newer, get the
                         * values from the dialog window and store them in the workout object
                         */
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        {
                            int hour = timePicker.getHour();
                            int minute = timePicker.getMinute();
                            mWorkout.setHour(hour);
                            mWorkout.setMinute(minute);
                        }

                        // Display the chosen date/time on the date picker button
                        mDateButton.setText(mWorkout.getWorkoutTime());
                    }
                }).create();
        timePickerDialog.show();
    }
}