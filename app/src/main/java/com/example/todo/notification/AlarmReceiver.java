package com.example.todo.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.todo.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AlarmReceiver", "onReceive called"); // Add this line
        String taskTitle = intent.getStringExtra("task");
        int taskId = intent.getIntExtra("task_id",0);

        Log.d("AlarmReceiver", "Task ID: " + taskId);
        Log.d("AlarmReceiver", "Task Title: " + taskTitle);

        // to et the sound
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Build the Notifiation
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "todo_channel")
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle("Task Reinder")
                .setContentText("Don' t forget : "  + taskTitle)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(soundUri)
                .setAutoCancel(true);


        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(manager != null){
            manager.notify(taskId, builder.build());
            Log.d("AlarmReceiver", "Notification displayed");
        }else{
            Log.e("AlarmReceiver", "NotificationManager is null");
        }

    }
}
