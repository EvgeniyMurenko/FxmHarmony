package com.sofac.fxmharmony.util.googleFirebaseService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;




import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.view.MainActivity;

import timber.log.Timber;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    NotificationCompat.Builder builder;
    NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Timber.i("From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Timber.i("Message data payload: " + remoteMessage.getData());
            buildNotificationToShow(remoteMessage.getData().get("message"));

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Timber.i("Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    private void buildNotificationToShow(String messageText) {


        Intent notificationIntent = null;
        notificationIntent = new Intent(this, MainActivity.class);

        mNotificationManager = (NotificationManager) this
                .getSystemService(this.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(messageText)
                .setSmallIcon(R.drawable.icon)
              /*  .setStyle(bigPictureStyle)*/
                .setAutoCancel(true)
                .setContentIntent(
                        PendingIntent.getActivity(this, 10,
                                notificationIntent, 0));

        Uri alarmSound = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);

        mNotificationManager.notify((int) (Math.random() * 100000), builder.build());

    }

}