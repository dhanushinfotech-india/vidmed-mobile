/*
package org.telemedicine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.Adapter.PendingDrugListAdapter;
import org.telemedicine.Web.JsonResult;

import java.util.ArrayList;

*/
/**
 * Created by hareesh on 8/16/16.
 *//*

public class POCViewActivity extends SuperActivity implements JsonResult{

    TextView patient_id, patient_name, patient_age, patient_gender, patient_phoneno, patient_date,
            doctor_name, chiefcomplaintfirst, rosfirst, cheifcomplaintsecond,
            rossecond,provisional,group_name,subgroup_name,investigation_name,instructionpoc ;
    Button close;
    ListView  firstpoc, secondpoc, secondros;
    ImageView patient_visit_logout;
    JSONArray viewproblemarray;
    String role1;

    */
/*Spinner poc_history;
    ArrayList  date_array;
    ArrayAdapter dateadapter;*//*

    String chief_first, chief_second, ros_first, ros_second, firstinstruction, secondinstruction;
    ArrayList<String> firstchiefroslist, secondchiefroslist, instructionlist,secondroslist;
    ArrayAdapter firstroslistAdapter, secondroslistAdapter, instructionlistAdapter,secondrosadapter;
//    ArrayList<DrugListView> drugListViews;
 //   PendingDrugListAdapter pendingdrugadapter;
    String[] array = new String[]{};
    public static ArrayList<DrugListView> drugListViews;
    public static ArrayList<DrugListView> pendingdrugslist;
    public static  PendingDrugListAdapter pendingdrugadapter;
    public static   EditDrugAdapter editdruglistadapter;
    public static ListView pendingdrug_listview,drug_listview;


    @Override
    protected void onCreation(Bundle savedInstanceState) {
        setContentView(R.layout.poc_view);
        role1 = getStrValueFromPref("role");
        drugListViews = new ArrayList<>();
        patient_id = (TextView) findViewById(R.id.et_pat_id);
        patient_name = (TextView) findViewById(R.id.et_patient_name);
        patient_age = (TextView) findViewById(R.id.et_pat_age);
        patient_gender = (TextView) findViewById(R.id.et_pat_gender);
        patient_phoneno = (TextView) findViewById(R.id.et_pat_phn);
        patient_date = (TextView) findViewById(R.id.et_plan_of_care_date);
        doctor_name = (TextView) findViewById(R.id.et_doctor_name);
        chiefcomplaintfirst = (TextView) findViewById(R.id.chief_complaint_one);
        cheifcomplaintsecond = (TextView) findViewById(R.id.chief_complaint_second);
        rosfirst = (TextView) findViewById(R.id.ros_one);
        rossecond = (TextView) findViewById(R.id.ros_second);
        provisional= (TextView) findViewById(R.id.provisional_dia);
        group_name= (TextView) findViewById(R.id.groupname);
        subgroup_name= (TextView) findViewById(R.id.subgroupname);
        investigation_name= (TextView) findViewById(R.id.investigationname);
        firstpoc = (ListView) findViewById(R.id.first_poc);
        drug_listview = (ListView) findViewById(R.id.drug_listview);
        secondros= (ListView) findViewById(R.id.second_ros);
        pendingdrug_listview= (ListView) findViewById(R.id.lv_pending);
        drugListViews = new ArrayList<>();
        pendingdrugslist=new ArrayList<>();

        pendingdrugadapter = new PendingDrugListAdapter(this,pendingdrugslist );
        editdruglistadapter=new EditDrugAdapter(this,drugListViews);
        drug_listview.setAdapter(editdruglistadapter);
        pendingdrug_listview.setAdapter(pendingdrugadapter);
        firstpoc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        secondpoc = (ListView) findViewById(R.id.second_poc);

        secondpoc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        instructionpoc = (TextView) findViewById(R.id.instruction_one);

        patient_visit_logout = (ImageView) findViewById(R.id.patient_vis_logout_image);
        patient_visit_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

       */
