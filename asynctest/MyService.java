package com.example.asynctest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.location.SettingInjectorService;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class MyService extends Service {


    public static int notify = 300000; //interval between two services(Here Service run every 5 Minute)
    private Handler mHandler = new Handler();
    private Timer mTimer = null; //timer handling
    private int i = 0;
    private int q = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        //int q = MainActivity.EXECTIMER;
        //notify = MainActivity.EXECTIMER;//ВРЕМЯ МЕЖДУ ЗАПУСКАМИ ПРОВЕРКИ
        //Toast.makeText(this," Q ".concat(String.valueOf(notify)), Toast.LENGTH_SHORT).show();
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else mTimer = new Timer(); //recreate
        mTimer.schedule(new TimeDisplay(), 0, notify);
   }

      public int onStartCommand(Intent intent, int flags, int startId) {
          Toast.makeText(this, "Service is Started", Toast.LENGTH_SHORT).show();
        try {
          TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              makeNotificationChannel("CHANNEL_1", "Example channel", NotificationManager.IMPORTANCE_DEFAULT);
              Notification.Builder builder = new Notification.Builder(this, "CHANNEL_1")
                      .setContentTitle(getString(R.string.app_name))
                      .setContentText("TestService!")
                      .setAutoCancel(true);

              Notification notification = builder.build();


              startForeground(1, notification);
          }
        //
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
       super.onDestroy();
        mTimer.cancel(); //For Cancel Timer
        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();


    }

    public void runtest() {


    }

    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            mHandler.post (new Runnable() {
                @Override
                public void run() {
                    //sendNotif("Таймер работает!");
                    //   Toast.makeText(MyService.this, "Service is running", Toast.LENGTH_SHORT).show();
 //Запуск утилиты проверки доступности сервера
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //      if (isHostReachable(SERVERIP,SERVERPORT,2000))
                            if (Tools.isHostReachable(MainActivity.SERVERIP,MainActivity.SERVERPORT,2000))
                            {
                                //showToast("Сервер доступен!");
                                //k = 1;
                                issueNotification("проверка доступности сервера", "Сервер доступен!", 1);
                            } else {
                                //showToast("Сервер НЕ доступен!");
                                issueNotification("проверка доступности сервера", "Сервер НЕДОСТУПЕН!", 1);
                            }
                        }

                    }).start();
//
                } });
        }
    }

    //test_notification
    public void issueNotification(final String notiftext, final String text, int i)
    {
        // Create PendingIntent
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
        PendingIntent.FLAG_UPDATE_CURRENT);
        // make the channel. The method has been discussed before.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        makeNotificationChannel("CHANNEL_1", "Example channel", NotificationManager.IMPORTANCE_DEFAULT);
        }
        // the check ensures that the channel will only be made
        // if the device is running Android 8+

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, "CHANNEL_1");
        // the second parameter is the channel id.
        // it should be the same as passed to the makeNotificationChannel() method

        notification
                .setSmallIcon(R.mipmap.ic_launcher_micro)
                .setColorized(true)// Включить цвет фона уведомления
                .setColor(Color.RED)// Устанавливаем цвет фона уведомления
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

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

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

    NotificationManager notificationManager =
            (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

    assert notificationManager != null;
    notificationManager.createNotificationChannel(channel);
}


}

 /*   Thread mThr = new Thread(new Runnable() {
        private volatile int k;
        public synchronized int getValue() {
            return k;
        }
        @Override
        public void run() {
            //      if (isHostReachable(SERVERIP,SERVERPORT,2000))
            if (Tools.isHostReachable(MainActivity.SERVERIP,MainActivity.SERVERPORT,2000))
            {
                //showToast("Сервер доступен!");
                k = 1;
                sendNotif("Сервер доступен!");
            } else {
                //showToast("Сервер НЕ доступен!");
                sendNotif("Сервер НЕ доступен!");
            }

        }
    });

  */
/*
                    mThr.start();
                    try {
                        mThr.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

  */
