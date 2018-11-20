package wizag.com.supa.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
//            MyApplication.isActivityVisible();
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            handleData(data);


        } else if (remoteMessage.getNotification() != null) {
//            MyApplication.isActivityVisible();
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.

    }

    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        Log.d("innitial message", message);
        Model_Notification notificationVO = new Model_Notification();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        Intent resultIntent = new Intent(getApplicationContext(), Activity_Confirm_Notification_Order.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();

    }

    private void handleData(Map<String, String> data) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(getApplicationContext(), Activity_Confirm_Notification_Order.class);
        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent1);

        // Set the Activity to start in a new, empty task
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 123, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "id_product")
                .setSmallIcon(R.drawable.ic_close) //your app icon
                .setBadgeIconType(R.drawable.ic_close) //your app icon
                .setChannelId("order_id")
                .setContentTitle(data.get("title"))
                .setAutoCancel(true).setContentIntent(pendingIntent)
                .setNumber(1)
                .setColor(255)
                .setContentText(data.get("message"))
                .setWhen(System.currentTimeMillis());


        String order_id = data.get("order_id");
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("order_id", order_id);
        editor.apply();


        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.playNotificationSound();
        notificationManager.notify(1, notificationBuilder.build());


    }

    
}