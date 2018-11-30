package wizag.com.supa.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(NotificationBroadcastReceiver.class.getSimpleName(), "service has stopped!");

        context.startService(new Intent(context, SensorService.class));
    }
}
