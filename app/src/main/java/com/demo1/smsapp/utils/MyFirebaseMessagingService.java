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
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo1.smsapp.R;
import com.demo1.smsapp.activity.HomeActivity;
import com.demo1.smsapp.activity.LoginActivity;
import com.demo1.smsapp.activity.SplashActivity;
import com.demo1.smsapp.api.ClassAPI;
import com.demo1.smsapp.api.ScheduleAPI;
import com.demo1.smsapp.api.StudentClassAPI;
import com.demo1.smsapp.api.utils.APIUtils;
import com.demo1.smsapp.dto.DataNotification;
import com.demo1.smsapp.dto.ResponseModel;
import com.demo1.smsapp.enums.ERole;
import com.demo1.smsapp.models.*;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private StudentClassAPI studentClassAPI = APIUtils.getStudentClass();
    private ScheduleAPI scheduleAPI = APIUtils.getScheduleAPI();
    private ClassAPI classAPI = APIUtils.getClasses();
    private Classses classses = new Classses();
    private Student student;
    private Gson gson = new Gson();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        System.out.println("From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("Message Notification Body: " + remoteMessage.getNotification().getBody());
            DataNotification data = gson.fromJson(remoteMessage.getNotification().getBody(),DataNotification.class);
            if(data.getAction().equals("Schedule")){
                sendNotificationSchedule(data.getContent(), remoteMessage.getNotification().getTitle());
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotificationSchedule(String messageBody, String tile) {
        SharedPreferences sharedPreferences = getSharedPreferences("informationAccount", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String role = sharedPreferences.getString("role", null);
        String data = sharedPreferences.getString("data", null);
        PendingIntent pendingIntent = null;
        LocalDate dateSend = LocalDate.parse(messageBody);
        if (role.equals(ERole.Student.toString())) {
            student = gson.fromJson(data,Student.class);
            if (token == null) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_IMMUTABLE);
            } else {
                String newToken = token.substring(7, token.length());
                DecodedJWT jwt = JWT.decode(newToken);
                if (jwt.getExpiresAt().before(new Date())) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                            PendingIntent.FLAG_IMMUTABLE);
                } else {
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                            PendingIntent.FLAG_IMMUTABLE);
                }
                PendingIntent finalPendingIntent = pendingIntent;
                studentClassAPI.getClassIdByStudentId(token, student.getId()).enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        String json = gson.toJson(response.body().getData());
                        Type studentClassType = new TypeToken<ArrayList<StudentClass>>() {
                        }.getType();
                        List<StudentClass> studentClasses = gson.fromJson(json, studentClassType);
                        for (StudentClass studentClass : studentClasses) {
                            classAPI.getClassById(token, studentClass.getClassId()).enqueue(new Callback<ResponseModel>() {
                                @Override
                                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                    String jsonClass = gson.toJson(response.body().getData());
//                                Type classType = new TypeToken<ResponseModel>(){}.getType();
                                    classses = gson.fromJson(jsonClass, Classses.class);
                                    scheduleAPI.getScheduleByClass(token, classses.getId()).enqueue(new Callback<ResponseModel>() {
                                        @Override
                                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                            if(response.isSuccessful()){
                                                String jsonResponse = gson.toJson(response.body().getData());
                                                Type scheduleType = new TypeToken<ArrayList<Schedule>>(){}.getType();
                                                List<Schedule> scheduleList = gson.fromJson(jsonResponse,scheduleType);
                                                String channelId = "fcm_default_channel";
                                                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                                for (Schedule schedule : scheduleList){
                                                    for (ScheduleDetail scheduleDetail : schedule.getScheduleDetailsById()){
                                                        if(LocalDate.parse(scheduleDetail.getDate()).equals(dateSend)){
                                                            StringBuilder message = new StringBuilder();
                                                            message.append("Môn: ").append(scheduleDetail.getSubjectBySubjectId().getSubjectName()).append("\n")
                                                                    .append("Vào lúc: ").append(ConvertDayOfWeek.convertShift(classses.getShift()));
                                                            NotificationCompat.Builder notificationBuilder =
                                                                    new NotificationCompat.Builder(getApplicationContext(), channelId)
                                                                            .setContentTitle(tile)
                                                                            .setContentText(message)
                                                                            .setAutoCancel(true)
                                                                            .setSmallIcon(R.drawable.hkt_logo)
                                                                            .setSound(defaultSoundUri)
                                                                            .setContentIntent(finalPendingIntent);

                                                            NotificationManager notificationManager =
                                                                    null;
                                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                                                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                            }

                                                            // Since android Oreo notification channel is needed.
                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                                NotificationChannel channel = new NotificationChannel(channelId,
                                                                        "HTK_APP",
                                                                        NotificationManager.IMPORTANCE_DEFAULT);
                                                                notificationManager.createNotificationChannel(channel);
                                                            }
                                                            if (notificationManager != null) {
                                                                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseModel> call, Throwable t) {

                                        }
                                    });
                                }
                                @Override
                                public void onFailure(Call<ResponseModel> call, Throwable t) {
                                    Log.e("msg", t.getMessage());
                                }
                            });
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {

                    }
                });

            }
        }
    }
}
