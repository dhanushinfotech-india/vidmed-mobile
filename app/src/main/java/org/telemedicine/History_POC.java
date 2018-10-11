package org.telemedicine;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.Adapter.DrugListAdapter;

import java.util.ArrayList;

/**
 * Created by hareesh on 9/6/16.
 */
public class History_POC extends SuperActivity {

    Spinner poc_history;
    Button close;
    ImageView logoutbtn;
    ArrayAdapter dateadapter;
    String gender, age, name, phone, pocdate, doctorname, pharmacistname, pharmaid, patientid, doctorid, historyros, historypocarray, cheifcomponentone, cheifcomponentsecond, historydruglist;
    String historychief_first, historychief_second, historyros_first, historyros_second, historysecondros_list;
    JSONArray hist_pocarray, historyarray;
    ArrayList date_array = new ArrayList();
    ArrayList<DrugListView> drugListViews;
    ArrayList<String> historyfirstchiefroslist, historysecondchiefroslist, historyinstructionlist, historysecondros;
    ArrayAdapter historyfirstroslistAdapter, historysecondroslistAdapter, historyinstructionlistAdapter, historysecondrosadapter;
    DrugListAdapter druglistadapter;
    TextView patient_id, patient_name, patient_age, patient_gender, patient_phoneno, patient_date, doctor_name,
            pharmacist_name, chiefcomplaint_first, ros_first, cheifcomplaint_second, ros_second, history_provisional,
            history_group, history_subgroup, history_investigationname,instruction_poc;
    ListView drug_listview, first_poc, second_poc,history_secondros;
    String[] array;


    @Override
    protected void onCreation(Bundle savedInstanceState) {
        setContentView(R.layout.history_poc);
        drugListViews = new ArrayList<>();
        date_array = new ArrayList();
        historyfirstchiefroslist = new ArrayList<>();
        historysecondchiefroslist = new ArrayList<>();
       // historyinstructionlist = new ArrayList<>();
        historysecondros = new ArrayList<>();
        array = new String[]{};
        poc_history = (Spinner) findViewById(R.id.ed_history);
        patient_id = (TextView) findViewById(R.id.et_id);
        patient_name = (TextView) findViewById(R.id.et_name);
        patient_age = (TextView) findViewById(R.id.et_age);
        patient_gender = (TextView) findViewById(R.id.et_gender);
        patient_phoneno = (TextView) findViewById(R.id.et_phn);
        patient_date = (TextView) findViewById(R.id.et_date);
        doctor_name = (TextView) findViewById(R.id.et_historydoctor_name);
        pharmacist_name = (TextView) findViewById(R.id.et_pharmacist_name);
        chiefcomplaint_first = (TextView) findViewById(R.id.historychief_complaint_one);
        cheifcomplaint_second = (TextView) findViewById(R.id.historychief_complaint_second);
        ros_first = (TextView) findViewById(R.id.historyros_one);
        ros_second = (TextView) findViewById(R.id.historyros_second);
        history_provisional = (TextView) findViewById(R.id.history_provisional_dia);
        history_group = (TextView) findViewById(R.id.history_groupname);
        history_subgroup = (TextView) findViewById(R.id.history_subgroupname);
        history_investigationname = (TextView) findViewById(R.id.history_investigationname);
        drug_listview = (ListView) findViewById(R.id.drug_listview);
        first_poc = (ListView) findViewById(R.id.historyfirst_poc);
        second_poc = (ListView) findViewById(R.id.historysecond_poc);
        instruction_poc = (TextView) findViewById(R.id.historyinstruction_one);
        history_secondros = (ListView) findViewById(R.id.history_second_ros);
        close = (Button) findViewById(R.id.bt_history_close);
        logoutbtn= (ImageView) findViewById(R.id.logout_image);

        gender = getIntent().getStringExtra("patientgender");
        age = getIntent().getStringExtra("patientage");
        name = getIntent().getStringExtra("patientname");
        phone = getIntent().getStringExtra("patientnum");
        pocdate = getIntent().getStringExtra("date");
        doctorname = getIntent().getStringExtra("doctorName");
        pharmacistname = getIntent().getStringExtra("pharmacistname");
        pharmaid = getIntent().getStringExtra("pharmacyid");
        patientid = getIntent().getStringExtra("patientid");
        doctorid = getIntent().getStringExtra("doctorid");
        historypocarray = getIntent().getStringExtra("pocarray");


        patient_id.setText(patientid);
        patient_name.setText(name);
        patient_age.setText(age);
        patient_gender.setText(gender);
        patient_phoneno.setText(phone);
        doctor_name.setText(doctorname);
        pharmacist_name.setText(pharmacistname);

        date_array = getIntent().getStringArrayListExtra("historydate");
        dateadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, date_array);
        poc_history.setAdapter(dateadapter);
        poc_history.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                drugListViews.clear();
                historyfirstchiefroslist.clear();
                historysecondchiefroslist.clear();
               // historyinstructionlist.clear();
                historysecondros.clear();