/* poc_history= (Spinner) findViewById(R.id.ed_history);

        date_array=new ArrayList();
        date_array=getIntent().getStringArrayListExtra("dates");
        dateadapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,date_array);
        poc_history.setAdapter(dateadapter);*//*



        String drugs = getIntent().getStringExtra("drugsrecommanded");
        Log.d("druglist", "view" + drugs);
        if(drugs.trim().length()!=0) {
            ((LinearLayout)findViewById(R.id.poc_view_druglist)).setVisibility(View.VISIBLE);
            String[] drugsData = drugs.split(",");

            for (int i = 0; i < drugsData.length; i++) {
                String drugInfo = drugsData[i];
                try {
                    drugInfo = (new JSONArray(drugsData[i])).get(0).toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String[] drugDetails = drugInfo.split("_");
                if(drugDetails.length>4)
                    drugListViews.add(new DrugListView(drugDetails[0], drugDetails[1], drugDetails[2], drugDetails[3], drugDetails[4] ,drugDetails[5]));

            }
            pendingdrugadapter.notifyDataSetChanged();

        }
        drug_listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        close = (Button) findViewById(R.id.bt_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (role1.equalsIgnoreCase("patient")) {
                   // Toast.makeText(getApplicationContext(),"Patient closing", Toast.LENGTH_SHORT).show();
                      finish();
                } else {
                    Intent in = new Intent(POCViewActivity.this, HomeActivity.class);
                    startActivity(in);
                }
            }
        });


//        patient_drugs.setAdapter(arrayAdapter);

        firstchiefroslist = new ArrayList<>();
        secondchiefroslist = new ArrayList<>();
       // instructionlist = new ArrayList<>();
        secondroslist=new ArrayList<>();


        firstroslistAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, firstchiefroslist);
        secondroslistAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, secondchiefroslist);
        //instructionlistAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, instructionlist);
        secondrosadapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,secondroslist);


        firstpoc.setAdapter(firstroslistAdapter);
        secondpoc.setAdapter(secondroslistAdapter);
      //  instructionpoc.setAdapter(instructionlistAdapter);
        secondros.setAdapter(secondrosadapter);


        String patient_secondros=getIntent().getStringExtra("patsecondros");
        if(patient_secondros.trim().length()!=0) {

            ((LinearLayout)findViewById(R.id.ros_poc_view)).setVisibility(View.VISIBLE);
            String[] pat = patient_secondros.replace("[", "").replace("]", "").split(",");
            for (int i = 0; i < pat.length; i++) {

                secondroslist.add(pat[i]);
            }
            secondrosadapter.notifyDataSetChanged();
        }

        patient_id.setText(getIntent().getStringExtra("patientid"));
        patient_name.setText(getIntent().getStringExtra("patientname"));
        patient_age.setText(getIntent().getStringExtra("age"));
        patient_gender.setText(getIntent().getStringExtra("gender"));
        patient_phoneno.setText(getIntent().getStringExtra("phonenumber"));
        patient_date.setText(getIntent().getStringExtra("pocdate"));
        doctor_name.setText(getIntent().getStringExtra("doctorname"));
        provisional.setText(getIntent().getStringExtra("provisional"));
        group_name.setText(getIntent().getStringExtra("group_name"));
        subgroup_name.setText(getIntent().getStringExtra("subgroup_name"));
        investigation_name.setText(getIntent().getStringExtra("investigation_name"));
        try {
            viewproblemarray = new JSONArray(getIntent().getStringExtra("jsonarray"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject prof = viewproblemarray.getJSONObject(0);


            chief_first = prof.getString("symtoms");
            chiefcomplaintfirst.setText(chief_first);
            ros_first = prof.getString("ros");
            rosfirst.setText(ros_first);
            array = prof.getString("selectedroslist").replace("[", "").replace("]", "").split(",");
            JSONArray array1 = new JSONArray(array);
            for (int i = 0; i < array1.length(); i++) {

                firstchiefroslist.add(array1.getString(i));
            }
           // instructionlist.add(prof.getString("instruction"));
            instructionpoc.setText(prof.getString("doctorNote"));


           */
/* if (viewproblemarray.length() > 1) {
                ((LinearLayout) findViewById(R.id.layout_second_poc)).setVisibility(View.VISIBLE);

                JSONObject prof1 = viewproblemarray.getJSONObject(1);
                chief_second = prof1.getString("symtoms");
                cheifcomplaintsecond.setText(chief_second);
                ros_second = prof1.getString("ros");
                rossecond.setText(ros_second);


                array = prof1.getString("selectedroslist").replace("[", "").replace("]", "").split(",");
                JSONArray array2 = new JSONArray(array);

                for (int i = 0; i < array2.length(); i++) {

                    secondchiefroslist.add(array2.getString(i));


                }
                instructionlist.add(prof1.getString("instruction"));



            }*//*


            firstroslistAdapter.notifyDataSetChanged();
            secondroslistAdapter.notifyDataSetChanged();
           // instructionlistAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View v) {


    }

    @Override
    public void getData(JSONArray response, int urlID) throws JSONException {

    }
}
*/
