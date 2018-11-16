package wizag.com.supa.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.ContentResolver;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import wizag.com.supa.R;

import static wizag.com.supa.helper.MyApplication.CHANNEL_1_ID;
import static wizag.com.supa.helper.MyApplication.CHANNEL_2_ID;

public class Activity_Test_Notification extends AppCompatActivity {
    EditText edit_text_title, edit_text_message;
    NotificationManagerCompat notificationManager;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_notification);
        mContext = this;

        notificationManager = NotificationManagerCompat.from(this);
        edit_text_title = findViewById(R.id.edit_text_title);
        edit_text_message = findViewById(R.id.edit_text_message);
    }

    public void sendOnChannel1(View view) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_traffic)
                .setContentTitle(edit_text_title.getText().toString())
                .setContentText(edit_text_message.getText().toString())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
        playNotificationSound();

    }

    public void sendOnChannel2(View view) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_circle)
                .setContentTitle(edit_text_title.getText().toString())
                .setContentText(edit_text_message.getText().toString())
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
        notificationManager.notify(2, notification);
        playNotificationSound();
    }

    public void playNotificationSound() {
        try {
            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + mContext.getPackageName() + "/raw/notification");
            Ringtone r = RingtoneManager.getRingtone(mContext, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
