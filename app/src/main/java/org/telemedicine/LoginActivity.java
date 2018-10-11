package org.telemedicine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.Web.Data;
import org.telemedicine.Web.HttpAgent;
import org.telemedicine.Web.JsonResult;
import org.telemedicine.Web.SocketHandler;
import org.telemedicine.Web.UrlList;
import org.telemedicine.Web.WebSocketExampleClient;


/**
 * Created by Naveen.k on 8/11/2016.
 */
public class LoginActivity extends SuperActivity implements JsonResult {

    static String userName, role, fullName, pharmaname, valid_date;
    EditText edtUsername, edtPassword;
    Button login_btn;
    TextView forgotpswd_btn, setting;
    CheckBox ch_showpwd;
    //Spinner sp_role;
    String saveUser, savePwd;
    public static Boolean backbutton = false;

    private static android.content.Context mContext;


    @Override
    protected void onCreation(Bundle savedInstanceState) {
        setContentView(R.layout.login_screen_activity);
        mContext = this.getApplicationContext();

        /* Hiding keyboard  when loyout with scroll view */
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        edtUsername = (EditText) findViewById(R.id.editUser);
        edtPassword = (EditText) findViewById(R.id.editpassworod);
        ch_showpwd = (CheckBox) findViewById(R.id.showpwdcheck);
        login_btn = (Button) findViewById(R.id.buttonlogin);
        forgotpswd_btn = (TextView) findViewById(R.id.btnforgotpassword);
        forgotpswd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(in);
            }
        });
        setting = (TextView) findViewById(R.id.settings_Button);


        login_btn.setOnClickListener(this);
        setting.setOnClickListener(this);
        ch_showpwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    edtPassword.setInputType(129);

                } else {
                    edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonlogin:
             /*   hideKeyboard();
                view.setClickable(false);*/
                saveUser = edtUsername.getText().toString().trim();
                savePwd = edtPassword.getText().toString();
                //& !sp_role.getSelectedItem().toString().equalsIgnoreCase("Select")
                if (saveUser.trim().length() != 0 && savePwd.trim().length() != 0) {
                    if (!dbHelper.isProfileExist(saveUser))
                        try {
                            dbHelper.clearProfile();
                            dbHelper.clearPatients();
                            //, sp_role.getSelectedItem().toString()
                            getDataService(saveUser, savePwd);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    else
                        gotoHome();
                } else {
                    Toast.makeText(getApplicationContext(), "please check valid username and password", Toast.LENGTH_LONG).show();
                }
                break;
            /*case R.id.setting:
                Intent intent = new Intent(this, UserSettingsActivity.class);
                startActivity(intent);*/
        }
    }


    private void getDataService(String user, String pwd) throws JSONException {
        HttpAgent asyncTaskData = new HttpAgent(this);
        Data data = new Data();
        // Log.d("getdataservice ", "username+pwd" + user + pwd);
        //data.setBaseUrl(UrlList.login_url + user+"&pwd="+pwd+"&role="+StoreValues.role);
        data.setBaseUrl(String.format(UrlList.login_url, user, pwd));
        data.setUrlId(UrlList.loginurl_id);
        data.setIsGET(true);
        data.setParams(new JSONObject());
        asyncTaskData.executeWs(data);
    }

    @Override
    public void getData(JSONArray response, int urlID) {
        try {
            if (urlID == UrlList.loginurl_id) {
                JSONObject object = response.getJSONObject(0);
                Log.e("json object", "{{{{json object}}}} " + object);
                try {
                    JSONObject persnolinfo = object.getJSONObject("result");
                    //String role = sp_role.getSelectedItem().toString();
                    role = object.has("role") ? object.getString("role") : "";
                    userName = persnolinfo.getString("userid");
                    fullName = persnolinfo.getString("name").replace(" ", "_");
                    pharmaname = persnolinfo.getString("pharmaname");
                    // valid_date=persnolinfo.getString("");

                    saveStringInPref("role", role);
                    saveStringInPref("userId", persnolinfo.getString("userid"));
                    saveStringInPref("fullName", fullName);
                    saveStringInPref("pharmaname", pharmaname);
                    //  saveStringInPref("valid",valid_date);
                    String locations = persnolinfo.has("locations") ? persnolinfo.getJSONObject("locations").toString() : "";
                    String complaints = persnolinfo.has("complaints") ? persnolinfo.getJSONObject("complaints").toString() : "";
                    if (dbHelper.getProfile().getCount() == 0)
                        dbHelper.insertProfile(persnolinfo.getString("userid"), savePwd, persnolinfo.getString("name"), "", persnolinfo.getString("phone"), persnolinfo.getString("email"), role, locations, complaints);
                    LoginActivity.connectWS(LoginActivity.this);
                    gotoHome();
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "Please Check Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "Invalid", Toast.LENGTH_SHORT).show();
        }
    }


    public static void disconnectWS() {
        WebSocketExampleClient.isRunning = false;
        SocketHandler.closeConnection();
    }

    public static void connectWS(Context context) {
        Log.d("websocket", "try to connect");
        try {
            connectNewWS(userName, fullName != null ? fullName.replace(" ", "%20") : "", context);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", "test" + "Exception for login ");

        }
    }
   /* private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), HIDE_NOT_ALWAYS);
    }*/


    public static void connectNewWS(final String callerName, final String fullName, Context context) {
        if (!WebSocketExampleClient.isRunning) {
            final String url = String.format(UrlList.INITIATEWSURL, callerName, role, fullName);
            new SocketHandler(url, "sample", context);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}
