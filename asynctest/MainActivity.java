package com.example.asynctest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/* package com.sibear.tcpc; */


public class MainActivity extends AppCompatActivity {
    public static String SERVERIP = "109.195.114.183"; //your comp
    public static int SERVERPORT = 443;
    public static int EXECTIMER = 0;
    public String ipad = null;
    public static int ij = 0;
    //Worker id
    public UUID wid;
    //savedinstance
    public static boolean isBtn2Show= true;

    final static String textViewTexKey = "TEXTVIEW_TEXT";
    // имя файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_COUNTER = "counter";
    public static final String APP_PREFERENCES_HOUR = "hour";
    private SharedPreferences mSettings;
    private static int SisBtn2Show=0;
    private static int SisHour=0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
           final Button submit = findViewById(R.id.btn1);
          // final TextView output = findViewById(R.id.out);
           final EditText ips = findViewById(R.id.edittxt1);
           final EditText ps = findViewById(R.id.edittxt2);
           final EditText timer =findViewById(R.id.editTN1);
            //Считать данные из файла
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCES_COUNTER)) {
            SisBtn2Show = mSettings.getInt(APP_PREFERENCES_COUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_HOUR)) {
            SisHour = mSettings.getInt(APP_PREFERENCES_HOUR, 0);
            timer.setText(String.valueOf(SisHour));
        }
            //
        Button BtnStartService = findViewById(R.id.btn2);
        Button BtnStopService = findViewById(R.id.btn3);
           if (SisBtn2Show==0) {
               BtnStartService.setEnabled(true);
               BtnStopService.setEnabled(false);
               showToast("Добро пожаловать!");
           }
           else {
               TextView workstate = findViewById(R.id.textView4);
               workstate.setText("Служба проверки запущена!");
               BtnStartService.setEnabled(false);
               BtnStopService.setEnabled(true);
           }
