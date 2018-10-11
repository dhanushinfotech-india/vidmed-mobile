package org.telemedicine;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.Adapter.ConsultationAdapter;
import org.telemedicine.Web.Data;
import org.telemedicine.Web.HttpAgent;
import org.telemedicine.Web.JsonResult;
import org.telemedicine.Web.UrlList;
import org.telemedicine.model.Consultationlist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by bhavana on 3/15/17.
 */
public class ConsultationListactivity extends SuperActivity implements JsonResult {
    ListView cons_listview;
    ConsultationAdapter consultationAdapter;
    ArrayList<Consultationlist> consultationlists;
    String pharmaid;
    long fromdate;
    EditText edt_date;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    Button btn_send_mail;
    long todate;
    Button btn_go;
    @Override
    protected void onCreation(Bundle savedInstanceState) {
        setContentView(R.layout.consultationlist);
        cons_listview = (ListView) findViewById(R.id.consultation_list);
        edt_date= (EditText) findViewById(R.id.edt_date);
        btn_go= (Button) findViewById(R.id.btn_go);
        btn_send_mail= (Button) findViewById(R.id.btn_sendmail);
        btn_send_mail.setOnClickListener(this);
        edt_date.setOnClickListener(this);
        btn_go.setOnClickListener(this);
        fromdate=0;
        consultationlists=new ArrayList<>();
        consultationAdapter=new ConsultationAdapter(consultationlists,ConsultationListactivity.this);
      //  consultationlists.add(new Consultationlist("1","sun","455","5757","55"));
        consultationAdapter.notifyDataSetChanged();
        cons_listview.setAdapter(consultationAdapter);

        pharmaid=getStrValueFromPref("userId");


        myCalendar  = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCalendar.set(Calendar.HOUR,0);
                myCalendar.set(Calendar.MINUTE,0);
                myCalendar.set(Calendar.SECOND,0);
                myCalendar.set(Calendar.MILLISECOND,0);

if (myCalendar.getTimeInMillis()>System.currentTimeMillis()){
    Toast.makeText(getApplicationContext(),"Unable to select future dates",Toast.LENGTH_SHORT).show();
    view.updateDate(year,monthOfYear,dayOfMonth);
}
else {
    updateLabel();
}
            }

        };

    }
    private void updateLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Log.d("timemilli","========"+(myCalendar.getTimeInMillis()+43200000));

        fromdate=(myCalendar.getTimeInMillis()-43200000);
        todate=(myCalendar.getTimeInMillis()+43200000);
        Log.d("timemilli","========"+fromdate+"==="+todate);
        edt_date.setText(sdf.format(myCalendar.getTime()));
    }
    public void getDataConsultationList(String fromdate,String todate,String pharmaid){
        HttpAgent httpAgent = new HttpAgent(this);
        Data data = new Data();
        data.setBaseUrl(String.format(UrlList.consultationlisturl,fromdate,todate,pharmaid));
        data.setUrlId(UrlList.consiltationurl_id);
        data.setIsGET(true);
        httpAgent.executeWs(data);
    }
    public void sendMail(String fromdate,String todate,String pharmaid){
        HttpAgent httpAgent = new HttpAgent(this);
        Data data = new Data();
        data.setBaseUrl(String.format(UrlList.sendmail_url,fromdate,todate,pharmaid));
        data.setUrlId(UrlList.sendmail_url_id);
        data.setIsGET(true);
        httpAgent.executeWs(data);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edt_date:
                DatePickerDialog datepickerdialogue=  new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datepickerdialogue.getDatePicker().setMaxDate(System.currentTimeMillis());
                datepickerdialogue.show();

                break;
            case R.id.btn_go:
                if (edt_date.getText().length()!=0){
                    getDataConsultationList(String.valueOf(fromdate), String.valueOf(todate),pharmaid);
                }else {
                    Toast.makeText(getApplicationContext(),"Please select a date",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_sendmail:
                sendMail(String.valueOf(fromdate), String.valueOf(todate),pharmaid);
                break;
        }
    }

    @Override
    public void getData(JSONArray response, int urlID) throws JSONException {
        if (urlID==UrlList.consiltationurl_id){
            JSONArray jsonArray=response.getJSONObject(0).getJSONArray("data");
            consultationlists.clear();
            if (jsonArray.length()!=0) {
                Log.d("jsonarray", "=====" + jsonArray.getJSONObject(0));

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    consultationlists.add(new Consultationlist("" + (i + 1), jsonObject.getString("patientname"),
                            jsonObject.getString("poc_type"), jsonObject.getString("docname"),jsonObject.getString("poc_date")));

                }

            }else {
                Toast.makeText(getApplicationContext(),"No History Available",Toast.LENGTH_SHORT).show();
            }
            consultationAdapter.notifyDataSetChanged();
        }
        else if (urlID==UrlList.sendmail_url_id){
            Log.d("responsemail","========"+response.get(0));
            if (response.toString().contains("1")){
                Toast.makeText(getApplicationContext(),"email sent successfully",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"email sending failed ",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
