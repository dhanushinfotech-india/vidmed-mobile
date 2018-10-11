/*
package org.telemedicine;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.Web.Data;
import org.telemedicine.Web.HttpAgent;
import org.telemedicine.Web.JsonResult;
import org.telemedicine.Web.UrlList;

import java.util.ArrayList;
import java.util.List;

//import org.ei.telemedicine.Context;

public class NewActionActivity extends SuperActivity implements JsonResult {

    public static boolean isBusy = false;
    boolean callStatus = false;
    public static Ringtone ringtone;
    String meetingURL = "", callerName = "", callerWSID = "", patientID = "";
    Thread ft;

    @Override
    protected void onCreation(Bundle savedInstanceState) {
        try {
            NewActionActivity.isBusy = true;
            Intent myIntent = getIntent();
            meetingURL = myIntent.getStringExtra("meetingURL");
            callerName = myIntent.getStringExtra("callerName");
            callerWSID = myIntent.getStringExtra("callerWSID");
            patientID = myIntent.getStringExtra("patientID");
            ft = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(40000);
                        checkForNoAnswer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            ft.start();

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), notification);
            ringtone.play();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        setContentView(R.layout.activity_action);
        this.setFinishOnTouchOutside(false);

        TextView showCaller = (TextView) findViewById(R.id.txtCaller);
        showCaller.setText(callerName + " is calling....");
        findViewById(R.id.tv_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtone.stop();
                Log.e("Receive URL", meetingURL);
                try {
                    NewActionActivity.isBusy = false;
                    getNotifyService(new JSONObject().put("meetingURL", meetingURL).put("status", "answer"), callerWSID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.tv_reject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtone.stop();
                if (ft.isAlive())
                    ft.interrupt();
                try {
                    getNotifyService(new JSONObject().put("status", "reject").put("patientID", patientID), callerWSID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    public void getNotifyService(String status, String callerWSID) throws JSONException {
        HttpAgent httpAgent = new HttpAgent(this);
        List<NameValuePair> _params = new ArrayList<NameValuePair>();
        _params.add(new BasicNameValuePair("info", new JSONObject().put("status", status).toString()));
        String encodedParams = URLEncodedUtils.format(_params, "utf-8");
        Data data = new Data();
        data.setBaseUrl(String.format(UrlList.NOTIFYURL, callerWSID) + encodedParams);
        data.setUrlId(UrlList.NOTIFY_URL_ID);
        data.setIsGET(true);
        data.setSecured(true);
        data.setSSLSecured(true);
        data.setParams(new JSONObject());
        httpAgent.executeWs(data);
    }

    public void getNotifyService(JSONObject infoJson, String callerWSID) throws JSONException {
        HttpAgent httpAgent = new HttpAgent(this);
        List<NameValuePair> _params = new ArrayList<NameValuePair>();
        _params.add(new BasicNameValuePair("info", infoJson.toString()));
        String encodedParams = URLEncodedUtils.format(_params, "utf-8");
        Data data = new Data();
        data.setBaseUrl(String.format(UrlList.NOTIFYURL, callerWSID) + encodedParams);
        data.setUrlId(UrlList.NOTIFY_URL_ID);
        data.setIsGET(true);
        data.setSecured(true);
        data.setSSLSecured(true);
        data.setParams(new JSONObject());
        httpAgent.executeWs(data);
    }

    private void checkForNoAnswer() {
        if (!callStatus && isBusy) {
            try {
                if (ringtone != null && ringtone.isPlaying())
                    ringtone.stop();
                NewActionActivity.isBusy = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NewActionActivity.this, "No Answer", Toast.LENGTH_SHORT).show();
                    }
                });
                finish();
                getNotifyService("no_answer", callerWSID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private String getDataFromJson(String jsonStr, String key) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            return jsonObject.has(key) ? jsonObject.getString(key) : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void getData(JSONArray response, int urlID) throws JSONException {
        switch (urlID) {
            case UrlList.NOTIFY_URL_ID:
                Log.e("respone", "response" + response);
                JSONObject object = new JSONObject(new JSONObject(response.getJSONObject(0).toString()).getString("info"));
                String status;
                status = getDataFromJson(object.toString(), "status");
                switch (status) {
                    case "no_answer":
                        NewActionActivity.isBusy = false;
                        finish();
                        break;
                    case "reject":
                        NewActionActivity.isBusy = false;
                        callStatus = true;
                        finish();
                        break;
                    case "answer":
                        callStatus = true;
                        NewActionActivity.isBusy = false;
                        startActivity(new Intent(NewActionActivity.this, POCActivity.class).putExtra("doctorId", getUserId()).putExtra("patientId", patientID));
                        finish();
                        break;
                }

                break;
        }
    }
}
*/
