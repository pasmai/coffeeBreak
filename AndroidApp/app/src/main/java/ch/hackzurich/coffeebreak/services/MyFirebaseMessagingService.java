package ch.hackzurich.coffeebreak.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import ch.hackzurich.coffeebreak.Config;
import ch.hackzurich.coffeebreak.InvitationReceivedActivity;
import ch.hackzurich.coffeebreak.R;

import static com.google.firebase.messaging.Constants.MessagePayloadKeys.SENDER_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * Message:
     * url          video url
     * timestamp    Date as long timestamp
     * target_topic topic
     */

    private static final String TAG = "FirebaseMessage";
    public static final AtomicInteger MSG_ID = new AtomicInteger();
    private String CHANNEL_ID = "12";

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String notificationTitle = null, notificationBody = null;

        // Check if message contains a notification payload
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification: " + remoteMessage.getNotification().getTitle() +": " +remoteMessage.getNotification().getBody());
        }

        sendPushNotification(remoteMessage);
    }

    /**
     * upon receiving a message, it will extract the coffee break information and turn it into a push notification
     * Upon clicking it, the user will be taken to the InvitationReceivedActivity.
     */
    private void sendPushNotification(RemoteMessage remoteMessage) {

        // see reference: https://medium.com/firebase-developers/mobile-app-push-notification-with-firebase-cloud-functions-and-realtime-database-194a82e43ba
        String notificationTitle = remoteMessage.getNotification().getTitle();
        String notificationBody = remoteMessage.getNotification().getBody();

        // extract Data from message
        Map<String, String> messageData = remoteMessage.getData();
        String url = messageData.get("url");
        String l = messageData.get("timestamp");
        if (l == null) {
            Log.d(TAG, "Received timestamp null");
            return;
        }
        if (url == null || url.isEmpty()){
            Log.d(TAG, "Received URL null");
            return;
        }

        Long timestamp = Long.parseLong(l);
        Log.d(TAG, "Message parsed: timestampe=" +timestamp+" url="+url+" senderId="+remoteMessage.getSenderId() +" data="+remoteMessage.getData());



        // send Notification that takes user to the AcceptActivity
        Intent intent = new Intent(this, InvitationReceivedActivity.class);
        intent.putExtra(Config.video_url_identifier, url);
        intent.putExtra(Config.break_time_identifier, timestamp);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        createNotificationChannel();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)   // Automatically delete the notification whe user taps it
                .setContentIntent(pendingIntent)
                .setContentTitle("Do you want to take a coffee break?")
                .setContentText(notificationBody);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1234, notificationBuilder.build());
    }

    public static void sendNotificationToServer(String url, Date date){
        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        RemoteMessage msg = new RemoteMessage.Builder(SENDER_ID + email)
                .setMessageId(Integer.toString(MyFirebaseMessagingService.MSG_ID.incrementAndGet()))
                .addData("url", url)
                .addData("timestamp", Long.toString(date.getTime()))
                .addData("target_topic", "all")
                .build();
        fm.send(msg);

        Log.d(TAG, "Sent message: " + msg.toString());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "This is a test channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
