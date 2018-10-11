package org.telemedicine.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.java_websocket.client.DefaultSSLWebSocketClientFactory;
import org.java_websocket.drafts.Draft_17;
import org.telemedicine.Web.WebSocketExampleClient;

import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class SocketService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_SOCKET = "org.telemedicine.service.action.socketinit";
    private static final int TIMEOUT = 10000;
    public static final String LOCAL_WSIP_PARAM = "localWSSIP";
    public static final String SEND_STRING_PARAM = "sendString";
    public static WebSocketExampleClient webSocketExampleClient;

    public SocketService() {
        super("SocketService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Socket Service", "On Create");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Socket Service", "Initated");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SOCKET.equals(action)) {
                //                final String localWSSIP = "ws://202.153.34.169:8004/wscall?id=cli:doc7675&peer_id=cli:anmdemo"
//                final String localWSSIP = "ws://103.15.74.24:8001/attendance/?name=ph7093710337&practice=PHARMACIST&fullname=Sudheer_kumar";
                final String localWSSIP = intent.getStringExtra(LOCAL_WSIP_PARAM);
                final String sendString = intent.getStringExtra(SEND_STRING_PARAM);
                handleActionFoo(localWSSIP, sendString);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String localWSSIP, String sendString) {
        // TODO: Handle action Foo
        try {
            Log.d("Socket Service", "Handle Socket URL " + localWSSIP);
//                Map<String, String> cmap = new HashMap<String, String>();
//                String cookieString = cookie.getName() + "=" + cookie.getValue();
//                cmap.put("cookie", cookieString);

            URI uri = new URI(localWSSIP);

            webSocketExampleClient = new WebSocketExampleClient(getApplicationContext(), uri, new Draft_17(), null, TIMEOUT);

            //This part is needed in case you are going to use self-signed certificates
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            }};

            try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());

                //Otherwise the line below is all that is needed.
                //sc.init(null, null, null);
     //enable this one for pharma.vidmed.in
               // HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                //enable for 202 server
   webSocketExampleClient.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sc));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("Socket Service", "websocket factory set");
//            java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");
//            java.lang.System.setProperty("java.net.preferIPv4Stack", "true");
            Log.d("Socket Service", "websocket cConnecttet");
            if (webSocketExampleClient.connectBlocking()) {
                webSocketExampleClient.send(sendString);
                Log.d("Send", "websocket connection send");
            } else {
                Log.d("Send", "websocket connection send failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Send", "websocket connection send done");

    }

}
