package com.jeff.workouttracker;

// Import packages
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Main activity for this application
 *
 * @author Jeffrey Trotz
 * Date: 3/27/18
 * Class: CS 305
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity
{
    // Declare fields used in this class
    private RecyclerView mWorkoutRecyclerView;
    private WorkoutAdapter mWorkoutAdapter;
    private FloatingActionButton mAddButton;

    /**
     * Updates the user interface when there's a change or the main activity is reloaded
     */
    private void updateUI()
    {
        WorkoutHelper workoutHelper = WorkoutHelper.get(MainActivity.this);
        List<Workout> workouts = workoutHelper.getWorkouts();

        // If the WorkoutAdapter object doesn't exist, make a new one
        if (mWorkoutAdapter == null)
        {
            mWorkoutAdapter = new WorkoutAdapter(workouts);
            mWorkoutRecyclerView.setAdapter(mWorkoutAdapter);
        }

        // If the WorkoutAdapter object does exist, set the new data
        else
        {
            mWorkoutAdapter.setWorkouts(workouts);
            mWorkoutAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_main);
        mWorkoutRecyclerView = findViewById(R.id.workout_recycler_view);
        mAddButton = findViewById(R.id.add_button);
        mWorkoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Listener for the floating action button to add a new workout
        mAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View addButton)
            {
                // Creates a new workout object and starts the workout details activity
                Workout workout = new Workout();
                WorkoutHelper.get(MainActivity.this).addWorkout(workout);
                Intent intent = DetailsActivity.newIntent(MainActivity.this, workout.getUUID());
                startActivity(intent);
            }
        });

        // Update the UI after adding a workout
        updateUI();
    }

    /**
     * Updates the UI when the main activity is resumed
     */
    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    /**
     * Holder class for the Recycler view
     */
    private class WorkoutHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        // Declare fields used in this class
        private Workout mWorkout;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mEffortImageView;

        /**
         * Instantiates the layout XML file into its corresponding view objects
         * @param layoutInflater LayoutInflater object
         * @param parent Parent View
         */
        public WorkoutHolder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.list_workouts, parent, false));
            mTitleTextView = itemView.findViewById(R.id.workout_title);
            mDateTextView = itemView.findViewById(R.id.workout_date);
            mEffortImageView = itemView.findViewById(R.id.effort_indicator);
            itemView.setOnClickListener(this);
        }

        /**
         * Sets the information for each view in the list
         * @param workout Workout object
         */
        public void bind(Workout workout)
        {
            mWorkout = workout;
            mTitleTextView.setText(workout.getTitle());
            mDateTextView.setText(mWorkout.getWorkoutTime());
            mEffortImageView.setVisibility(workout.getEffort() ? View.VISIBLE : View.GONE);
        }

        /**
         * Click listener for each item in the Recycler view. Launches the details activity
         * @param listItem Item in the list that was selected
         */
        @Override
        public void onClick(View listItem)
        {
            Intent intent = DetailsActivity.newIntent(MainActivity.this, mWorkout.getUUID());
            startActivity(intent);
        }
    }

    /**
     * Adapter class for the Recycler View
     */
    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutHolder>
    {
        // List of workouts
        private List<Workout> mWorkouts;

        /**
         * Constructor
         * @param workouts List of workouts
         */
        public WorkoutAdapter(List<Workout> workouts)
        {
            mWorkouts = workouts;
        }

        /**
         * Setter for workouts
         * @param workouts
         */
        public void setWorkouts(List<Workout> workouts)
        {
            mWorkouts = workouts;
        }

        /**
         * Creates the LayoutInflater object
         * @param parent Parent view
         * @param viewType What kind of view it is
         * @return Returns a WorkoutHolder object
         */
        @Override
        public WorkoutHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new WorkoutHolder(layoutInflater, parent);
        }

        /**
         * Calls the bind() method for each workout object
         * @param workoutHolder Holder object to instantiate the layout
         * @param position Position in the list the object is located at
         */
        @Override
        public void onBindViewHolder(WorkoutHolder workoutHolder, int position)
        {
            Workout workout = mWorkouts.get(position);
            workoutHolder.bind(workout);
        }

        /**
         * Getter for workout list item count
         * @return Returns the number of items in mWorkouts as an integer
         */
        @Override
        public int getItemCount()
        {
            return mWorkouts.size();
        }
    }
}