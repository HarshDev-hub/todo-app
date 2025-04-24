package com.example.todo.notification;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.todo.model.Task;

public class NotificationHelper {
    @SuppressLint("ScheduleExactAlarm")
    public static void scheduleNotification(Context context, Task task){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("task",task.getTask());
        intent.putExtra("task_id",task.getTaskId());

        //unique PendingIntenet for each task (use task_id)
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                task.getTaskId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        //schedule the alarm
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    task.getDueDate(),
                    pendingIntent
            );
        }else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,task.getDueDate(), pendingIntent);

        }
    }

    //create a notification channel (call this in mainactivity)
    public static void createNotificationChannel(Context context){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            android.app.NotificationChannel channel = new android.app.NotificationChannel(
                  "todo_channel",
                  "Task Reminders",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Reminders for your tasks");
            android.app.NotificationManager manager =  context.getSystemService(android.app.NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
