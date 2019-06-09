package com.mcc.restaurant.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mcc.restaurant.R;
import com.mcc.restaurant.activity.FoodDetailsActivity;
import com.mcc.restaurant.activity.MainActivity;
import com.mcc.restaurant.data.constant.AppConstants;
import com.mcc.restaurant.data.preference.AppPreference;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> params = remoteMessage.getData();

            if (AppPreference.getInstance(MyFirebaseMessagingService.this).isNotificationOn()) {

                sendNotification(params.get("title"), params.get("message"), params.get("id"));
                broadcastNewNotification();
            }
        }
    }

    private void sendNotification(String title, String messageBody, String postId) {



        int id = -1;
        try {
            id = Integer.parseInt(postId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent;
        if (id != -1) {
            intent = new Intent(this, FoodDetailsActivity.class);
            intent.putExtra(AppConstants.FOOD_TITLE, title);
            intent.putExtra(AppConstants.FOOD_ID, id);
            intent.putExtra(AppConstants.BUNDLE_FROM_PUSH, true);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000})
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

   private void broadcastNewNotification() {
        Intent intent = new Intent(AppConstants.NEW_NOTI);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
