//package org.telemedicine;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.media.Ringtone;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Vibrator;
//import android.util.Log;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
//import de.tavendo.autobahn.WebSocketConnection;
//import de.tavendo.autobahn.WebSocketException;
//import de.tavendo.autobahn.WebSocketHandler;
//
////import org.ei.telemedicine.Context;
//
//public class ActionActivity extends SuperActivity {
//
//    public static boolean isBusy = false;
//    protected String callUrl;
//    boolean callStatus = false;
//    private Ringtone ringtone;
//    private String packName = "org.mozilla.firefox";
//    final WebSocketConnection mConnection = new WebSocketConnection();
//    String callerId = "", callerName = "", patientId = "";
//
//
//    @Override
//    protected void onCreation(Bundle savedInstanceState) {
//        try {
//            ActionActivity.isBusy = true;
//            Intent myIntent = getIntent();
//            callerId = myIntent.getStringExtra("name");
//            callerName = myIntent.getStringExtra("receiver");
//            patientId = myIntent.getStringExtra("patientId");
//            Log.e("ANM", "Before Temp WEb Socket Connection");
//            createTempWebSocket(callerId);
//            Log.e("ANM", "After Temp WEb Socket Connection");
//
//            Thread ft = new Thread() {
//                public void run() {
//                    try {
//                        Thread.sleep(40000);
//                        checkForNoAnswer();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//
//            ft.start();
//
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//            ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
//            ringtone.play();
//            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//            // Vibrate for 500 milliseconds
//            v.vibrate(500);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        setContentView(R.layout.activity_action);
//        this.setFinishOnTouchOutside(false);
//        callUrl = Constants.videoURL + Constants.RECEIVING_URL;
//
//        TextView showCaller = (TextView) findViewById(R.id.txtCaller);
//        showCaller.setText(callerId + " is calling....");
//        findViewById(R.id.tv_accept).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ringtone.stop();
//                Log.e("Receive URL", callUrl);
//                try {
//                    mConnection.sendTextMessage(new JSONObject().put("msg_type", "Call is accepted").put("status", "accept").put("receiver", callerName).toString());
//                    callStatus = true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Uri url = Uri.parse(String.format(callUrl, callerName, callerId));
//                if (mConnection.isConnected())
//                    mConnection.disconnect();
//                ActionActivity.isBusy = false;
////                Toast.makeText(ActionActivity.this, "asdfadsfa---------" + patientId, Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(ActionActivity.this, POCActivity.class).putExtra("doctorId", getUserId()).putExtra("patientId", patientId).putExtra("url", String.format(callUrl, callerName, callerId)));
//                finish();
//                //                if (isPackageExist(packName)) {
//                //                _broswer.setComponent(new ComponentName(org.ei.telemedicine.Context.getInstance().configuration().getClientBrowserUrl(), context.configuration().getClientBrowserAPPUrl()));
////                } else {
////                    Toast.makeText(ActionActivity.this, "Video call is compatible with FireFox. Please install", Toast.LENGTH_SHORT).show();
////                    String firefoxUrl = org.ei.telemedicine.Context.getInstance().configuration().getClientAPPURL();
////                    Uri ul = Uri.parse(firefoxUrl);
////                    Intent downLoadfire = new Intent(Intent.ACTION_VIEW, ul);
////                    startActivity(downLoadfire);
////                }
//            }
//        });
//
//        findViewById(R.id.tv_reject).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ringtone.stop();
//                try {
//                    mConnection.sendTextMessage(new JSONObject().put("msg_type", "Call is Rejected").put("status", "reject").put("receiver", callerName).toString());
//                    if (mConnection.isConnected())
//                        mConnection.disconnect();
//                    ActionActivity.isBusy = false;
//                    callStatus = true;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                finish();
//            }
//        });
//
//
//    }
//
//
//    private void checkForNoAnswer() {
//        if (!callStatus) {
//            try {
//                if (ringtone != null && ringtone.isPlaying())
//                    ringtone.stop();
//                if (mConnection != null && mConnection.isConnected()) {
//                    mConnection.sendTextMessage(new JSONObject().put("msg_type", "No Answer").put("status", "no_answer").put("receiver", callerName).toString());
//                    if (mConnection.isConnected())
//                        mConnection.disconnect();
//                }
//                ActionActivity.isBusy = false;
//                finish();
////                new AlertDialog.Builder(ActionActivity.this).setMessage("You are missed " + callerId + " call").setNeutralButton("Ok", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                    }
////                }).show();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private WebSocketConnection createTempWebSocket(final String callerId) {
//        try {
//            mConnection.connect("ws://202.153.34.169:8004/wscall?id=cli:" + callerName + "&peer_id=cli:" + callerId, new WebSocketHandler() {
//            mConnection.connect("ws://103.15.74.24:8004/wscall?id=cli:" + callerName + "&peer_id=cli:" + callerId, new WebSocketHandler() {
//                @Override
//                public void onOpen() {
//                    super.onOpen();
//                    Log.e("Initiate", "ANM Temp WebSocket");
//                    try {
//                        mConnection.sendTextMessage(new JSONObject().put("msg_type", "Call is ringing").put("status", "ring_starts").put("receiver", callerName).toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onTextMessage(String payload) {
//                    super.onTextMessage(payload);
//                    Log.e("Get ANM ", payload);
//                    String status = getDataFromJson(payload, "status");
//                    switch (status) {
//                        case "disconnect":
//                            ringtone.stop();
//                            ActionActivity.isBusy = false;
//                            Toast.makeText(ActionActivity.this, "Call is disconnected by " + callerId, Toast.LENGTH_SHORT).show();
//                            finish();
//                            break;
//                    }
//                }
//
//                @Override
//                public void onClose(int code, String reason) {
//                    super.onClose(code, reason);
//                }
//            });
//        } catch (WebSocketException e) {
//            e.printStackTrace();
//        }
//        return mConnection;
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
//    @Override
//    public void onClick(View v) {
//
//    }
//}