//if (isBtn2Show) {showToast("TRUE");} else {showToast("FALSE");}
           submit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                 SERVERIP = ips.getText().toString();
                 SERVERPORT = Integer.parseInt(ps.getText().toString());
                   submit.setEnabled(false);
                  //
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           if (Tools.isHostReachable(SERVERIP,SERVERPORT,2000))
                           {
                               showToast("Сервер доступен!");
                               ij++;
                               issueNotification("проверка доступности сервера", "Сервер доступен!", ij);
                             //  k = 1;
                           } else {
                               showToast("Сервер НЕ доступен!");
                               ij++;
                               issueNotification("проверка доступности сервера", "Сервер НЕ доступен!", ij);
                           }
                       }

                    }).start();
                   submit.setEnabled(true);
                  //
               }
           });
       }


  public void showToast(final String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, text,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

//Нажатие "Запустить"
    public void OnClickBtn2(View view) {
        SisBtn2Show=1;
        final EditText timer =findViewById(R.id.editTN1);
        EXECTIMER = Integer.parseInt(timer.getText().toString());
        //create a Data object to pass to the work
        final String workTag = "SrvService";
        WorkManager workManager = WorkManager.getInstance(getApplicationContext());
        WorkRequest request = new PeriodicWorkRequest
                //.Builder(MyWorker.class, 15, TimeUnit.MINUTES)
                .Builder(MyWorker.class, EXECTIMER, TimeUnit.HOURS)
                //  .setInputData(string)
                //  .setConstraints(constraints)
                .addTag(workTag)
                .build();
        workManager.enqueue(request);
        wid = request.getId();
        //
        /*
        LiveData<WorkInfo> status = workManager.getWorkInfoByIdLiveData(request.getId());
        status.observe(this, new Observer() {
            @Override
            public void update(Observable observable, Object o) {
                TextView workstate = findViewById(R.id.textView4);
                workstate.setText(value);
            }


        });
        */

        /*
        final String workTag = "SrvService";
        //Data string = new Data.Builder().putString(tag, "tag").build();
        PeriodicWorkRequest work = new PeriodicWorkRequest
                .Builder(MyWorker.class, EXECTIMER, TimeUnit.HOURS)
              //  .setInputData(string)
                //       .setConstraints(constraints)
                .addTag(workTag)
                .build();
                WorkManager.getInstance(this).enqueue(work);

         */
        Button BtnStartService = findViewById(R.id.btn2);
        BtnStartService.setEnabled(false);
        isBtn2Show = false;
        Button BtnStopService = findViewById(R.id.btn3);
        BtnStopService.setEnabled(true);
        TextView workstate = findViewById(R.id.textView4);
        workstate.setText("Служба проверки запущена!");
/*
        //
        final EditText timer =findViewById(R.id.editTN1);
        EXECTIMER = Integer.parseInt(timer.getText().toString())*1200000;
        //showToast(String.valueOf(EXECTIMER));


        Button BtnStartService = findViewById(R.id.btn2);
        BtnStartService.setEnabled(false);

        // Create PendingIntent
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent j=new Intent(this, MyService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(j);
        }
        else {
            startService(j);
        }

        //startService(new Intent(this, MyService.class));
        Log.e("!!!@#!!!", "-=-=-=-=-=-=-=-=-=-=-=ZAPUSK=-=-=-=-=-=-=-=-=-");
        Button BtnStopService = findViewById(R.id.btn3);
        BtnStopService.setEnabled(true);

        */
    }

//Нажатие "Остановить"
    public void OnClickBtn3(View view) {
        isBtn2Show = true;
        SisBtn2Show=0;
        WorkManager.getInstance(this).cancelAllWorkByTag("SrvService");
        Button BtnStartService = findViewById(R.id.btn2);
        BtnStartService.setEnabled(true);
        Button BtnStopService = findViewById(R.id.btn3);
        BtnStopService.setEnabled(false);
        TextView workstate = findViewById(R.id.textView4);
        workstate.setText("Запуск проверки в фоновом режиме");
/*
        //showToast("Программа будет свернута. Проверка будет производиться в фоновом режиме!");
        Intent j=new Intent(this, MyService.class);
        stopService(j);
        //startService(new Intent(this, MyService.class));
        Log.e("!!!@#!!!", "-=-=-=-=-=-=-=-=-=-=-=STOP=-=-=-=-=-=-=-=-=-");
        Button BtnStartService = findViewById(R.id.btn2);
        BtnStartService.setEnabled(true);
        Button BtnStopService = findViewById(R.id.btn3);
        BtnStopService.setEnabled(false);
*/
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
            makeNotificationChannel("CHANNEL_1", "Device channel", NotificationManager.IMPORTANCE_DEFAULT);
        }
        // the check ensures that the channel will only be made
        // if the device is running Android 8+

        NotificationCompat.Builder notification =
//***
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //    new NotificationCompat.Builder(this, "CHANNEL_1");
        //}
        //else {
            new NotificationCompat.Builder(this, "CHANNEL_1");
        //}
//***
        // the second parameter is the channel id.
        // it should be the same as passed to the makeNotificationChannel() method

        notification
                .setSmallIcon(R.mipmap.ic_launcher_micro)
                .setContentTitle(notiftext)
                .setContentText(text)
                .setContentIntent(resultPendingIntent)
                .setNumber(33)
                //.setContentInfo(text)
                //.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
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

//создание канала notification
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

//закрытие worker по ID
int b=0;
        public void OnClickClose(View view) {

        b++;
            if(b == 10) {
                WorkManager.getInstance(getApplicationContext()).cancelWorkById(wid);
                showToast("Работа планировщика остановлена!");
                b=0;
            }
        }

//проверка состояния worker
        public void OnClickshow(UUID id) {
            WorkManager.getInstance(this).cancelWorkById(id);
        }




    @Override
    protected void onSaveInstanceState(Bundle outState) {
     //  if (isBtn2Show==true) showToast("СОХРАНЕНИЕ! TRUE");
        //boolean myBoolean = isBtn2Show;
        outState.putInt("is_button_showed", SisBtn2Show);
        //outState.putBoolean("is_button_showed", isBtn2Show);
        TextView workstate = findViewById(R.id.textView4);
       outState.putString(textViewTexKey,workstate.getText().toString() );
        //outState.putString(textViewTexKey,workstate.getText().toString() );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //boolean myBoolean = savedInstanceState.getBoolean("is_button_showed");
        //isBtn2Show = savedInstanceState.getBoolean("is_button_showed");
        SisBtn2Show = savedInstanceState.getInt("is_button_showed");
        //if (savedInstanceState.getBoolean("is_button_showed", true))
        Button BtnStartService = findViewById(R.id.btn2);
        Button BtnStopService = findViewById(R.id.btn3);
       /*
        if (!isBtn2Show)
        {
            BtnStartService.setEnabled(false);
        }
        else {
            BtnStartService.setEnabled(true);
            BtnStopService.setEnabled(false);
        }
       */
            //
            String textViewText= savedInstanceState.getString(textViewTexKey);
            TextView workstate = findViewById(R.id.textView4);
            workstate.setText(textViewText);
            //
    }

    @Override
    protected void onPause() {
        super.onPause();
        final EditText timer =findViewById(R.id.editTN1);
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_COUNTER, SisBtn2Show);
        editor.putInt(APP_PREFERENCES_HOUR, Integer.parseInt(timer.getText().toString()));
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mSettings.contains(APP_PREFERENCES_COUNTER)) {
            // Получаем число из настроек
            SisBtn2Show = mSettings.getInt(APP_PREFERENCES_COUNTER, 0);

            // Выводим на экран данные из настроек
            //mInfoTextView.setText("Я насчитал "+ mCounter + " ворон");
            final EditText timer = findViewById(R.id.editTN1);
            SisHour = mSettings.getInt(APP_PREFERENCES_HOUR, 0);
            timer.setText(String.valueOf(SisHour));
            //        outState.putInt("is_hours_showed", Integer.parseInt(timer.getText().toString()));
        }
    }

}

