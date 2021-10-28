package com.example.asynctest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import static androidx.core.content.ContextCompat.getSystemService;

public class Tools {


    public static boolean isHostReachable(String serverAddress, int serverTCPport, int timeoutMS)
    {
        boolean connected = false;
        Socket socket;
        try
        { socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(serverAddress, serverTCPport);
            socket.connect(socketAddress, timeoutMS);
            if (socket.isConnected()) {
                connected = true; socket.close();

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            socket = null;
        }
        return connected;
    }



/*
            InetAddress serverAddi;
            Socket socket = null;
          //  Log.e("TCP Client", "C: Connecting...");
                    try{

                        serverAddi = InetAddress.getByName(SERVERIP);
                        socket = new Socket(serverAddi, SERVERPORT);
                        //socket.getOutputStream();
                        if (socket.connect(serverAddi,2000))
                                 //if (socket.isConnected())
                                 {
                                     boolean connected = true;
                                     socket.close();
                            showToast(s);
                        }
                        // final EditText hostname = findViewById(R.id.editText1);
                      //  Toast toast = Toast.makeText(getApplicationContext(),"Поток!",Toast.LENGTH_LONG);
                      //  toast.show();
                     //   hostname.setText((CharSequence) serverAddi);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            //

*/
}
