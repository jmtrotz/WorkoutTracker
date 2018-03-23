package com.jeff.workouttracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public class WorkoutDetailsActivity extends AppCompatActivity
{
    private Workout mWorkout;
    private EditText mTitleView;
    private EditText mDescriptionView;
    private CheckBox mEffortCheckBox;
    private ImageButton mDateButton;
    private ImageButton mCameraButton;
    private ImageView mPhotoView;
    private FloatingActionButton mSaveButton;
    private File mPhotoFile;
    private static final int REQUEST_PHOTO = 2;
    private static final String DEFAULT_TITLE = "Default workout title";
    private static final String DEFAULT_DESCRIPTION = "Default workout description";
    private static final String EXTRA_UUID = "details_uuid";
    private static final String CAMERA_AUTHORITY = "com.jeff.workouttracker.fileprovider";

    public static Intent newIntent(Context context, UUID uuid)
    {
        Intent intent = new Intent(context, WorkoutDetailsActivity.class);
        intent.putExtra(EXTRA_UUID, uuid);
        return intent;
    }

    private void updatePhotoView()
    {
        if (mPhotoFile == null || !mPhotoFile.exists())
        {
            mPhotoView.setImageDrawable(null);
        }

        else
        {
            Bitmap bitmap = PictureFactory.getScaledBitmap(mPhotoFile.getPath(), WorkoutDetailsActivity.this);
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);
        mTitleView = findViewById(R.id.workout_title);
        mDescriptionView = findViewById(R.id.workout_description);
        mEffortCheckBox = findViewById(R.id.effort_check_box);
        mDateButton = findViewById(R.id.date_chooser_button);
        mCameraButton = findViewById(R.id.camera_button);
        mPhotoView = findViewById(R.id.photo_view);
        mSaveButton = findViewById(R.id.save_button);
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_UUID);

        if (uuid != null)
        {
            mWorkout = WorkoutHelper.get(WorkoutDetailsActivity.this).getWorkout(uuid);
            mTitleView.setText(mWorkout.getTitle());
            mDescriptionView.setText(mWorkout.getDescription());
            mEffortCheckBox.setChecked(mWorkout.getEffort());
            mPhotoFile = WorkoutHelper.get(WorkoutDetailsActivity.this).getPhotoFile(mWorkout);
            updatePhotoView();

            final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            PackageManager packageManager = getPackageManager();
            boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
            mCameraButton.setEnabled(canTakePhoto);

            mTitleView.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                    // Not used
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    mWorkout.setTitle(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    // Not used
                }
            });

            mDescriptionView.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {
                    // Not used
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {
                    mWorkout.setDescription(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    // Not used
                }
            });

            mEffortCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    mWorkout.setEffort(isChecked);
                }
            });

            mDateButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    View v = LayoutInflater.from(WorkoutDetailsActivity.this).inflate(R.layout.dialog_date_picker, null);
                    final DatePicker datePicker = v.findViewById(R.id.dialog_date_picker);
                    Dialog dialog = new AlertDialog.Builder(WorkoutDetailsActivity.this)
                            .setView(v)
                            .setTitle("Workout Date:")
                            .setPositiveButton(android.R.string.ok, new DialogInterface
                                    .OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    int year = datePicker.getYear();
                                    int month = datePicker.getMonth();
                                    int day = datePicker.getDayOfMonth();
                                    Date date = new GregorianCalendar(year, month, day).getTime();
                                    mWorkout.setDate(date);
                                }
                            })
                            .create();
                    dialog.show();
                }
            });

            mCameraButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Uri uri = FileProvider.getUriForFile(WorkoutDetailsActivity.this, CAMERA_AUTHORITY, mPhotoFile);
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    List<ResolveInfo> cameraActivities = getPackageManager().queryIntentActivities(captureImage,
                            PackageManager.MATCH_DEFAULT_ONLY);

                    for (ResolveInfo activity: cameraActivities)
                    {
                        grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivityForResult(captureImage, REQUEST_PHOTO);
                    }
                }
            });

            mSaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WorkoutHelper.get(WorkoutDetailsActivity.this).updateWorkout(mWorkout);
                    finish();
                }
            });
        }

        else
        {
            mWorkout = new Workout();
            mTitleView.setText(DEFAULT_TITLE);
            mDescriptionView.setText(DEFAULT_DESCRIPTION);
            mEffortCheckBox.setChecked(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.workout_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.delete_button:
            {
                WorkoutHelper.get(WorkoutDetailsActivity.this).deleteWorkout(mWorkout);
                finish();
                return true;
            }

            default:
            {
                return super.onOptionsItemSelected(menuItem);
            }
        }
    }
}