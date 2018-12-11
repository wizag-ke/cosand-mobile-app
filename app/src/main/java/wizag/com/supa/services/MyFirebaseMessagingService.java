package wizag.com.supa.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import wizag.com.supa.NotificationUtils;
import wizag.com.supa.R;
import wizag.com.supa.TestActivity;
import wizag.com.supa.activity.Activity_Confirm_Notification_Order;
import wizag.com.supa.activity.Activity_Home;
import wizag.com.supa.activity.Activity_track_Driver;
import wizag.com.supa.helper.MyApplication;
import wizag.com.supa.models.Model_Notification;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgingService";
    private static final String TITLE = "title";
    private static final String ORDER_ID = "order_id";
    private static final String MESSAGE = "message";
    private static final String IMAGE = "image";
    private static final String ACTION = "action";
    private static final String DATA = "data";
    private static final String ACTION_DESTINATION = "action_destination";
    private static final String SHARED_PREF_NAME = "notification";
    String order_id, client_phone, driver_phone, driver_name, client_name, order_otp;
    JSONObject data;
    PendingIntent pendingIntent;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "message data payload" + remoteMessage.getData());

            data = new JSONObject(remoteMessage.getData());
            try {
                order_id = data.getString("order_id");
                driver_phone = data.get("driver_phone").toString();
                order_otp = data.get("order_otp").toString();
                driver_name = data.get("driver_name").toString();


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        if (remoteMessage.getNotification() != null) {
//            sendNotification(remoteMessage);
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getNotification().getClickAction();

            Log.e(TAG, "Message Notification Title" + title);
            Log.e(TAG, "Message Notification body" + message);
            Log.e(TAG, "Message Notification click action" + click_action);

            sendNotification(title, message, click_action);

        }
    }

    private void sendNotification(String title, String message, String click_action) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "cosand_buy";


//        Intent class_intent = null;

        if (click_action != null && click_action.equals(".activity.Activity_Confirm_Notification_Order")) {
            Intent class_intent = new Intent(this, Activity_Confirm_Notification_Order.class);
            class_intent.putExtra("order_id", order_id);
            try {
                client_phone = data.get("client_phone").toString();
                client_name = data.get("client_name").toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("client_phone", client_phone);
            editor.putString("client_name", client_name);
            editor.apply();

            class_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, class_intent, PendingIntent.FLAG_ONE_SHOT);


        } else if (click_action != null && click_action.equals(".activity.Activity_track_Driver")) {
            Intent class_intent = new Intent(this, Activity_track_Driver.class);

            SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("driver_phone", driver_phone);
            editor.putString("driver_name", driver_name);
            editor.putString("order_otp", order_otp);
            editor.apply();
            class_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, class_intent, PendingIntent.FLAG_ONE_SHOT);


        }

        /*for android oreo and higher*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Cosand Notification",
                    NotificationManager.IMPORTANCE_MAX);
            /*CONFIGURE notification channel*/
            notificationChannel.setDescription("Cosand channel for order reuqests");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{
                    0, 1000, 500, 1000
            });
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.info)
                .setTicker("Cosand")
                .setContentTitle(title)
                .setContentText(message)
                .setContentInfo("info")
                .setContentIntent(pendingIntent);

        notificationManager.notify(1, notificationBuilder.build());

    }


}