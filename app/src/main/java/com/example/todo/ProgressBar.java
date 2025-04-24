package com.example.todo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.todo.model.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProgressBar extends AppCompatActivity {

    private AnyChartView anyChartView;
    private TaskViewModel taskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        // Initialize the AnyChartView and TaskViewModel
        anyChartView = findViewById(R.id.anychartview);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);


        // Observe total task count
        taskViewModel.getTotalTaskCount().observe(this, totalTask -> {
            Log.d("ProgressBar", "Total Task Count: " + totalTask); // Log statement to check the total task count

            if (totalTask != null && totalTask > 0) {
                // Observe completed task count
                taskViewModel.getCompletedTaskCount().observe(this, completedTasks -> {
                    Log.d("ProgressBar", "Completed Task Count: " + completedTasks); // Log statement to check completed task count

                    // Observe non-completed task count
                    taskViewModel.getNonCompletedTaskCount().observe(this, nonCompletedTasks -> {
                        // Calculate the percentages
                        float completedPercentage = (completedTasks != null ? completedTasks : 0) / (float) totalTask * 100;
                        float nonCompletedPercentage = (nonCompletedTasks != null ? nonCompletedTasks : 0) / (float) totalTask * 100;

//                        float completedPercentage = (completedTasks / (float) totalTask) * 100;
//                        float nonCompletedPercentage = (nonCompletedTasks / (float) totalTask) * 100;


                        Toast.makeText(this, "Completed: " + completedPercentage + "%, Non-Completed: " + nonCompletedPercentage + "%", Toast.LENGTH_SHORT).show();


                        // Update the chart
                        updateAnyChart(completedPercentage,nonCompletedPercentage);
                    });
                });
            } else {
                updateAnyChart(0, 0);
            }
        });

    }

    private void updateAnyChart(float completedPercentage, float nonCompletedPercentage) {
        // Create a Pie chart
        Pie pie = AnyChart.pie();

        // Create a list of DataEntry objects to pass to the pie chart
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Completed", completedPercentage));
        data.add(new ValueDataEntry("Non-Completed", nonCompletedPercentage));

        // Set data for the pie chart
        pie.data(data);

        // Set the pie chart to the AnyChartView
        anyChartView.setChart(pie);
    }
}


