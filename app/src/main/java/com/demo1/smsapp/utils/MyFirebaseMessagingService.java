package com.demo1.smsapp.utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.os.Handler;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo1.smsapp.R;
import com.demo1.smsapp.activity.HomeActivity;
import com.demo1.smsapp.activity.LoginActivity;
import com.demo1.smsapp.activity.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        System.out.println("From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        sendNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
    }


    public void sendNotification(String messageBody,String tile) {
        SharedPreferences sharedPreferences = getSharedPreferences("informationAccount", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        PendingIntent pendingIntent = null;
        if (token == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_IMMUTABLE);
        } else {
            String newToken = token.substring(7,token.length());
            DecodedJWT jwt = JWT.decode(newToken);
            if (jwt.getExpiresAt().before(new Date())) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_IMMUTABLE);
            }else {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_IMMUTABLE);
            }
        }

    String channelId = "fcm_default_channel";
    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder =
            new NotificationCompat.Builder(this, channelId)
                    .setContentTitle(tile)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.hkt_logo)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

    NotificationManager notificationManager =
            null;
        if(android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.M)

    {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    // Since android Oreo notification channel is needed.
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O)

    {
        NotificationChannel channel = new NotificationChannel(channelId,
                "HTK_APP",
                NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
    }
        if(notificationManager !=null)

    {
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}


}
