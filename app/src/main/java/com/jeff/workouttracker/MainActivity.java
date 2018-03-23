package com.jeff.workouttracker;

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

public class MainActivity extends AppCompatActivity
{
    private RecyclerView mWorkoutRecyclerView;
    private WorkoutAdapter mWorkoutAdapter;
    private FloatingActionButton mAddButton;

    private void updateUI()
    {
        WorkoutHelper workoutHelper = WorkoutHelper.get(MainActivity.this);
        List<Workout> workouts = workoutHelper.getWorkouts();

        if (mWorkoutAdapter == null)
        {
            mWorkoutAdapter = new WorkoutAdapter(workouts);
            mWorkoutRecyclerView.setAdapter(mWorkoutAdapter);
        }

        else
        {
            mWorkoutAdapter.setWorkouts(workouts);
            mWorkoutAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWorkoutRecyclerView = findViewById(R.id.workout_recycler_view);
        mWorkoutRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAddButton = findViewById(R.id.add_button);

        mAddButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Workout workout = new Workout();
                WorkoutHelper.get(MainActivity.this).addWorkout(workout);
                Intent intent = WorkoutDetailsActivity.newIntent(MainActivity.this, workout.getUUID());
                startActivity(intent);
            }
        });

        updateUI();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    private class WorkoutHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Workout mWorkout;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mEffortImageView;

        public WorkoutHolder(LayoutInflater layoutInflater, ViewGroup parent)
        {
            super(layoutInflater.inflate(R.layout.list_workouts, parent, false));
            mTitleTextView = itemView.findViewById(R.id.workout_title);
            mDateTextView = itemView.findViewById(R.id.workout_date);
            mEffortImageView = itemView.findViewById(R.id.effort_indicator);
            itemView.setOnClickListener(this);
        }

        public void bind(Workout workout)
        {
            mWorkout = workout;
            mTitleTextView.setText(workout.getTitle());
            mDateTextView.setText(workout.getDate().toString());
            mEffortImageView.setVisibility(workout.getEffort() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v)
        {
            Intent intent = WorkoutDetailsActivity.newIntent(MainActivity.this, mWorkout.getUUID());
            startActivity(intent);
        }
    }

    private class WorkoutAdapter extends RecyclerView.Adapter<WorkoutHolder>
    {
        private List<Workout> mWorkouts;

        public WorkoutAdapter(List<Workout> workouts)
        {
            mWorkouts = workouts;
        }

        public void setWorkouts(List<Workout> workouts)
        {
            mWorkouts = workouts;
        }

        @Override
        public WorkoutHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new WorkoutHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(WorkoutHolder workoutHolder, int position)
        {
            Workout workout = mWorkouts.get(position);
            workoutHolder.bind(workout);
        }

        @Override
        public int getItemCount()
        {
            return mWorkouts.size();
        }
    }
}