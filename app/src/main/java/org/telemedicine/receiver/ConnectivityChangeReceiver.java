package org.telemedicine.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.util.Log;

import org.telemedicine.DbHelper;
import org.telemedicine.LoginActivity;


public class ConnectivityChangeReceiver extends BroadcastReceiver {
    private final String CALLER = "name";
    private String TAG = "RECEIVER";
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i("Receiver", "Connectivity change receiver triggered.");
        if (intent.getExtras() != null) {
            if (isDeviceDisconnectedFromNetwork(intent)) {
                Log.i("Receiver", "Device got disconnected from network. Stopping Dristhi Sync scheduler.");
                SyncScheduler.stop(context);
//                LoginActivity.disconnectWS();
                return;
            }
            if (isDeviceConnectedToNetwork(intent)) {
                SyncScheduler.start(context);
            }
        }
    }

    private boolean isDeviceDisconnectedFromNetwork(Intent intent) {
        Log.e("Connectivity", "deveice disconnect");
        return intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
    }

    private boolean isDeviceConnectedToNetwork(Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
        return networkInfo != null && networkInfo.isConnected();
    }
}

