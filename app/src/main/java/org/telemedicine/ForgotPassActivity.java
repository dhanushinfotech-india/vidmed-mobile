package org.telemedicine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.Web.Data;
import org.telemedicine.Web.HttpAgent;
import org.telemedicine.Web.JsonResult;
import org.telemedicine.Web.UrlList;

/**
 * Created by hareesh on 10/4/16.
 */
public class ForgotPassActivity extends SuperActivity implements JsonResult {
String forgotpassword;
    EditText forgot_edit;
    Button forgot_save;


    @Override
    protected void onCreation(Bundle savedInstanceState) {

        setContentView(R.layout.forgot_pass);

        forgot_edit= (EditText) findViewById(R.id.forgot_pass);
        forgot_save= (Button) findViewById(R.id.forgot_pass_save);

        forgot_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_pass_save:

                forgotpassword=forgot_edit.getText().toString();

                if (forgotpassword.trim().length()!=0){
                    try {
                        getDataService(forgotpassword);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(),"Please fill the UserID",Toast.LENGTH_SHORT).show();
                }


        }

    }
    private void getDataService(String userid) throws JSONException {
        HttpAgent asyncTaskData = new HttpAgent(this);
        Data data = new Data();
        data.setBaseUrl(String.format(UrlList.forgot_pass_url, userid));
        data.setUrlId(UrlList.forgot_passurl_id);
        data.setIsGET(true);
        data.setParams(new JSONObject());
        asyncTaskData.executeWs(data);
    }

    @Override
    public void getData(JSONArray response, int urlID) throws JSONException {
        if(urlID==UrlList.forgot_passurl_id){
            JSONObject jsonObject=response.getJSONObject(0);
            if (jsonObject.getString("result").equalsIgnoreCase("success")){
            Toast.makeText(getApplicationContext(),jsonObject.getString("result"),Toast.LENGTH_LONG).show();
            Intent in=new Intent(this,LoginActivity.class);
            startActivity(in);}
            else {
                Toast.makeText(getApplicationContext(),jsonObject.getString("result"),Toast.LENGTH_LONG).show();
            }
        }


    }
}
