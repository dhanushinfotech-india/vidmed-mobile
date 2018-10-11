package org.telemedicine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.Web.Data;
import org.telemedicine.Web.HttpAgent;
import org.telemedicine.Web.UrlList;
import org.telemedicine.receiver.SyncScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Naveen.k on 8/11/2016.
 */
public abstract class SuperActivity extends Activity implements View.OnClickListener {
    SharedPreferences preferences;
    // protected Context context;
    DbHelper dbHelper;
    String role;

    String callStatus = "call is initiated", CALLER_URL, patientId;
    ProgressDialog callProgressDialog;

    boolean isPeerUnavailalbe = false;
    public static boolean isLogin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   context = Context.getInstance();*/
        dbHelper = DbHelper.getInstance(this);
        onCreation(savedInstanceState);
        preferences = getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE);
    }

    public void onResumption() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        onResumption();
    }

    protected abstract void onCreation(Bundle savedInstanceState);


    public void saveStringInPref(String key, String value) {
        preferences.edit().putString(key, value).commit();
    }

    public String getStrValueFromPref(String key) {
        if (preferences == null)
            preferences = getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE);
        return preferences.contains(key) ? preferences.getString(key, "") : "";
    }

    public String getUserId() {
        return getProfileInfoDB("userId");
    }

    public String getUserName() {
        return getProfileInfoDB("firstName") + " " + getProfileInfoDB("lastName");
    }

    public String getProfile() {
        return getProfileInfoDB("userId");
    }

    private String getProfileInfoDB(String key) {
        Cursor profileCursor = dbHelper.getProfile();
        try {
            while (profileCursor.moveToNext()) {
                return profileCursor.getString(profileCursor.getColumnIndex(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return null;
    }

    public String getDataFromJsonStr(String jsonStr, String key) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            return jsonObject.has(key) ? jsonObject.getString(key) : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getGender(String value) {
        switch (value) {
            case "M":
                return "Male";
            case "F":
                return "Female";
            case "a":
                break;
        }
        return "";
    }


    public String getGenderCode(String value) {
        switch (value.toLowerCase()) {
            case "male":
                return "M";
            case "female":
                return "F";
            case "a":
                break;
        }
        return "";
    }

    public String getDataFromJsonObj(JSONObject jsonObject, String key) {
        try {
            return jsonObject.has(key) ? jsonObject.getString(key) : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void getNotifyService(String status, String callerWSID) throws JSONException {
        HttpAgent httpAgent = new HttpAgent(this);
        List<NameValuePair> _params = new ArrayList<NameValuePair>();
        _params.add(new BasicNameValuePair("info", new JSONObject().put("status", status).toString()));
        String encodedParams = URLEncodedUtils.format(_params, "utf-8");
        Data data = new Data();
        data.setBaseUrl(String.format(UrlList.NOTIFYURL, callerWSID) + encodedParams);
        data.setUrlId(UrlList.NOTIFY_URL_ID);
        data.setSecured(true);
        data.setIsGET(true);
        data.setSSLSecured(true);
        data.setParams(new JSONObject());
        httpAgent.executeWs(data);
    }

//    public void getNotifyService(JSONObject infoJson, String callerWSID) throws JSONException {
//        HttpAgent httpAgent = new HttpAgent(this);
//        List<NameValuePair> _params = new ArrayList<NameValuePair>();
//        _params.add(new BasicNameValuePair("info", infoJson.toString()));
//        String encodedParams = URLEncodedUtils.format(_params, "utf-8");
//        Data data = new Data();
//        data.setBaseUrl(String.format(UrlList.NOTIFYURL, callerWSID) + encodedParams);
//        data.setUrlId(UrlList.NOTIFY_URL_ID);
//        data.setIsGET(true);
//        data.setSecured(true);
//        data.setSSLSecured(true);
//        data.setParams(new JSONObject());
//        httpAgent.executeWs(data);
//    }

    public void getNotifyService(JSONObject infoJson, String[] callerWSIDs) throws JSONException {
        HttpAgent httpAgent = new HttpAgent(this);
        List<NameValuePair> _params = new ArrayList<NameValuePair>();
        _params.add(new BasicNameValuePair("info", infoJson.toString()));
        JSONArray doctors = new JSONArray();
        for (String str : callerWSIDs) {
            doctors.put(str);
        }
        _params.add(new BasicNameValuePair("receivers", doctors.toString()));
        String encodedParams = URLEncodedUtils.format(_params, "utf-8");
        Data data = new Data();
        data.setBaseUrl(UrlList.NOTIFYSTATUSURL + encodedParams);
        Log.e("GET NOTIFY SERVICE", data.getBaseUrl());
        data.setUrlId(UrlList.NOTIFY_STATUS_URL_ID);
        data.setIsGET(true);
        data.setSecured(true);
        data.setSSLSecured(true);
        data.setParams(new JSONObject());
        httpAgent.executeWs(data);
    }


//    public void callDoctor(final String pharmacist, String doctor, String patientId) {
//        this.patientId = patientId;
//        callProgressDialog = new ProgressDialog(this);
//        callProgressDialog.setCancelable(false);
//        callProgressDialog.setButton("End Call", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (mConnection != null) {
//                    try {
//                        mConnection.sendTextMessage(new JSONObject().put("msg_type", "Call disconnected").put("status", "disconnect").put("receiver", pharmacist).put("patientId", SuperActivity.this.patientId).toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                disconnect(mConnection);
//                dialog.dismiss();
//            }
//        });
//
//        callProgressDialog.setMessage(callStatus);
//        if (isNetworkAvailable())
//            createTempWebSocket(pharmacist, doctor);
//        else
//            Toast.makeText(SuperActivity.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
//    }

//    private void checkForNoAnswer() {
//        if (callProgressDialog != null && callProgressDialog.isShowing()) {
//            callProgressDialog.dismiss();
//            if (mConnection != null && mConnection.isConnected()) {
//                mConnection.disconnect();
//            }
//            this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(SuperActivity.this, "Unable to reach doctor. Please try again", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(SuperActivity.this, HomeActivity.class));
//                }
//            });
//        }
//    }


//    private void createTempWebSocket(final String pharmacist, final String doctor) {
////        context = org.ei.telemedicine.Context.getInstance().updateApplicationContext(this.getApplicationContext());
//        final String wsuri = "ws://202.153.34.169:8004/wscall?id=cli:" + pharmacist + "&peer_id=cli:" + doctor;
//        final String wsuri = "ws://103.15.74.24:8004/wscall?id=cli:" + pharmacist + "&peer_id=cli:" + doctor;
//
//        try {
//            if (mConnection != null && mConnection.isConnected())
//                mConnection.disconnect();
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
//                    callProgressDialog.setTitle("Call to " + doctor);
//                    callProgressDialog.show();
//                    Log.e("Temp", "Socket is created");
//                    try {
//                        mConnection.sendTextMessage(new JSONObject().put("msg_type", "initated").put("status", "INI").put("caller", pharmacist).put("receiver", doctor).put("patientId", SuperActivity.this.patientId).toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                private void getsmsrequestsingle(String user, String pass, String sender, String phone, String text, String priority, String stype) throws JSONException {
//
//                    HttpAgent asyncTaskData = new HttpAgent(SuperActivity.this);
//                    Data data = new Data();
//                    data.setBaseUrl(String.format(UrlList.sms_request_single + user + pass + sender + phone + text + priority + stype));
//                    data.setUrlId(UrlList.smsrequest_single_id);
//                    data.setIsGET(true);
//                    data.setParams(new JSONObject());
//                    asyncTaskData.executeWs(data);
//
//
//                }
//
//                private void getsmsrequestmultiple(String user, String pass, String sender, String phone, String text, String priority, String stype) throws JSONException {
//
//                    HttpAgent asyncTaskData = new HttpAgent(SuperActivity.this);
//                    Data data = new Data();
//                    data.setBaseUrl(String.format(UrlList.sms_request_multiple + user + pass + sender + phone + text + priority + stype));
//                    data.setUrlId(UrlList.smsrequest_multiple_id);
//                    data.setIsGET(true);
//                    data.setParams(new JSONObject());
//                    asyncTaskData.executeWs(data);
//
//                }
//
//
//                private void getsmsschedulerequest(String user, String pass, String sender, String phone, String text, String priority, String stype, String time) throws JSONException {
//
//                    HttpAgent asyncTaskData = new HttpAgent(SuperActivity.this);
//                    Data data = new Data();
//                    data.setBaseUrl(String.format(UrlList.sms_request_schedule + user + pass + sender + phone + text + priority + stype + time));
//                    data.setUrlId(UrlList.smsrequest_schedule_id);
//                    data.setIsGET(true);
//                    data.setParams(new JSONObject());
//                    asyncTaskData.executeWs(data);
//
//                }
//
//                private void getsmscheckbalance(String user, String pass) throws JSONException {
//
//                    HttpAgent asyncTaskData = new HttpAgent(SuperActivity.this);
//                    Data data = new Data();
//                    data.setBaseUrl(String.format(UrlList.sms_check_balance + user + pass));
//                    data.setUrlId(UrlList.smscheck_balance_id);
//                    data.setIsGET(true);
//                    data.setParams(new JSONObject());
//                    asyncTaskData.executeWs(data);
//
//                }
//
//
//                private void getsmsallsender(String user, String pass) throws JSONException {
//
//                    HttpAgent asyncTaskData = new HttpAgent(SuperActivity.this);
//                    Data data = new Data();
//                    data.setBaseUrl(String.format(UrlList.sms_allsender + user + pass));
//                    data.setUrlId(UrlList.sms_allsender_id);
//                    data.setIsGET(true);
//                    data.setParams(new JSONObject());
//                    asyncTaskData.executeWs(data);
//
//                }
//
//
//                private void getsmsrequestsender(String user, String senderid, String type) throws JSONException {
//
//                    HttpAgent asyncTaskData = new HttpAgent(SuperActivity.this);
//                    Data data = new Data();
//                    data.setBaseUrl(String.format(UrlList.sms_requestsender + user + senderid + type));
//                    data.setUrlId(UrlList.sms_requestsender_id);
//                    data.setIsGET(true);
//                    data.setParams(new JSONObject());
//                    asyncTaskData.executeWs(data);
//
//                }
//
//                private void getsmsdeliveryreport(String user, String msgid, String phone, String msgtype) throws JSONException {
//
//                    HttpAgent asyncTaskData = new HttpAgent(SuperActivity.this);
//                    Data data = new Data();
//                    data.setBaseUrl(String.format(UrlList.sms_delivery_report + user + msgid + phone + msgtype));
//                    data.setUrlId(UrlList.sms_delivery_report_id);
//                    data.setIsGET(true);
//                    data.setParams(new JSONObject());
//                    asyncTaskData.executeWs(data);
//
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
//                            Toast.makeText(SuperActivity.this, "Waiting for response from " + getDataFromJson(payload, "receiver"), Toast.LENGTH_SHORT).show();
////                            new AlertDialog.Builder(TempWebSocket.this).setMessage("Ring Start").show();
//                            break;
//                        case "accept":
//                            isPeerUnavailalbe = false;
//                            if (callProgressDialog != null && callProgressDialog.isShowing())
//                                callProgressDialog.dismiss();
//                            Toast.makeText(SuperActivity.this, getDataFromJson(payload, "receiver") + " accepted the call", Toast.LENGTH_SHORT).show();
//                            openWeb(pharmacist, doctor);
//                            //gotoHome();
//                            disconnect(mConnection);
////                            new AlertDialog.Builder(TempWebSocket.this).setMessage("Ring Start").show();
//                            break;
//                        case "reject":
//                            isPeerUnavailalbe = false;
//                            if (callProgressDialog != null && callProgressDialog.isShowing())
//                                callProgressDialog.dismiss();
//                            Toast.makeText(SuperActivity.this, getDataFromJson(payload, "receiver") + " rejected the call", Toast.LENGTH_SHORT).show();
//                            disconnect(mConnection);
//                            break;
//                        case "no_answer":
//                            isPeerUnavailalbe = false;
//                            if (callProgressDialog != null && callProgressDialog.isShowing())
//                                callProgressDialog.dismiss();
//                            new AlertDialog.Builder(SuperActivity.this).setMessage(getDataFromJson(payload, "receiver") + " is not responding. Please try later").
//                                    setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//
//                                        }
//                                    }).show();
//                            Toast.makeText(SuperActivity.this, getDataFromJson(payload, "receiver") + " is not responding. Please try later", Toast.LENGTH_SHORT).show();
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
//                        new AlertDialog.Builder(SuperActivity.this).setMessage("Unable to reach " + doctor + ". Please try again").
//                                setNeutralButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//
//                                    }
//                                }).show();
//                        Toast.makeText(SuperActivity.this, "Unable to reach " + doctor + ". Please try again", Toast.LENGTH_SHORT).show();
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

//    private void disconnect(WebSocketConnection mConnection) {
//        if (mConnection != null) {
//            mConnection.disconnect();
//        }
//    }

    private String getDataFromJson(String jsonStr, String key) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            return jsonObject.has(key) ? jsonObject.getString(key) : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void start() {
        try {
            SyncScheduler.stop(SuperActivity.this);
        } finally {
            if (isNetworkAvailable()) {
                SyncScheduler.startOnlyIfConnectedToNetwork(SuperActivity.this);
            } else {
                Log.d("Login Activity Web Socket", "No connection");
            }
        }
    }

    public void gotoHome() {
        SuperActivity.isLogin = true;
        Intent intent = null;
        saveStringInPref("isUserloged", "yes");
        role = getStrValueFromPref("role");
        String userId = getStrValueFromPref("userId");
        LoginActivity.userName = getStrValueFromPref("userId");
        LoginActivity.fullName = getStrValueFromPref("fullName");
        start();
        switch (role.toLowerCase()) {
            case "pharmacist":
                intent = new Intent(SuperActivity.this, HomeActivity.class);

                break;
            case "doctor":
                intent = new Intent(SuperActivity.this, HomeActivity.class);

                break;
            case "patient":
//                if(edtPassword.getText().toString().equalsIgnoreCase("123456"))
//                {
                intent = new Intent(SuperActivity.this, PatientInfoActivity.class);

                intent.putExtra("patientId", userId);

//                else {
//                Toast.makeText(getApplicationContext(),"invalid username/password",Toast.LENGTH_LONG).show();}

                break;
        }
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            this.finish();

        }
    }

    private Runnable changeMessage = new Runnable() {
        @Override
        public void run() {
            //Log.v(TAG, strCharacters);
            callProgressDialog.setMessage(callStatus);
        }
    };

    public void logout() {
        new AlertDialog.Builder(SuperActivity.this).setMessage("Do you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.clearPatients();
                        dbHelper.clearProfile();
                        SyncScheduler.stop(SuperActivity.this);
                        LoginActivity.disconnectWS();
                        saveStringInPref("isUserloged", "no");
                        Intent in = new Intent(SuperActivity.this, LoginActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    /*Converting string image to bitmap*/
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Log.d("encodinfbitmaptostring", "=====" + encodedString);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }

    public BroadcastReceiver callStatusBroadCast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = intent.getStringExtra("status");
            if (status.trim().length() != 0)
                new AlertDialog.Builder(SuperActivity.this)
                        .setTitle("Call Status")
                        .setMessage(status)
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              dialog.dismiss();
                            }
                        }).show();

             //   Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
        }
    };

}
