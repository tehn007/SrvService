package com.example.asynctest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Preview extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview);

        //init();
        StartMain ();
    }

//    private void init() {
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private void StartMain () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(Preview.this, MainActivity.class);
                startActivity(i);

            }
        }).start();
    }
}
