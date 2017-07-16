package com.capstone.jobapplication.jobbridge.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.capstone.jobapplication.jobbridge.JobApplicationDetailActivity;
import com.capstone.jobapplication.jobbridge.JobDetailActivity;
import com.capstone.jobapplication.jobbridge.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Sarah on 2017-06-13.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private static final String NEW_JOB = "jobId"; // new job notification
    private static final String JOB_APPLICATION_UPDATE = "jobApplicationId"; //job application status update notification

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());


        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage);
        }
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        Map<String,String> customData = remoteMessage.getData();
        String message = remoteMessage.getNotification().getBody();
        if(customData.size() > 0) {
            for (Map.Entry entry : customData.entrySet()) {
                String key = (String) entry.getKey();
                String id = (String) entry.getValue();
                switch (key) {
                    case NEW_JOB:
                        send(JobDetailActivity.class,message, "jobId",Integer.parseInt(id));
                        break;
                    case JOB_APPLICATION_UPDATE:
                        send(JobApplicationDetailActivity.class,message, "jobApplicationId",Integer.parseInt(id));
                        break;
                }
            }
        }
    }

    private void send(Class target,String message, String extraKey, int extraValue ) {

        Intent notificationIntent = new Intent(this, target);
        notificationIntent.putExtra(extraKey, extraValue);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);
    }
}
