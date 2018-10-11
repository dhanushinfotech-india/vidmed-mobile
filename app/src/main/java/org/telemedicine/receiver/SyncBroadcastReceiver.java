package org.telemedicine.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.telemedicine.LoginActivity;
import org.telemedicine.Web.WebSocketExampleClient;

/*import org.telemedicine.NewActionActivity;*/


public class SyncBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Sync alarm triggered. Trying to Sync.", "Sync Braoadcast");
        if (!WebSocketExampleClient.isRunning)
//            LoginActivity.disconnectWS();
            LoginActivity.connectWS(context);
    }
}

