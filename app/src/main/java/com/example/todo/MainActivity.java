package com.example.todo;

import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.adapter.OnTodoClickListener;
import com.example.todo.adapter.RecyclerViewAdapter;
import com.example.todo.model.SharedViewModel;
import com.example.todo.model.Task;
import com.example.todo.model.TaskViewModel;
import com.example.todo.notification.NotificationHelper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnTodoClickListener {
    private TaskViewModel taskViewModel; // Class-level variable
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;
    private Task task;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // use to stick with same colour
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        NotificationHelper.createNotificationChannel(this);
        //   Log.d("MainActivity", "Notification channel created");

        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize taskViewModel at the class level
        taskViewModel = new ViewModelProvider.AndroidViewModelFactory(
                MainActivity.this.getApplication())
                .create(TaskViewModel.class);

        sharedViewModel = new ViewModelProvider(this)
                .get(SharedViewModel.class);

        taskViewModel.getAllTasks().observe(this, tasks -> {
            recyclerViewAdapter = new RecyclerViewAdapter(tasks, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });

        taskViewModel.getTotalTaskCount().observe(this, totalTask -> {
            taskViewModel.getCompletedTaskCount();
                });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> showBottomSheetDialog());
    }


    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Get the SearchManager
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Observe filtered tasks based on search text
                taskViewModel.searchTasks(newText).observe(MainActivity.this, tasks -> recyclerViewAdapter.updateList(tasks));
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // Handle settings action
        } else if (id == R.id.action_reminder) {
            showTimePickerDialog();
        } else if (id == R.id.progress_bar) {

            Intent intent = new Intent(this, ProgressBar.class);
            startActivities(new Intent[]{intent});
        }
        return super.onOptionsItemSelected(item);
    }

    private void showTimePickerDialog() {
        if (task == null) {
            Toast.makeText(MainActivity.this, "Please select a task first", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                MainActivity.this,
                (view, hourOfDay, minuteOfHour) -> {
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar1.set(Calendar.MINUTE, minuteOfHour);

                    Date reminderDate = calendar1.getTime();

                    task.setDueDate(reminderDate);
                    NotificationHelper.scheduleNotification(MainActivity.this, task);
                    Toast.makeText(MainActivity.this, "Reminder set for " + hourOfDay + ":" + minuteOfHour, Toast.LENGTH_SHORT).show();
                },
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

    @Override
    public void onTodoClick(Task task) {
        this.task = task; // Set the task object here
        sharedViewModel.selectItem(task);
        sharedViewModel.setIsEdit(true);
        showBottomSheetDialog();
    }

    @Override
    public void onTodoRadioButtonClick(Task task) {

        taskViewModel.delete(task);
        recyclerViewAdapter.notifyDataSetChanged();


    }

    @Override
    public void onTodoCompleteClick(Task task) {
        task.setDone(!task.isDone());
        taskViewModel.update(task);
    }


}

