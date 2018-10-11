/*
package org.telemedicine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.Adapter.PendingDrugListAdapter;
import org.telemedicine.Web.Data;
import org.telemedicine.Web.HttpAgent;
import org.telemedicine.Web.JsonResult;
import org.telemedicine.Web.UrlList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

*
 * Created by hareesh on 8/12/16.


public abstract class POCActivity extends SuperActivity implements JsonResult {
    EditText chiefcomplaintedit, pastmedical, family, allergy, provisional, investigation, pulse, bp, temperature, weight;
    Spinner symptoms, servicegroupname, servicesubgroupname, serviceinvestigationname;
    CheckBox roscheckbox;
    final SparseBooleanArray mSelectedItemsIds = new SparseBooleanArray();
    ImageView plus, history, addchiefcomplaint, addpastmedical, addfamily, addallergy, closepastmedical, closefamily, closeallergy;
    String date;
    Calendar dt;
    EditText drug, direct, dosage, frequency, instructions, eddays;
    ArrayList<DrugListView> druglistview;
    String select_drug, select_direct, select_dosage, select_frequency, comment, poc_days;
    Button save;
    PendingDrugListAdapter druglistadapter;
    ArrayList<DrugListView> prescr_druglist;
    String prescr_instruction;
    ListView drug_list, roslist, roslist2;
    JSONArray Previousarray;
    LinearLayout layout_header;
    ArrayList historydates = new ArrayList();
    String age, patientname, pharmacistname, pharmacyid, patientAddrs2, patientAddrs1, patientnum, patientemail, gender;
    TextView tvpatientname, tvage, tvgender, tvpatientid, tvpharmaname, tv_doctorname, ros;
    ArrayAdapter rosAdapter2;
    ArrayList<String> selectedroslist = new ArrayList<>();
    ArrayList<String> selectedroslist2 = new ArrayList<>();
    ArrayList<String> complaints = new ArrayList<>();
    ArrayList<String> listofros = new ArrayList<>();
    ArrayList<String> listofros2 = new ArrayList<>();
    ArrayList<String> groupname = new ArrayList<>();
    ArrayList<String> subgroupname = new ArrayList<>();
    ArrayList<String> investigation_name = new ArrayList<>();
    ArrayAdapter complaintAdapter, rosAdapter,groupnameadapter,subgroupnameadapter,investigation_nameadapter;
    String[] array, array2;


    String existingpoc, existingdrugs, prescription;
    JSONObject rosobj, profileInfoJson, js;
    JSONArray problemsArray;
    JSONObject investigationObject,obj;
    public JSONObject poc = new JSONObject();
    TextView tvheading;
    String url, patientId, doctorId, doctorName;
    JSONObject selectedRos;

    @Override
    protected void onCreation(Bundle saveInstanceState) {
        setContentView(R.layout.poc_screen_sample);
        problemsArray = new JSONArray();
        final Bundle bundle = getIntent().getExtras();
        selectedRos = new JSONObject();
        View headerview = findViewById(R.id.page_name);
        tvheading = (TextView) headerview.findViewById(R.id.tvheading);
        layout_header = (LinearLayout) findViewById(R.id.header);
        array = new String[]{};
        if (bundle != null) {
            url = bundle.getString("url");
            doctorName = getUserName();
            patientId = bundle.getString("patientId");
            doctorId = bundle.getString("doctorId");
        }

        roslist2 = (ListView) findViewById(R.id.all_ros_list);
        tv_doctorname = (TextView) findViewById(R.id.htv_docname);
        tv_doctorname.setText(getUserName());
        instructions = (EditText) findViewById(R.id.patient_instructions);
        chiefcomplaintedit = (EditText) findViewById(R.id.ed_another_cheifcomplaints);
        pastmedical = (EditText) findViewById(R.id.ed_past_medical);
        family = (EditText) findViewById(R.id.ed_family_history);
        allergy = (EditText) findViewById(R.id.ed_allergy);
        provisional = (EditText) findViewById(R.id.ed_provisional);
        //investigation = (EditText) findViewById(R.id.ed_invest);
        pulse = (EditText) findViewById(R.id.pulse_edit);
        bp = (EditText) findViewById(R.id.bp_edit);
        temperature = (EditText) findViewById(R.id.temperature_edit);
        weight = (EditText) findViewById(R.id.weight_edit);
        tvpatientname = (TextView) findViewById(R.id.text_patient_name);
        tvage = (TextView) findViewById(R.id.text_patient_age);
        tvgender = (TextView) findViewById(R.id.text_patient_gender);
        tvpatientid = (TextView) findViewById(R.id.text_patient_id);
        history = (ImageView) findViewById(R.id.img);
        addchiefcomplaint = (ImageView) findViewById(R.id.plus_image2);
        addpastmedical = (ImageView) findViewById(R.id.plus_image3);
        closepastmedical = (ImageView) findViewById(R.id.plus_image4);
        addfamily = (ImageView) findViewById(R.id.plus_image5);
        closefamily = (ImageView) findViewById(R.id.plus_image6);
        addallergy = (ImageView) findViewById(R.id.plus_image7);
        closeallergy = (ImageView) findViewById(R.id.plus_image8);
        tvpharmaname = (TextView) findViewById(R.id.htext_pharmacist_name);
        roscheckbox = (CheckBox) findViewById(R.id.secondros_checkbox);
addpoc = (Button) findViewById(R.id.another_poc);
        addprescription = (Button) findViewById(R.id.patient_prescription_button);

Bundle extras = getIntent().getExtras();
        if (extras != null) {

            pres_url=extras.getString("presurl");
            if(pres_url!=null)
                existingpoc="";
            pres_doctorName=extras.getString("presdoctorName");
            pres_doctorId=extras.getString("presdoctorid");
            pres_patientId=extras.getString("prespatientid");
            Log.e("patientid","+"+pres_doctorName+pres_patientId+pres_doctorId);
        }


        if (existingpoc == null) {
            Intent _broswer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(_broswer);
            try {
                getDataService();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

        }
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (profileInfoJson.has("poc") && profileInfoJson.getJSONArray("poc").length() != 0) {
                        Intent in = new Intent(POCActivity.this, History_POC.class);
                        in.putExtra("patientgender", gender);
                        in.putExtra("patientage", age);
                        in.putExtra("patientname", patientname);
                        in.putExtra("pharmacistname", pharmacistname);
                        in.putExtra("pharmacyid", pharmacyid);
                        in.putExtra("patientaddress2", patientAddrs2);
                        in.putExtra("patientaddress1", patientAddrs1);
                        in.putExtra("patientnum", patientnum);
                        in.putExtra("patientemail", patientemail);
                        in.putExtra("doctorName", doctorName);
                        in.putExtra("date", date);
                        in.putExtra("patientid", patientId);
                        in.putExtra("doctorid", doctorId);
                        in.putExtra("pocarray", Previousarray.toString());
                        in.putExtra("historydate", historydates);
                        startActivity(in);
                    } else
                        Toast.makeText(POCActivity.this, "No Previous Visits", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
 addprescription.setOnClickListener(this);
        addpoc.setOnClickListener(this);

        View view = findViewById(R.id.lv_drugs);
        drug_list = (ListView) view.findViewById(R.id.drug_listview);
        eddays = (EditText) findViewById(R.id.patient_days);
        drug = (EditText) findViewById(R.id.drug_edit);
        direct = (EditText) findViewById(R.id.direct_edit);
        dosage = (EditText) findViewById(R.id.dosage_edit);
        frequency = (EditText) findViewById(R.id.frequency_edit);
        symptoms = (Spinner) findViewById(R.id.symtoms_spinner);
        servicegroupname = (Spinner) findViewById(R.id.groupname_spinner);
        servicesubgroupname = (Spinner) findViewById(R.id.subgroupname_spinner);
        serviceinvestigationname = (Spinner) findViewById(R.id.investigationname_spinner);
        ros = (TextView) findViewById(R.id.ros);
        roslist = (ListView) findViewById(R.id.tv_ros);
        listofros = new ArrayList<>();
        groupnameadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, groupname);
        subgroupnameadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, subgroupname);
         investigation_nameadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, investigation_name);
       servicegroupname.setAdapter(groupnameadapter);
        servicesubgroupname.setAdapter(subgroupnameadapter);
        serviceinvestigationname.setAdapter(investigation_nameadapter);
        rosAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, listofros);
        rosAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listofros2);
        roslist2.setAdapter(rosAdapter2);
        roslist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        roslist.setAdapter(rosAdapter);
        roslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (!mSelectedItemsIds.get(position)) {
                    roslist.setItemChecked(position, true);
                    mSelectedItemsIds.put(position, true);
                    selectedroslist.add(array[position]);
                    Log.d("roslist", "123456" + selectedroslist.toString());
                    Toast.makeText(POCActivity.this, "" + array[position], Toast.LENGTH_SHORT).show();
                } else {
                    mSelectedItemsIds.delete(position);
                    selectedroslist.remove(array[position]);
                    roslist.setItemChecked(position, false);
                }


            }
        });
        roslist2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(POCActivity.this);
                builder.setTitle("ROS List");
               // final String selected = complaints.get(position + 1).toString();

                try {
                    //builder.setMessage(rosobj.getJSONObject(complaints.get(position+1).toString()).getString("list_ros"));
                    array2 = rosobj.getJSONObject(complaints.get(position + 1).toString()).getString("list_ros").split(",");
                    boolean isSelected[]=isSelected=new boolean[array2.length];

                    for (int i=0;i<isSelected.length;i++){
                        isSelected[i]=selectedroslist2.contains(array2[i]);
                    }

                    builder.setMultiChoiceItems(array2, isSelected, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                            if (isChecked) {
                                selectedroslist2.add(array2[indexSelected]);
                                Log.d("selected", "=-=====================" + selectedroslist2.toString());
                            } else if (selectedroslist2.contains(array2[indexSelected])) {
                                selectedroslist2.remove((array2[indexSelected]));
                                Log.d("selected", "=-========unchek=============" + selectedroslist2.toString());
                            }
                            Log.d("selected", "=-=====================" + selectedroslist2.toString());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                builder.setCancelable(true);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
try {
                            JSONArray subArray = new JSONArray();
                            for (String subRos : selectedroslist2) {
                                subArray.put(subRos);
                            }
                            if(selectedRos.has(selected))
                            selectedRos.put(selected, subArray.toString());
                            selectedroslist2.clear();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
                builder.create();
                builder.show();

            }
        });


        symptoms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listofros.clear();
                try {
                    if (!symptoms.getSelectedItem().toString().equalsIgnoreCase("select")) {

                        ros.setText(rosobj.getJSONObject(String.valueOf(symptoms.getSelectedItem())).getString("ros"));
                        array = rosobj.getJSONObject(String.valueOf(symptoms.getSelectedItem()))
                                .getString("list_ros").split(",");
                        for (int i = 0; i < array.length; i++) {
                            listofros.add(array[i]);
                        }
                        for (int k = 0; k < roslist.getAdapter().getCount(); k++) {
                            roslist.setItemChecked(k, false);
                        }
                    } else {
                        ros.setText("");
                    }
                    rosAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("nothing selected", "===============");
            }
        });




        servicegroupname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //servicegroupname.setSelection((position));
                subgroupname.clear();
                try {
                    if (!servicegroupname.getSelectedItem().toString().equalsIgnoreCase("select")) {

                        obj = investigationObject.getJSONObject(servicegroupname.getSelectedItem().toString());
                        Log.e("obj", "test" + obj);
                        Iterator iterato = obj.keys();
                        while (iterato.hasNext()) {
                            String key = (String) iterato.next();
                            if (subgroupname.size() == 0) {
                                subgroupname.add("Select");
                            }
                            subgroupname.add(key);
                        }
                    }
                    else
                    servicesubgroupname.setSelection(0);

                    subgroupnameadapter.notifyDataSetChanged();
                    Log.d("cou===============",subgroupnameadapter.getCount()+"");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                servicesubgroupname.setSelection(0);
                serviceinvestigationname.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        servicesubgroupname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                investigation_name.clear();
                investigation_name.add("Select");
                try {
                    if (!servicesubgroupname.getSelectedItem().toString().equalsIgnoreCase("select")) {

                        JSONArray arr = investigationObject.getJSONObject(servicegroupname.getSelectedItem().toString()).getJSONArray(subgroupname.get(servicesubgroupname.getSelectedItemPosition()));
                        Log.e("arr", "test" + arr);
                        for (int i = 0; i < arr.length(); i++) {
                            investigation_name.add(arr.getString(i));
                        }
                    }
                    else
                    serviceinvestigationname.setSelection(0);
                investigation_nameadapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        roslist2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        roslist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        addchiefcomplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.ed_cheifcomplaint)).setVisibility(View.VISIBLE);
            }
        });

        addpastmedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.tv_past_medical)).setVisibility(View.VISIBLE);
            }
        });
        addfamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.tv_family)).setVisibility(View.VISIBLE);
            }
        });

        addallergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.tv_allergy)).setVisibility(View.VISIBLE);

            }
        });


        closepastmedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.tv_past_medical)).setVisibility(View.GONE);
                pastmedical.setText("");
            }
        });


        closefamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.tv_family)).setVisibility(View.GONE);
                family.setText("");
            }
        });

        closeallergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LinearLayout) findViewById(R.id.tv_allergy)).setVisibility(View.GONE);
                allergy.setText("");
            }
        });

        save = (Button) findViewById(R.id.patient_save_button);
        plus = (ImageView) findViewById(R.id.plus_image1);
        complaintAdapter = new ArrayAdapter(POCActivity.this, android.R.layout.simple_list_item_1, complaints);
        symptoms.setAdapter(complaintAdapter);
        druglistview = new ArrayList<DrugListView>();
        druglistadapter = new PendingDrugListAdapter(this, druglistview);
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
                        Toast.makeText(POCActivity.this, "Please fill all drugs field", Toast.LENGTH_SHORT).show();

                    } else if (eddays.getText().length() == 0) {

                        Toast.makeText(POCActivity.this, "Enter Number Of Days", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(POCActivity.this, "Please fill valid drug", Toast.LENGTH_SHORT).show();
            }
        });
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        date = df.format(Calendar.getInstance().getTime());
        comment = instructions.getText().toString();
        poc_days = eddays.getText().toString();
        select_drug = drug.getText().toString();
        select_direct = direct.getText().toString();
        select_dosage = dosage.getText().toString();
        select_frequency = frequency.getText().toString();


    }


    private void getDataService() throws JSONException {
        HttpAgent asyncTaskData = new HttpAgent(this);
        Data data = new Data();
        data.setBaseUrl(String.format(UrlList.poc_url_patient + patientId));
        data.setUrlId(UrlList.pocurl_patient_id);
        data.setIsGET(true);
        data.setParams(new JSONObject());
        asyncTaskData.executeWs(data);
    }

    private void getDataServicedrug() throws JSONException {
        HttpAgent asyncTaskData = new HttpAgent(this);
        Data data = new Data();
        data.setBaseUrl(String.format(UrlList.drug_url_patient));
        data.setUrlId(UrlList.drugurl_patient_id);
        data.setIsGET(true);
        data.setParams(new JSONObject());
        asyncTaskData.executeWs(data);
    }

    @Override
    public void getData(JSONArray response, int urlID) throws JSONException {
        if (urlID == UrlList.pocurl_patient_id) {
            JSONArray patient_info = response.getJSONObject(0).getJSONArray("patientsinformation");
            profileInfoJson = patient_info.getJSONObject(0);

            Previousarray = profileInfoJson.getJSONArray("poc");

            for (int i = 0; i < Previousarray.length(); i++) {

                JSONObject obj = new JSONObject(Previousarray.getString(i));

                historydates.add("POC on  " + obj.getString("date"));

            }


            //Pass profileInfoJson to intent
            parsePatientInfo(profileInfoJson);
            getDataServicedrug();

        } else if (urlID == UrlList.drugurl_patient_id) {

            JSONObject jsonObject = response.getJSONObject(0);
            investigationObject = jsonObject.getJSONObject("investigation_data");
            parseDrugArray(investigationObject);
            //pass to Intent

        } else if (urlID == UrlList.pocsaveurl_patient_id) {
            Intent intent = new Intent(POCActivity.this, HomeActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

   private void parseDrugArray(JSONObject investigation) throws JSONException {

        groupname.clear();
       Iterator iterat = investigation.keys();
       while (iterat.hasNext()) {
           String key = (String) iterat.next();
           if (groupname.size() == 0) {
               groupname.add("Select");
           }
           groupname.add(key);
       }
       groupnameadapter.notifyDataSetChanged();


    }

    private void parsePatientInfo(JSONObject patientInfoJson) throws JSONException {
        gender = getGender(patientInfoJson.getString("gender"));
        age = patientInfoJson.getString("age");
        patientname = (patientInfoJson.getString("fname")) + (patientInfoJson.getString("lname"));
        pharmacistname = patientInfoJson.getString("pharmacist_name");
        pharmacyid = patientInfoJson.getString("pharmacist_id");
        patientAddrs2 = patientInfoJson.getString("addr2");
        patientAddrs1 = patientInfoJson.getString("addr1");
        patientnum = patientInfoJson.getString("pnumber");
        patientemail = patientInfoJson.getString("email");
        tvpatientname.setText(patientname);
        tvgender.setText(gender);
        tvage.setText(age);
        tvpatientid.setText(patientId);
        tvpharmaname.setText(pharmacistname);
        rosobj = patientInfoJson.getJSONObject("ros");
        Log.d("rosonject", "=======" + rosobj);

        Iterator iterator = rosobj.keys();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (complaints.size() == 0) {
                complaints.add("Select");

            }
String[] keys=key.split(",");
            for (int i = 0; i <keys.length ; i++) {
                complaints.add(keys[i]);

            }

            complaints.add(key);
            listofros2.add((rosobj.getJSONObject(key).getString("ros")));

        }
        rosAdapter2.notifyDataSetChanged();
        complaintAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
 case R.id.patient_prescription_button:


                if (symptoms.getSelectedItem().toString().equalsIgnoreCase("Select") || provisional.getText().length() == 0 || investigation.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please fill the mandatory fields ", Toast.LENGTH_LONG).show();
                } else if (selectedroslist2.size() == 0 && !(roscheckbox.isChecked())) {

                    Toast.makeText(this, "Please choose ROS or CheckBox", Toast.LENGTH_SHORT).show();

                } else if (((LinearLayout) findViewById(R.id.tv_past_medical)).getVisibility() == View.VISIBLE && pastmedical.getText().length() == 0) {

                    Toast.makeText(POCActivity.this, "Please Fill Past Medical Condition Editbox", Toast.LENGTH_SHORT).show();

                } else if (((LinearLayout) findViewById(R.id.tv_family)).getVisibility() == View.VISIBLE && family.getText().length() == 0) {

                    Toast.makeText(POCActivity.this, "Please Fill Family History Editbox", Toast.LENGTH_SHORT).show();
                } else if (((LinearLayout) findViewById(R.id.tv_allergy)).getVisibility() == View.VISIBLE && allergy.getText().length() == 0) {

                    Toast.makeText(POCActivity.this, "Please Fill Drugs/Allergy Editbox", Toast.LENGTH_SHORT).show();

                } else {
                    Intent i = new Intent(this, Prescription_Add.class);
                    i.putExtra("symtoms", symptoms.getSelectedItem().toString());
                    i.putExtra("ros", ros.getText().toString());
                    i.putExtra("selectedroslist", selectedroslist.toString());
                    i.putExtra("patientgender", gender);
                    i.putExtra("patientage", age);
                    i.putExtra("patientname", patientname);
                    i.putExtra("pharmacistname", pharmacistname);
                    i.putExtra("pharmacyid", pharmacyid);
                    i.putExtra("patientaddress2", patientAddrs2);
                    i.putExtra("patientaddress1", patientAddrs1);
                    i.putExtra("patientnum", patientnum);
                    i.putExtra("patientemail", patientemail);
                    i.putExtra("date", date);
                    i.putExtra("url",url);
                    i.putExtra("doctorName", doctorName);
                    i.putExtra("patientid", patientId);
                    i.putExtra("doctorid", doctorId);
                    i.putExtra("anotherchiefcomplaint", chiefcomplaintedit.getText().toString());
                    i.putExtra("pastmedical", pastmedical.getText().toString());
                    i.putExtra("familyhistory", family.getText().toString());
                    i.putExtra("allergy", allergy.getText().toString());
                    i.putExtra("pulse", pulse.getText().toString());
                    i.putExtra("bp", bp.getText().toString());
                    i.putExtra("temperature", temperature.getText().toString());
                    i.putExtra("weight", weight.getText().toString());
                    i.putExtra("secondros", selectedroslist2.size() != 0 ? selectedroslist2 : "All Other Systems has been Checked None");
                    i.putExtra("provisional", provisional.getText().toString());
                    i.putExtra("investigations", investigation.getText().toString());
                    startActivity(i);
                }

                break;


            case R.id.patient_save_button:
                Log.e("TEST", "SAMPLE" + symptoms.getSelectedItem().toString());
                try {
                    if (symptoms.getSelectedItem().toString().equalsIgnoreCase("Select") || instructions.getText().toString().equalsIgnoreCase("") || provisional.getText().length() == 0 || servicegroupname.getSelectedItem().toString().equalsIgnoreCase("select")
                            ||servicesubgroupname.getSelectedItem().toString().equalsIgnoreCase("select")||serviceinvestigationname.getSelectedItem().toString().equalsIgnoreCase("select")) {
                        Toast.makeText(getApplicationContext(), "Please fill the mandatory fields ", Toast.LENGTH_LONG).show();
                    }
else if (chiefcomplaintedit.getText().length() == 0) {

                        Toast.makeText(POCActivity.this, "Please Fill Chief Complaint Editbox", Toast.LENGTH_SHORT).show();

                    }
 else if (selectedroslist2.size() == 0 && !(roscheckbox.isChecked())) {

                        Toast.makeText(this, "Please choose ROS or CheckBox", Toast.LENGTH_SHORT).show();

                    } else if (((LinearLayout) findViewById(R.id.tv_past_medical)).getVisibility() == View.VISIBLE && pastmedical.getText().length() == 0) {

                        Toast.makeText(POCActivity.this, "Please Fill Past Medical Condition Editbox", Toast.LENGTH_SHORT).show();

                    } else if (((LinearLayout) findViewById(R.id.tv_family)).getVisibility() == View.VISIBLE && family.getText().length() == 0) {

                        Toast.makeText(POCActivity.this, "Please Fill Family History Editbox", Toast.LENGTH_SHORT).show();
                    } else if (((LinearLayout) findViewById(R.id.tv_allergy)).getVisibility() == View.VISIBLE && allergy.getText().length() == 0) {

                        Toast.makeText(POCActivity.this, "Please Fill Drugs/Allergy Editbox", Toast.LENGTH_SHORT).show();

                    } else {
                        String selectedDrugs = savePocInfo();
                        if (selectedDrugs.length() == 0) {
                            new AlertDialog.Builder(POCActivity.this).setMessage("Do you want to proceed without Medication").setPositiveButton("yes", new DialogInterface.OnClickListener() {
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
 case R.id.another_poc:
                Toast.makeText(POCActivity.this, "Adding Another POC", Toast.LENGTH_SHORT).show();
                try {
                    if (symptoms.getSelectedItem().toString().equalsIgnoreCase("Select") || instructions.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Please fill the mandatory fields ", Toast.LENGTH_LONG).show();
                    } else {
                        String selectedDrugs = savePocInfo();
                        if (selectedDrugs.length() == 0) {
                            new AlertDialog.Builder(POCActivity.this).setMessage("Do you want to proceed without Medication").setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    tvheading.setText("Another Plan of Care");
                                    clearData();
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                        } else {
                            tvheading.setText("Another Plan of Care");
                            clearData();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }

    }


 private void clearData() {
        symptoms.setSelection(0);
        drug.setText("");
        direct.setText("");
        dosage.setText("");
        frequency.setText("");
        eddays.setText("");
        instructions.setText("");
        druglistview.clear();
        druglistadapter.notifyDataSetChanged();
        //addpoc.setVisibility(View.GONE);
        findViewById(R.id.drugs_list).setVisibility(View.GONE);
    }



    private String savePocInfo() throws JSONException {
        String array = "";
        for (DrugListView drugListView : druglistview) {
            String drugData = drugListView.getDrug() + "-" + drugListView.getDirect() + "-" + drugListView.getDosage() + "-" + drugListView.getFrequency() +"-"+drugListView.getInstruction()+ "-" + drugListView.getDays();
            array = array + (array.equalsIgnoreCase("") ? "" : ",") + drugData;
        }

        if (existingdrugs != null && existingdrugs.trim().length() != 0) {
            existingdrugs = existingdrugs + "," + array;
        } else
            existingdrugs = array;

        poc.put("druglist", existingdrugs);

        JSONObject problemJson = new JSONObject();
        problemJson.put("symtoms", symptoms.getSelectedItem().toString());
        problemJson.put("ros", ros.getText().toString());
        problemJson.put("selectedroslist", selectedroslist.toString());
        problemJson.put("instruction", instructions.getText().toString());
        problemsArray.put(problemJson);
        poc.put("problems", problemsArray.toString());
        poc.put("patientgender", gender);
        poc.put("patientage", age);
        poc.put("patientname", patientname);
        poc.put("pharmacistname", pharmacistname);
        poc.put("pharmacyid", pharmacyid);
        poc.put("patientaddress2", patientAddrs2);
        poc.put("patientaddress1", patientAddrs1);
        poc.put("patientnum", patientnum);
        poc.put("patientemail", patientemail);
        poc.put("doctorName", doctorName);
        poc.put("date", date);
        poc.put("patientid", patientId);
        poc.put("doctorid", doctorId);
        poc.put("anotherchiefcomplaint", chiefcomplaintedit.getText().toString());
        poc.put("pastmedical", pastmedical.getText().toString());
        poc.put("familyhistory", family.getText().toString());
        poc.put("allergy", allergy.getText().toString());
        poc.put("secondros", selectedroslist2.size() != 0 ? selectedroslist2 : "All Other Systems Has No Problem");
        poc.put("provisional", provisional.getText().toString());
        poc.put("groupname", servicegroupname.getSelectedItem());
        poc.put("subgroupname",servicesubgroupname.getSelectedItem());
        poc.put("investigationname",serviceinvestigationname.getSelectedItem());
        poc.put("pulse", getIntent().getStringExtra("pulse"));
        poc.put("bp", getIntent().getStringExtra("bp"));
        poc.put("temperature", getIntent().getStringExtra("temperature"));
        poc.put("weight", getIntent().getStringExtra("weight"));


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


    public abstract void onCreation();

    @Override
    public void onBackPressed() {

        Toast.makeText(this, "POC is mandatory", Toast.LENGTH_SHORT).show();
    }


}
*/
