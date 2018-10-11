package org.telemedicine.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.telemedicine.DbHelper;
import org.telemedicine.SuperActivity;

public class SyncScheduler {
    public static final int SYNC_INTERVAL = 2 * 60000;
    public static final int SYNC_START_DELAY = 1 * 1000;
    private static Listener<Boolean> logoutListener;
    private static String TAG = "SyncScheduler";

    public static void start(final Context context) {

        PendingIntent syncBroadcastReceiverIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, SyncBroadcastReceiver.class), 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(
                AlarmManager.RTC,
                System.currentTimeMillis() + SYNC_START_DELAY,
                SYNC_INTERVAL,
                syncBroadcastReceiverIntent);

        Log.i("Scheduled to sync from server every {0} seconds.", SYNC_INTERVAL / 1000 + "");

        attachListenerToStopSyncOnLogout(context);

    }

    private static void attachListenerToStopSyncOnLogout(final Context context) {
//        ON_LOGOUT.removeListener(logoutListener);
//        logoutListener = new Listener<Boolean>() {
//            public void onEvent(Boolean data) {
//                logInfo("User is logged out. Stopping Dristhi Sync scheduler.");
//                stop(context);
//            }
//        };
//        ON_LOGOUT.addListener(logoutListener);
        if (!SuperActivity.isLogin) {
            stop(context);
            Log.e("No login", "false");
        }
    }

    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void startOnlyIfConnectedToNetwork(Context context) {
        if (isNetWorkAvailable(context)) {
            start(context);
        } else {
            Log.i("Device not connected to network so not starting sync scheduler.", "no network");
        }

    }

    public static void stop(Context context) {
        Log.i("Sync Scheduler", "Stop Service");
        PendingIntent syncBroadcastReceiverIntent = PendingIntent.getBroadcast(context, 0, new Intent(context, SyncBroadcastReceiver.class), 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(syncBroadcastReceiverIntent);
    }
}
