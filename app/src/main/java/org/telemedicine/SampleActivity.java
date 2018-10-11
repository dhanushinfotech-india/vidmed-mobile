//package org.telemedicine;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import de.tavendo.autobahn.WebSocketConnection;
//import de.tavendo.autobahn.WebSocketException;
//import de.tavendo.autobahn.WebSocketHandler;
//
//public class SampleActivity extends Activity {
//    public static WebSocketConnection mConnection = null;
//    private static android.content.Context mContext;
//    public String callerName, receiverName;
//    boolean isPeerUnavailalbe = false;
//    ProgressDialog callProgressDialog;
//    String callStatus = "call is initiated";
//    EditText et_caller, et_rec;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sample);
//        mContext = this.getApplicationContext();
//        et_caller = (EditText) findViewById(R.id.et_caller);
//        et_rec = (EditText) findViewById(R.id.et_receiver);
//
//        ((Button) findViewById(R.id.login)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callerName = et_caller.getText().toString();
//                receiverName = et_rec.getText().toString();
//                if (isNetworkAvailable()) {
//                    start(callerName, receiverName);
//                } else {
//                    Log.d("Sample Activity", "No connection");
//                }
//            }
//        });
//        ((Button) findViewById(R.id.call)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isNetworkAvailable()) {
//                    callProgressDialog = new ProgressDialog(SampleActivity.this);
//                    callProgressDialog.setCancelable(false);
//                    callProgressDialog.setButton("End Call", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (mConnection != null) {
//                                try {
//                                    mConnection.sendTextMessage(new JSONObject().put("msg_type", "Call disconnected").put("status", "disconnect").put("receiver", callerName).toString());
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            disconnect(mConnection);
//                            dialog.dismiss();
//                        }
//                    });
//
//                    callProgressDialog.setMessage(callStatus);
//
//                    createTempWebSocket(callerName, receiverName);
//                } else {
//                    Log.d("Sample Activity", "No connection");
//                }
//            }
//        });
//    }
//
//    public void start(String callerName, String receiverName) {
//        if (SampleActivity.mConnection == null || !SampleActivity.mConnection.isConnected())
//            SampleActivity.connectWS(callerName, receiverName);
//    }
//
//    private boolean isNetworkAvailable() {
//        try {
//            ConnectivityManager connectivityManager
//                    = (ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
//            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public static void disconnectWS() {
//        if (SampleActivity.mConnection != null) {
//            SampleActivity.mConnection.disconnect();
//            SampleActivity.mConnection = null;
//        }
//    }
//
//    public static void connectWS(String _callerName, String receiverName) {
//
//        SampleActivity.mConnection = new WebSocketConnection();
//        final String wsuri = Constants.wsURL + Constants.WEBSOCKET;
//        final String userName = _callerName;
//        final String _receiverName = receiverName;
//        try {
//            final String url = String.format(wsuri, userName);
//            Log.e("FFFFF", url);
//
//            mConnection.connect(url, new WebSocketHandler() {
//
//                @Override
//                public void onOpen() {
//                    Log.d("SampleActivity-WS Open", "Status: Connected to " + url);
//                    //Toast.makeText(getApplicationContext(), String.format(wsuri, getUsern()), Toast.LENGTH_SHORT).show();
//                    //mConnection.sendTextMessage("Hello, world!");
//                }
//
//                @Override
//                public void onTextMessage(String payload) {
//                    Log.d("Sample Act onTextMsg", "Got echo: " + payload);
//                    try {
//                        JSONObject jObject = new JSONObject(payload);
//                        String status = jObject.getString("status");
//                        String msg = jObject.getString("msg_type");
//                        String caller = jObject.getString("caller");
//                        String receiver = jObject.getString("receiver");
//                        //Log.d(TAG, check);
//                        String match = "INI";
//                        boolean response = (status.equals(match));
//                        if (receiver.equalsIgnoreCase(userName) && response && !ActionActivity.isBusy) {
//                            //Toast.makeText(getApplicationContext(),"call started",Toast.LENGTH_LONG).show();
//                            Intent i = new Intent(mContext, ActionActivity.class);
//                            i.putExtra("name", caller);
//                            i.putExtra("receiver", _receiverName);
//                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            mContext.startActivity(i);
//                        }
//                    } catch (Exception ex) {
//                        Log.d("SampleAct -WS Exception", ex.toString());
//                    }
//                }
//
//                @Override
//                public void onClose(int code, String reason) {
//                    Log.d("SampleActivity-WS Close", "Connection lost.");
//                    //Toast.makeText(getApplicationContext(),"closed",Toast.LENGTH_SHORT).show();
//                }
//            });
//        } catch (WebSocketException e) {
//            Log.d("SampleAct-WS Exception", e.toString());
//        }
//
//    }
//
//    private void createTempWebSocket(final String doc_name, final String nus_name) {
////        context = org.ei.telemedicine.Context.getInstance().updateApplicationContext(this.getApplicationContext());
//        final String wsuri = "ws://202.153.34.169:8004/wscall?id=cli:" + doc_name + "&peer_id=cli:" + nus_name;
//        final String wsuri = "ws://103.15.74.24:8004/wscall?id=cli:" + doc_name + "&peer_id=cli:" + nus_name;