                druglistadapter.notifyDataSetChanged();
                historyfirstroslistAdapter.notifyDataSetChanged();
                historysecondroslistAdapter.notifyDataSetChanged();
              //  historyinstructionlistAdapter.notifyDataSetChanged();
                historysecondrosadapter.notifyDataSetChanged();
                loadData(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        druglistadapter = new DrugListAdapter (this, drugListViews);
        drug_listview.setAdapter(druglistadapter);
        historyfirstroslistAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, historyfirstchiefroslist);
        historysecondroslistAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, historysecondchiefroslist);
      //  historyinstructionlistAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, historyinstructionlist);
        historysecondrosadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, historysecondros);
        first_poc.setAdapter(historyfirstroslistAdapter);
        second_poc.setAdapter(historysecondroslistAdapter);
       // instruction_poc.setAdapter(historyinstructionlistAdapter);
        history_secondros.setAdapter(historysecondrosadapter);


        drug_listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        first_poc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        second_poc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                History_POC.this.finish();

            }
        });
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void loadData(int selectPoc) {

        try {
            hist_pocarray = new JSONArray(historypocarray);
            JSONObject obj = new JSONObject(hist_pocarray.getString(selectPoc));
            patient_date.setText(getDataFromJsonObj(obj, "date"));
            historydruglist = getDataFromJsonObj(obj, "druglist");
            history_provisional.setText(getDataFromJsonObj(obj, "provisional"));
            history_group.setText(getDataFromJsonObj(obj, "groupname"));
            history_subgroup.setText(getDataFromJsonObj(obj, "subgroupname"));
            history_investigationname.setText(getDataFromJsonObj(obj, "investigationname"));
            historysecondros_list = getDataFromJsonObj(obj, "secondros");
            String[] history_rossecond = historysecondros_list.replace("[", "").replace("]", "").split(",");
            for (int i = 0; i < history_rossecond.length; i++) {

                historysecondros.add(history_rossecond[i]);

            }
            if (historydruglist.trim().length() != 0) {
                ((LinearLayout) findViewById(R.id.history_druglist)).setVisibility(View.VISIBLE);
                String[] historydrugs = historydruglist.split(",");


                for (int i = 0; i < historydrugs.length; i++) {
                    String drugInfo = historydrugs[i];
                    try {
                        drugInfo = (new JSONArray(historydrugs[i])).get(0).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String[] drugDetail = drugInfo.split("_");
                    if(drugDetail.length>4)
                        drugListViews.add(new DrugListView(drugDetail[0], drugDetail[1], drugDetail[2], drugDetail[3], drugDetail[4],drugDetail[5]));

                }


              /*  for (int i = 0; i < historydrugs.length; i++) {
                    String drugInfo = historydrugs[i];
                    String[] drugDetail = drugInfo.split("_");
                    drugListViews.add(new DrugListView(drugDetail[0], drugDetail[1], drugDetail[2], drugDetail[3], drugDetail[4]));

                }*/
            }
            historyarray = new JSONArray(obj.getString("problems"));
            JSONObject prbobjs = historyarray.getJSONObject(0);

            historychief_first = prbobjs.getString("symtoms");
            chiefcomplaint_first.setText(historychief_first);
            historyros_first = prbobjs.getString("ros");
            ros_first.setText(historyros_first);
            array = prbobjs.getString("selectedroslist").replace("[", "").replace("]", "").split(",");
            JSONArray array1 = new JSONArray(array);

            for (int i = 0; i < array1.length(); i++) {

                historyfirstchiefroslist.add(array1.getString(i));


            }

           // historyinstructionlist.add(prbobjs.getString("instruction"));
            instruction_poc.setText(prbobjs.getString("doctorNote"));
/*
            if (historyarray.length() > 1) {
                ((LinearLayout) findViewById(R.id.historylayout_second_poc)).setVisibility(View.VISIBLE);

                JSONObject prof2 = historyarray.getJSONObject(1);
                historychief_second = prof2.getString("symtoms");
                cheifcomplaint_second.setText(historychief_second);
                historyros_second = prof2.getString("ros");
                ros_second.setText(historyros_second);
                array = prof2.getString("selectedroslist").replace("[", "").replace("]", "").split(",");
                JSONArray array2 = new JSONArray(array);


                for (int i = 0; i < array2.length(); i++) {

                    historysecondchiefroslist.add(array2.getString(i));


                }
                historyinstructionlist.add(prof2.getString("instruction"));
            } else
                ((LinearLayout) findViewById(R.id.historylayout_second_poc)).setVisibility(View.GONE);*/


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

    }
}
