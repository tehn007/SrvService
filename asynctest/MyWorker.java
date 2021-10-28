package com.example.asynctest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static androidx.core.content.ContextCompat.getSystemService;


public class MyWorker extends Worker {
    public MyWorker(Context context, WorkerParameters wp)
    {
        super(context, wp);

    }

    @NonNull
    @Override
    public Result doWork() {
        //Do some work here.
        try {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Tools.isHostReachable(MainActivity.SERVERIP,MainActivity.SERVERPORT,2000))
                {

                    //MainActivity.showToast("Сервер доступен!");
                    MainActivity.ij++;
                    issueNotification("проверка доступности сервера", "Сервер доступен!", MainActivity.ij);


                    //  k = 1;

                } else {
                    //MainActivity.showToast("Сервер НЕ доступен!");
                    MainActivity.ij++;
                    issueNotification("проверка доступности сервера", "Сервер НЕ доступен!", MainActivity.ij);

                }
            }

        }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }

        //You can return Result.failure() to indicate work failed
        // or Result.retry() to retry the work.
        return Result.success();        //You can also pass a Data object to send results (10KB max)
        // Data data = new Data.Builder().putString(tag,"").build();
        // return Result.success(data);
    }


    /*
    //create constraints for the work
    Constraints constraints = new Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build();

    //create the actual WorkRequest calling the Worker above
    OneTimeWorkRequest work = new OneTimeWorkRequest
            .Builder(MyWorker.class)
            .setInitialDelay(1000, TimeUnit.MILLISECONDS)
            .setInputData(string)
            .setConstraints(constraints)
            .addTag("workTag")
            .build();

    */

    public void onStarted (){


        //Data string = new Data.Builder().putString(tag, "tag").build();


    }


    public void onStopped(UUID wid) {
        super.onStopped();

        WorkManager.getInstance(getApplicationContext()).cancelWorkById(wid);

    }
/*
    {
        PeriodicWorkRequest work = new PeriodicWorkRequest
                .Builder(MyWorker.class, EXECTIMER, TimeUnit.HOURS)
                //  .setInputData(string)
                //       .setConstraints(constraints)
                .addTag(workTag)
                .build();
        WorkManager.getInstance(this).enqueue(work);

    }
 */
    //test_notification
    public void issueNotification(final String notiftext, final String text, int i)
    {

        // Create PendingIntent
        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        // make the channel. The method has been discussed before.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHANNEL_1", "Example channel", NotificationManager.IMPORTANCE_DEFAULT);
        }
        // the check ensures that the channel will only be made
        // if the device is running Android 8+

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(getApplicationContext(), "CHANNEL_1");
        // the second parameter is the channel id.
        // it should be the same as passed to the makeNotificationChannel() method

        notification
                .setSmallIcon(R.mipmap.ic_launcher_micro)
                .setContentTitle(notiftext)
                .setContentText(text)
                .setContentIntent(resultPendingIntent)
                .setNumber(33)
                //.setContentInfo(text)
                //     .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                //.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setShowWhen(true)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true);


        Context context=getApplicationContext();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());
        // it is better to not use 0 as notification id, so used 1.
    }
//test_end


    @RequiresApi(api = Build.VERSION_CODES.O)
    void makeNotificationChannel(String id, String name, int importance)
    {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true); // set false to disable badges, Oreo exclusive

        Context context=getApplicationContext();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }


}

