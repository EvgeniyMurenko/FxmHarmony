package com.sofac.fxmharmony.util.googleFirebaseService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Html;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sofac.fxmharmony.Constants;
import com.sofac.fxmharmony.R;
import com.sofac.fxmharmony.data.dto.PushMessage;
import com.sofac.fxmharmony.view.SplashActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

import static com.sofac.fxmharmony.Constants.APP_PREFERENCES;
import static com.sofac.fxmharmony.Constants.PUSH_MASSEGES;


public class MyFirebaseMessagingService extends FirebaseMessagingService {


    NotificationCompat.Builder builder;
    NotificationManager mNotificationManager;
    String pushMessageType = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Timber.e("From: " + remoteMessage.getFrom());

         pushMessageType = remoteMessage.getData().get("type");

        if (pushMessageType.equals(Constants.GROUP_PUSH_TYPE)){
            buildNotificationToShow(remoteMessage.getData().get("message"), remoteMessage.getData().get("date"), remoteMessage.getData().get("title"));
        } else {

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Timber.e("Message data payload: " + remoteMessage.getData());
                //SharedPreferences preferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
                buildNotificationToShow(remoteMessage.getData().get("message"), remoteMessage.getData().get("date"), remoteMessage.getData().get("title"));

                PushMessage newPushMessage = new PushMessage(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), remoteMessage.getData().get("date"));
                newPushMessage.save();

                //List<PushMessage> pushMessages = new ArrayList<PushMessage>();

//            String stringPushPreferences = preferences.getString(PUSH_MASSEGES, "");
//            if (stringPushPreferences.length() > 4) {
//                pushMessages = new Gson().fromJson(stringPushPreferences, new TypeToken<List<PushMessage>>() {
//                }.getType());
//            }
//
//            pushMessages.add(newPushMessage);
//
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putString(PUSH_MASSEGES, new Gson().toJson(pushMessages));
//            editor.apply();
//            editor.commit();
            }

            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Timber.i("Message Notification Body: " + remoteMessage.getNotification().getBody());
            }

            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
        }
    }


    private void buildNotificationToShow(String messageText, String date, String title) {


        Intent notificationIntent = null;
        notificationIntent = new Intent(this, SplashActivity.class);

        mNotificationManager = (NotificationManager) this
                .getSystemService(this.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(Html.fromHtml(messageText).toString())
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