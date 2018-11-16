package wizag.com.supa.services;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import wizag.com.supa.NotificationUtils;
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

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
//            MyApplication.isActivityVisible();
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            Map<String, String> data = remoteMessage.getData();
//            handleData(data);

        } else if (remoteMessage.getNotification() != null) {
//            MyApplication.isActivityVisible();
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            handleNotification(remoteMessage.getNotification());
        }// Check if message contains a notification payload.

    }

    private void handleNotification(RemoteMessage.Notification RemoteMsgNotification) {
        String message = RemoteMsgNotification.getBody();
        String title = RemoteMsgNotification.getTitle();
        Log.d("innitial message",message);
        Model_Notification notificationVO = new Model_Notification();
        notificationVO.setTitle(title);
        notificationVO.setMessage(message);

        Intent resultIntent = new Intent(getApplicationContext(), Activity_Confirm_Notification_Order.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();

    }

    private void handleData(Map<String, String> data) {
        String order_id = data.get(ORDER_ID);
        String action =  data.get("actionDestination");
        Model_Notification notificationVO = new Model_Notification();
        notificationVO.setOrder_id(order_id);
        notificationVO.setActionDestination(action);
        Intent resultIntent = new Intent(getApplicationContext(), Activity_Confirm_Notification_Order.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.displayNotification(notificationVO, resultIntent);
        notificationUtils.playNotificationSound();

        startActivity(resultIntent);


    }
}