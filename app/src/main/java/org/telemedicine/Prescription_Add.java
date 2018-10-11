/*
package org.telemedicine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.Adapter.DrugListAdapter;
import org.telemedicine.Web.Data;
import org.telemedicine.Web.HttpAgent;
import org.telemedicine.Web.JsonResult;
import org.telemedicine.Web.UrlList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

*/
/**
 * Created by hareesh on 9/9/16.
 *//*

public class Prescription_Add extends SuperActivity implements JsonResult {

    EditText drug, direct, dosage, frequency, instructions, eddays;
    ImageView plus;
    ArrayList<DrugListView> druglistview;
    ListView drug_list;
    String select_drug, select_direct, select_dosage, select_frequency, comment, poc_days;
    Button save;
    DrugListAdapter druglistadapter;
    public JSONObject poc = new JSONObject();
    String url, patientId, doctorId, doctorName;
    String existingdrugs;
    JSONArray problemsArray;
    ArrayList<DrugListView> prescr_druglist;
    String prescr_instruction;

    @Override
    protected void onCreation(Bundle savedInstanceState) {
        setContentView(R.layout.add_prescription);
        */
/*final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            doctorName = getUserName();
            patientId = bundle.getString("patientId");
            doctorId = bundle.getString("doctorId");
        }*//*

        problemsArray = new JSONArray();
        instructions = (EditText) findViewById(R.id.patient_instructions);
        eddays = (EditText) findViewById(R.id.patient_days);
        drug = (EditText) findViewById(R.id.drug_edit);
        direct = (EditText) findViewById(R.id.direct_edit);
        dosage = (EditText) findViewById(R.id.dosage_edit);
        frequency = (EditText) findViewById(R.id.frequency_edit);
        save = (Button) findViewById(R.id.patient_save_button);
        plus = (ImageView) findViewById(R.id.plus_image1);
        View view = findViewById(R.id.lv_drugs);
        drug_list = (ListView) view.findViewById(R.id.drug_listview);


        druglistview = new ArrayList<DrugListView>();
        druglistadapter = new DrugListAdapter(this, druglistview);
        drug_list.setAdapter(druglistadapter);
        drug_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        save.setOnClickListener(this);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(drug.getText().length() == 0)) {
                    if ((direct.getText().length() == 0) || (dosage.getText().length() == 0)
                            || (frequency.getText().length() == 0)) {
                        Toast.makeText(Prescription_Add.this, "Please fill all drugs field", Toast.LENGTH_SHORT).show();

                    } else if (eddays.getText().length() == 0) {

                        Toast.makeText(Prescription_Add.this, "Enter Number Of Days", Toast.LENGTH_SHORT).show();
                    } else {
                        findViewById(R.id.drugs_list).setVisibility(View.VISIBLE);
                        druglistview.add(new DrugListView(drug.getText().toString(), direct.getText().toString(), dosage.getText().toString(),
                                frequency.getText().toString(), eddays.getText().toString()));
                        druglistadapter.notifyDataSetChanged();

                        drug.setText("");
                        direct.setText("");
                        dosage.setText("");
                        frequency.setText("");
                        eddays.setText("");
                    }
                } else
                    Toast.makeText(Prescription_Add.this, "Please fill valid drug", Toast.LENGTH_SHORT).show();
            }
        });

        comment = instructions.getText().toString();
        poc_days = eddays.getText().toString();
        select_drug = drug.getText().toString();
        select_direct = direct.getText().toString();
        select_dosage = dosage.getText().toString();
        select_frequency = frequency.getText().toString();

        url=getIntent().getStringExtra("url");
        doctorName=getIntent().getStringExtra("doctorName");
        doctorId=getIntent().getStringExtra("doctorid");
        patientId=getIntent().getStringExtra("patientid");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            prescr_instruction=extras.getString("preinstructions");
            prescr_druglist= (ArrayList<DrugListView>) extras.get("predruglist");
            if(prescr_instruction!=null){

                instructions.setText(prescr_instruction);
            }
            if(prescr_druglist!=null){
        Log.e("size","array"+prescr_druglist.size());
                druglistview.clear();
                druglistview.addAll(prescr_druglist);


            }
        }

    }
    @Override
    public void getData(JSONArray response, int urlID) throws JSONException {

        if (urlID == UrlList.pocsaveurl_patient_id) {
            Intent intent = new Intent(Prescription_Add.this, HomeActivity.class);
            startActivity(intent);
            this.finish();

        }

    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.patient_save_button:
                // Log.e("TEST", "SAMPLE" + symptoms.getSelectedItem().toString());
                try {
                    if (instructions.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Please fill the mandatory fields ", Toast.LENGTH_LONG).show();


                    } else {
                        String selectedDrugs = savePocInfo();
                        if (selectedDrugs.length() == 0) {
                            new AlertDialog.Builder(Prescription_Add.this).setMessage("Do you want to proceed without Medication").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        getDataServicesave(poc.toString(), patientId, doctorId);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                        } else
                            try {
                                getDataServicesave(poc.toString(), patientId, doctorId);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                    }
                    // Toast.makeText(getApplicationContext(), "save", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private String savePocInfo() throws JSONException {
        String array = "";
        for (DrugListView drugListView : druglistview) {
            String drugData = drugListView.getDrug() + "-" + drugListView.getDirect() + "-" + drugListView.getDosage() + "-" + drugListView.getFrequency() + "-" + drugListView.getDays();
            array = array + (array.equalsIgnoreCase("") ? "" : ",") + drugData;
        }

        if (existingdrugs != null && existingdrugs.trim().length() != 0) {
            existingdrugs = existingdrugs + "," + array;
        } else
            existingdrugs = array;

        poc.put("druglist", existingdrugs);

        JSONObject problemJson = new JSONObject();
        problemJson.put("symtoms", getIntent().getStringExtra("symtoms"));
        problemJson.put("ros", getIntent().getStringExtra("ros"));
        problemJson.put("selectedroslist", getIntent().getStringExtra("selectedroslist"));
        problemJson.put("instruction", instructions.getText().toString());
        problemsArray.put(problemJson);
        poc.put("problems", problemsArray.toString());
        poc.put("patientgender", getIntent().getStringExtra("patientgender"));
        poc.put("patientage", getIntent().getStringExtra("patientage"));
        poc.put("patientname", getIntent().getStringExtra("patientname"));
        poc.put("pharmacistname", getIntent().getStringExtra("pharmacistname"));
        poc.put("pharmacyid", getIntent().getStringExtra("pharmacyid"));
        poc.put("patientaddress2", getIntent().getStringExtra("patientaddress2"));
        poc.put("patientaddress1", getIntent().getStringExtra("patientaddress1"));
        poc.put("patientnum", getIntent().getStringExtra("patientnum"));
        poc.put("patientemail", getIntent().getStringExtra("patientemail"));
        poc.put("doctorName", getIntent().getStringExtra("doctorName"));
        poc.put("date", getIntent().getStringExtra("date"));
        poc.put("patientid",  getIntent().getStringExtra("patientid"));
        poc.put("doctorid", getIntent().getStringExtra("doctorid"));
        poc.put("anotherchiefcomplaint", getIntent().getStringExtra("anotherchiefcomplaint"));
        poc.put("pastmedical", getIntent().getStringExtra("pastmedical"));
        poc.put("familyhistory", getIntent().getStringExtra("familyhistory"));
        poc.put("allergy", getIntent().getStringExtra("allergy"));
        poc.put("secondros", getIntent().getStringExtra("secondros"));
        poc.put("provisional", getIntent().getStringExtra("provisional"));
        poc.put("investigations", getIntent().getStringExtra("investigations"));
        poc.put("pulse",getIntent().getStringExtra("pulse"));
        poc.put("bp",getIntent().getStringExtra("bp"));
        poc.put("temperature",getIntent().getStringExtra("temperature"));
        poc.put("weight",getIntent().getStringExtra("weight"));

        Log.d("finalpoc", "poc" + poc.toString());
        return existingdrugs;
    }


    private void getDataServicesave(String poc, String patientId, String doctorId) throws JSONException {
        HttpAgent asyncTaskData = new HttpAgent(this);
        List<NameValuePair> _params = new ArrayList<NameValuePair>();
        _params.add(new BasicNameValuePair("poc", poc.toString()));
        _params.add(new BasicNameValuePair("doc", doctorId));
        _params.add(new BasicNameValuePair("patient", patientId));
        String encodedParams = URLEncodedUtils.format(_params, "utf-8");
        Data data = new Data();
        data.setBaseUrl(UrlList.poc_save_url_patient + encodedParams);
        data.setUrlId(UrlList.pocsaveurl_patient_id);
        data.setIsGET(true);
        data.setParams(new JSONObject());
        asyncTaskData.executeWs(data);
    }


    @Override
    public void onBackPressed() {


        new AlertDialog.Builder(Prescription_Add.this).setMessage("Do you want to clear prescription data").setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent in=new Intent(Prescription_Add.this,POCActivity.class);
                in.putExtra("presurl",url);
                in.putExtra("presdoctorName", doctorName);
                in.putExtra("prespatientid", patientId);
                in.putExtra("presdoctorid", doctorId);
                Log.e("doctorname","++"+doctorName+patientId+doctorId);
                startActivity(in);

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();


    }
}

*/
