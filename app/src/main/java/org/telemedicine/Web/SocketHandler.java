package org.telemedicine.Web;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.telemedicine.service.SocketService;

import static org.telemedicine.service.SocketService.webSocketExampleClient;

public class SocketHandler {
    //Server IP address
//    private static String localipaddress = "192.168.1.123";
//    private static final String localWSSIP = "wss://" + localipaddress + ":9443/socket";
    String localWSSIP;
    Context context;
    private static final int TIMEOUT = 10000;

    public SocketHandler(String url, String output, Context context) {
//        SocketConnector socketConnector = new SocketConnector();
//        socketConnector.sendString = output;
//        socketConnector.context = context;
//        this.localWSSIP = url;
        this.context = context;
//        socketConnector.execute();
        enableSocket(url, output);
    }

    private void enableSocket(String url, String sendStr) {
        Log.d("socket service", "Try to connect");
        Intent intent = new Intent(context.getApplicationContext(), SocketService.class);
        intent.setAction(SocketService.ACTION_SOCKET);
        intent.putExtra(SocketService.LOCAL_WSIP_PARAM, url);
        intent.putExtra(SocketService.SEND_STRING_PARAM, sendStr);
        context.getApplicationContext().startService(intent);
    }

//    private class SocketConnector extends AsyncTask<Void, Void, Void> {
//        private String sendString;
//        private Context context;
//
////        public void executeWs() {
////            SocketConnector mAsyncTaskData = new SocketConnector();
//////            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//////                mAsyncTaskData.executeOnExecutor(
//////                        AsyncTask.THREAD_POOL_EXECUTOR);
//////            } else {
////            mAsyncTaskData.execute();
//////            }
////        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            Log.d("websocket", "try to Connect through asynctask");
////            PersistentCookieStore cookieStore = SingletonPersistentCookieStore.getInstance(context);
////            final Cookie cookie = cookieStore.getCookies().get(0);
//            try {
////                Map<String, String> cmap = new HashMap<String, String>();
////                String cookieString = cookie.getName() + "=" + cookie.getValue();
////                cmap.put("cookie", cookieString);
//
//                URI uri = new URI(localWSSIP);
//
//                webSocketExampleClient = new WebSocketExampleClient(context, uri, new Draft_17(), null, TIMEOUT);
//
//                //This part is needed in case you are going to use self-signed certificates
//                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                    public X509Certificate[] getAcceptedIssuers() {
//                        return new X509Certificate[]{};
//                    }
//
//                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                    }
//
//                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                    }
//                }};
//
//                try {
//                    SSLContext sc = SSLContext.getInstance("TLS");
//                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
//
//                    //Otherwise the line below is all that is needed.
//                    //sc.init(null, null, null);
//                    webSocketExampleClient.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sc));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                webSocketExampleClient.connectBlocking();
//                webSocketExampleClient.send(sendString);
//                Log.d("Send", "websocket connection send");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            Log.d("Send", "websocket connection send done");
//            return null;
//        }
//
//    }

    public static void closeConnection() {
        if (webSocketExampleClient != null)
            webSocketExampleClient.close();
    }
}