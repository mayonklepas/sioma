package com.meteorit.sioma;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Minami on 17/11/2017.
 */

public class Firebasemessagehandler extends FirebaseMessagingService {

    public static final String NOTIFICATION_CHANNEL_ID = "4655";
    public static final String NOTIFICATION_CHANNEL_NAME = "sioma";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size()>0){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.createNotificationChannel(notificationChannel);
            }
            Intent in=new Intent(this.getBaseContext(), InboxActivity.class);
            in.putExtra("pesan", remoteMessage.getData().get("tag"));
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pi=PendingIntent.getActivity(this, 555 , in,PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            //Uri defaultSoundUri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.ready);
            NotificationCompat.Builder nb = new NotificationCompat.Builder(this.getBaseContext(),NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.polrintb)
                    .setContentTitle(remoteMessage.getData().get("judul"))
                    .setContentText(remoteMessage.getData().get("pesan"))
                    .setSound(defaultSoundUri)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setContentIntent(pi)
                    .setAutoCancel(true);
            NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(555,nb.build());
        }else{
            if(remoteMessage.getNotification() != null){

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    nm.createNotificationChannel(notificationChannel);
                }
                Intent in=new Intent(this.getBaseContext(), InboxActivity.class);
                in.putExtra("pesan", remoteMessage.getNotification().getBody());
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pi=PendingIntent.getActivity(this, 555 , in,PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                //Uri defaultSoundUri=Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.ready);
                NotificationCompat.Builder nb = new NotificationCompat.Builder(this.getBaseContext(),NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.polrintb)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setSound(defaultSoundUri)
                        .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                        .setContentIntent(pi)
                        .setAutoCancel(true);
                NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(555,nb.build());

            }
        }



    }
}