//
//        try {
////            if (mConnection != null && mConnection.isConnected())
//            disconnectWS();
////                mConnection.disconnect();
//            Log.e("WsUrl", wsuri);
//            if (mConnection == null)
//                mConnection = new WebSocketConnection();
//            mConnection.connect(wsuri, new WebSocketHandler() {
//                @Override
//                public void onOpen() {
//                    super.onOpen();
//                    Thread ft = new Thread() {
//                        public void run() {
//                            try {
//                                Thread.sleep(45000);
//                                checkForNoAnswer();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    };
//                    ft.start();
//                    callProgressDialog.setTitle("Call to " + nus_name);
//                    callProgressDialog.show();
//                    Log.e("Temp", "Socket is created");
//                    try {
//                        mConnection.sendTextMessage(new JSONObject().put("msg_type", "initated").put("status", "INI").put("caller", doc_name).put("receiver", nus_name).toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onTextMessage(String payload) {
//                    super.onTextMessage(payload);
//                    Log.e("Temp Payload", "Getting Data" + payload);
//
//                    String status = getDataFromJson(payload, "status");
//                    switch (status) {
//                        case "ring_starts":
//                            isPeerUnavailalbe = false;
//                            callStatus = "Ringing";
//                            runOnUiThread(changeMessage);
//                            Toast.makeText(SampleActivity.this, "Waiting for response from " + getDataFromJson(payload, "receiver"), Toast.LENGTH_SHORT).show();
////                            new AlertDialog.Builder(TempWebSocket.this).setMessage("Ring Start").show();
//                            break;
//                        case "accept":
//                            isPeerUnavailalbe = false;
//                            if (callProgressDialog != null && callProgressDialog.isShowing())
//                                callProgressDialog.dismiss();
//                            Toast.makeText(SampleActivity.this, getDataFromJson(payload, "receiver") + " accepted the call", Toast.LENGTH_SHORT).show();
//                            openWeb(callerName, receiverName);
//                            disconnect(mConnection);
////                            new AlertDialog.Builder(TempWebSocket.this).setMessage("Ring Start").show();
//                            break;
//                        case "reject":
//                            isPeerUnavailalbe = false;
//                            if (callProgressDialog != null && callProgressDialog.isShowing())
//                                callProgressDialog.dismiss();
//                            Toast.makeText(SampleActivity.this, getDataFromJson(payload, "receiver") + " rejected the call", Toast.LENGTH_SHORT).show();
//                            disconnect(mConnection);
//                            break;
//                        case "no_answer":
//                            isPeerUnavailalbe = false;
//                            if (callProgressDialog != null && callProgressDialog.isShowing())
//                                callProgressDialog.dismiss();
//                            new AlertDialog.Builder(SampleActivity.this).setMessage(getDataFromJson(payload, "receiver") + " is not responding. Please try later").
//                                    setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//
//                                        }
//                                    }).show();
//                            Toast.makeText(SampleActivity.this, getDataFromJson(payload, "receiver") + " is not responding. Please try later", Toast.LENGTH_SHORT).show();
//                            disconnect(mConnection);
//                            break;
//                        default:
//                            isPeerUnavailalbe = true;
//                            if (callProgressDialog != null && callProgressDialog.isShowing())
//                                callProgressDialog.dismiss();
//
//                            disconnect(mConnection);
//                            break;
//                    }
//                    if (isPeerUnavailalbe) {
//                        new AlertDialog.Builder(SampleActivity.this).setMessage("Unable to reach " + nus_name + ". Please try again").
//                                setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                }).show();
//                        Toast.makeText(SampleActivity.this, "Unable to reach " + nus_name + ". Please try again", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//
//                @Override
//                public void onClose(int code, String reason) {
//                    super.onClose(code, reason);
//                    Log.e("Temp", "Socket is closed");
//                    mConnection = null;
//                }
//            });
//
//        } catch (WebSocketException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void disconnect(WebSocketConnection mConnection) {
//        if (mConnection != null) {
//            mConnection.disconnect();
//        }
//    }
//
//    private String getDataFromJson(String jsonStr, String key) {
//        try {
//            JSONObject jsonObject = new JSONObject(jsonStr);
//            return jsonObject.has(key) ? jsonObject.getString(key) : "";
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    private Runnable changeMessage = new Runnable() {
//        @Override
//        public void run() {
//            //Log.v(TAG, strCharacters);
//            callProgressDialog.setMessage(callStatus);
//        }
//    };
//
//    private void openWeb(String doc_name, String nus_name) {
//        String CALLER_URL = Constants.videoURL + Constants.CALLING_URL;
//        String caller_url = String.format(CALLER_URL, doc_name, nus_name);
//        Log.e("Calling URL", caller_url);
////                        startActivity(new Intent(SampleActivity.this, VideoCallActivity.class).putExtra("loadUrl", "http://www.google.com"));
//        Uri url = Uri.parse(caller_url);
//        Intent _broswer = new Intent(Intent.ACTION_VIEW, url);
//        startActivity(_broswer);
//
//    }
//
//    private void checkForNoAnswer() {
//        if (callProgressDialog != null && callProgressDialog.isShowing()) {
//            callProgressDialog.dismiss();
//            if (mConnection != null && mConnection.isConnected()) {
//                mConnection.disconnect();
//            }
//            this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(SampleActivity.this, "Unable to reach anm. Please try again", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//}