/*
//
                   mThr.start();
                   try {
                       mThr.join();
                       mThr.sleep(2000);
                       submit.setEnabled(true);
                       //output.setText("Сервер "+ SERVERIP +"Доступен!");

                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
//

//
Thread mThr = new Thread(new Runnable() {
        private volatile int k;
        public synchronized int getValue() {
            return k;
        }
        @Override
            public void run() {
                showToast("Поток запущен!");

      //      if (isHostReachable(SERVERIP,SERVERPORT,2000))
          if (Tools.isHostReachable(SERVERIP,SERVERPORT,2000))
            {
                showToast("Сервер доступен!");
                k = 1;

            } else {
                showToast("Сервер НЕ доступен!");
            }



        }
                });
//

        //****************Service************************
        //
        //String CHANNEL_ID = "";
        //int NOTIFY_ID = 0;
        //CHANNEL_ID = "Test";NOTIFY_ID++;
        //Intent notificationIntent = new Intent(MainActivity.this, MainActivity.class);
        //AtomicReference<PendingIntent> contentIntent = new AtomicReference<>(PendingIntent.getActivity(MainActivity.this,
        //       0, notificationIntent,
        //       PendingIntent.FLAG_CANCEL_CURRENT));

        // NotificationCompat.Builder builder =
        //         new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
        //                 .setSmallIcon(R.mipmap.ic_launcher)
        //                 .setContentTitle("Напоминание")
        //                 .setContentText("Пора покормить кота")
        //                 .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        //                 .setContentIntent(contentIntent.get());

        //NotificationManagerCompat notificationManager =
        //        NotificationManagerCompat.from(MainActivity.this);
        //notificationManager.notify(NOTIFY_ID, builder.build());
//
        //getApplicationContext().startService(new Intent(getApplicationContext(), MyService.class));



 */