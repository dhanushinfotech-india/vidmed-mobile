package org.telemedicine;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.Adapter.PatientsAdapter;
import org.telemedicine.Web.Data;
import org.telemedicine.Web.HttpAgent;
import org.telemedicine.Web.JsonResult;
import org.telemedicine.Web.UrlList;
import org.telemedicine.model.Patient;
import org.telemedicine.model.Profile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class HomeActivity extends SuperActivity implements JsonResult, View.OnClickListener {


    private ArrayList<Patient> patientsList = new ArrayList<>();
    private ArrayList<Profile> profiledetails = new ArrayList<>();
    public static String valids;
    JSONObject patientJson;
    PatientsAdapter patientsAdapter;
    RecyclerView recycler_view;
    SwipeRefreshLayout swipeRefreshLayout;
    // SearchView searchView;
    EditText search;
    FloatingActionButton fab;
    String role, pharmaname;
    TextView name, tv_role;
    ImageView iv_search, iv_cancel, logout, person_detail;
    String profileId;
    Cursor cursor;
    TextView fname, email;
    String date, dt,renew;
    Date new_dates;


    @Override
    protected void onCreation(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);

         /* Hiding keyboard  when loyout with scroll view */
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        name = (TextView) findViewById(R.id.tv_pat_doc_name);
        name.setSelected(true);
        tv_role = (TextView) findViewById(R.id.tv_pat_doc_role);
        logout = (ImageView) findViewById(R.id.logout_image);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // logout();
                PopupMenu popupMenu = new PopupMenu(HomeActivity.this, logout);
                popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.actionlogout:
                                logout();
                                return true;
                            case R.id.actionreports:
                                startActivity(new Intent(HomeActivity.this,ConsultationListactivity.class));
                                return true;
                            default:
                                return true;
                        }
                    }
                });

                popupMenu.show();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        role = getStrValueFromPref("role");
        pharmaname = getStrValueFromPref("pharmaname");
        if (role != null && role.trim().length() != 0 && role.equalsIgnoreCase("doctor")) {
            fab.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.tv_heading)).setText("Previously visited patients");
        } else if (role.equalsIgnoreCase("pharmacist")) {
            name.setText(pharmaname);
            Log.d("Pharma", "Name" + name);
        }





      /* name.setText(getUserName());*/

      /*  findViewById(R.id.tv_pat_doc_nam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, POCActivity.class).putExtra("url", "http://www.google.com").putExtra("patientId", "7095124632").putExtra("doctorId", "dr256455"));
            }
        });*/

        patientsAdapter = new PatientsAdapter(HomeActivity.this,patientsList, new PatientsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Patient itemPatient) {
                //Toast.makeText(HomeActivity.this, "Name--" + itemPatient.getFirstName(), Toast.LENGTH_SHORT).show();

                startActivity((new Intent(HomeActivity.this, PatientInfoActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION).putExtra("value", itemPatient.getFirstName()).putExtra("patientId", itemPatient.getPatientServerId())));
                // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setAdapter(patientsAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });

        search = (EditText) findViewById(R.id.inputSearch);
//        search.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (event.getRawX() >= search.getRight() - search.getTotalPaddingRight()) {
//                        // your action for drawable click event
//                        Toast.makeText(HomeActivity.this, "Clear Search", Toast.LENGTH_SHORT).show();
//                        search.setText("");
//                        searchItems("");
//                        return true;
//                    }
//                }
//                return true;
//            }
//        });

        iv_search = (ImageView) findViewById(R.id.iv_search);
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("searched string", "=======" + search.getText().toString());
                if (search.getText().toString().length() == 10) {
                    searchItems(search.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "please enter complete phone number", Toast.LENGTH_LONG).show();
                }

            }
        });
        iv_cancel = (ImageView) findViewById(R.id.iv_cancel);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Clear Search", Toast.LENGTH_SHORT).show();
                search.setText("");
                searchItems("");
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(new Intent(HomeActivity.this, PatientRegisterationActivity.class));
            }
        });
        String roleletters = role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
        Log.d("checking", "&&&&&" + roleletters);
        tv_role.setText(roleletters);

        String nameletters = getUserName().substring(0, 1).toUpperCase() + getUserName().substring(1).toLowerCase();
        Log.d("checking", "=====" + nameletters);
        name.setText(pharmaname);


        person_detail = (ImageView) findViewById(R.id.person_details);

        if (role.equalsIgnoreCase("doctor")) {
            person_detail.setImageDrawable(getResources().getDrawable(R.drawable.doc_image));

        } else {
            person_detail.setImageDrawable(getResources().getDrawable(R.drawable.pharma_image));

        }

        person_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (role != null && role.trim().length() != 0 && role.equalsIgnoreCase("doctor")) {
                    tv_role.setText(role);
                    //                    name.setText(getUserName());
                    startActivity(new Intent(HomeActivity.this, Doctor_profile.class));
                } else if (role != null && role.trim().length() != 0 && role.equalsIgnoreCase("pharmacist")) {
                    name.setText(pharmaname);
                    startActivity(new Intent(HomeActivity.this, Pharma_Profile.class));
                } else if (role != null && role.trim().length() != 0 && role.equalsIgnoreCase("patient")) {
                    name.setText(getUserName());
                   /* startActivity(new Intent(HomeActivity.this,Patient_Profile.class));*/
                }
            }
        });



       /* name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (role != null && role.trim().length() != 0 && role.equalsIgnoreCase("doctor")) {
                    tv_role.setText(role);
//                    name.setText(getUserName());
                    startActivity(new Intent(HomeActivity.this, Doctor_profile.class));
                } else if (role != null && role.trim().length() != 0 && role.equalsIgnoreCase("pharmacist")) {
                    name.setText(getUserName());
                    startActivity(new Intent(HomeActivity.this, Pharma_Profile.class));
                } else if (role != null && role.trim().length() != 0 && role.equalsIgnoreCase("patient")) {
                    name.setText(getUserName());
                   *//* startActivity(new Intent(HomeActivity.this,Patient_Profile.class));*//*
                }
            }
        });*/
        refreshItems();

    }

    private void searchItems(String searchStr) {
        patientsList.clear();
        patientsList.addAll(dbHelper.searchPatientDetails(searchStr));
        Collections.reverse(patientsList);
        patientsAdapter.notifyDataSetChanged();
        if (patientsList.isEmpty()) {
            getDataFromDb(searchStr);
        }
    }

    private void refreshItems() {
        // Load items
        // ...
        getDataFromWS(getStrValueFromPref("userId"), getStrValueFromPref("role"));
        // Load complete

    }

//    private ArrayList<Patient> filter(ArrayList<Patient> patientsList, String newText) {
//
//        newText = newText.toLowerCase();
//
//        final List<Patient> filteredModelList = new ArrayList<>();
//        for (Patient model : patientsList) {
//            final String text = model.getPhoneNo();
//            if (text.contains(newText)) {
//                filteredModelList.add(model);
//            }
//        }
//        return (ArrayList<Patient>) filteredModelList;
//    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...
        Collections.reverse(patientsList);
        patientsAdapter.notifyDataSetChanged();
        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshItems();
    }

    @Override
    public void onClick(View v) {

    }

    private void getDataFromDb(String userId) {
        HttpAgent httpAgent = new HttpAgent(this);
        Data data = new Data();
        //data.setBaseUrl(UrlList.login_url + user+"&pwd="+pwd+"&role="+StoreValues.role);
        Log.d("searched string", "==getdata=====" + search.getText().toString());
        data.setBaseUrl(UrlList.poc_url_patient + userId);
        data.setUrlId(153);
        data.setIsGET(true);
        data.setParams(new JSONObject());
        httpAgent.executeWs(data);
    }


    private void getDataFromWS(String userId, String _role) {
        HttpAgent httpAgent = new HttpAgent(this);
        Data data = new Data();
        //data.setBaseUrl(UrlList.login_url + user+"&pwd="+pwd+"&role="+StoreValues.role);
        data.setBaseUrl(String.format(UrlList.GET_PATIENTS_URL, userId, _role.toUpperCase()));
        data.setUrlId(UrlList.GET_PATIENTS_URL_ID);
        data.setIsGET(true);
        data.setParams(new JSONObject());
        httpAgent.executeWs(data);
    }

    @Override
    public void getData(JSONArray response, int urlID) throws JSONException {
        if (urlID == UrlList.GET_PATIENTS_URL_ID) {
            JSONObject resultJson = response.getJSONObject(0);
            JSONArray patientsArray = resultJson.has("patients") ? resultJson.getJSONArray("patients") : new JSONArray();
            for (int i = 0; i < patientsArray.length(); i++) {
                patientJson = patientsArray.getJSONObject(i);
                Log.d("patientjson", "========" + getDataFromJsonObj(patientJson, "fname") + "===" + getDataFromJsonObj(patientJson, "last_updated"));
                Log.d("getpatientgender", "==========" + getGender(getDataFromJsonObj(patientJson, "gender")));

                renew = getDataFromJsonObj(patientJson, "payment_date");
                date=getDataFromJsonObj(patientJson,"last_updated");
                Log.d("renew","1111"+renew+date);

                dt = renew;

                if (dt!=null&&dt.length() != 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar c = Calendar.getInstance();
                    try {

                        c.setTime(sdf.parse(dt));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Integer.parseInt(getStrValueFromPref("valid"))

                    c.add(Calendar.DATE,10);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                    valids = sdf1.format(c.getTime());
                    Log.d("dates_new1", "++++" + valids);
                }
                else
                    valids="";

                /*Insertdatatask insertdatatask = new Insertdatatask();
                insertdatatask.execute();*/
                dbHelper.insertPatientDetails(new Patient(getDataFromJsonObj(patientJson, "fname"), getDataFromJsonObj(patientJson, "lname"), getDataFromJsonObj(patientJson, "age"), getDataFromJsonObj(patientJson, "pnumber"),
                        getDataFromJsonObj(patientJson, "email"), getDataFromJsonObj(patientJson, "addr1"), getDataFromJsonObj(patientJson, "addr2"),
                        getDataFromJsonObj(patientJson, "city"), getDataFromJsonObj(patientJson, "state"), getGender(getDataFromJsonObj(patientJson, "gender")), null, getDataFromJsonObj(patientJson, "pnumber"), getDataFromJsonObj(patientJson, "last_updated"), valids,renew).withPoc(getDataFromJsonObj(patientJson, "patient_poc")));

            }
            patientsList.clear();
            patientsList.addAll(dbHelper.getPatientDetails(0));
            onItemsLoadComplete();
            // Toast.makeText(HomeActivity.this, "Updated", Toast.LENGTH_SHORT).show();
        } else if (urlID == 153) {
            Log.d("searched response", "================" + response.toString());
            if (!response.toString().contains("failed")) {
                JSONObject patientJsondata = response.getJSONObject(0);
                JSONArray patientarray = patientJsondata.getJSONArray("patientsinformation");
                JSONObject patientJson = patientarray.getJSONObject(0);
                patientsList.clear();
                if (getDataFromJsonObj(patientJson, "pnumber").equalsIgnoreCase(search.getText().toString())) {
                    patientsList.add((new Patient(getDataFromJsonObj(patientJson, "fname"), getDataFromJsonObj(patientJson, "lname"), getDataFromJsonObj(patientJson, "age"), getDataFromJsonObj(patientJson, "pnumber"),
                            getDataFromJsonObj(patientJson, "email"), getDataFromJsonObj(patientJson, "addr1"), getDataFromJsonObj(patientJson, "addr2"),
                            getDataFromJsonObj(patientJson, "city"), getDataFromJsonObj(patientJson, "state"), getGender(getDataFromJsonObj(patientJson, "gender")), null, getDataFromJsonObj(patientJson, "pnumber"), getDataFromJsonObj(patientJson, "last_updated"), valids,renew).withPoc(getDataFromJsonObj(patientJson, "patient_poc"))));
                }

            } else {
                Toast.makeText(getApplicationContext(), "Patient not found", Toast.LENGTH_LONG).show();
            }
            onItemsLoadComplete();
        }

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }


   /* public class Insertdatatask extends AsyncTask {


        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (this.dialog == null) {
                dialog = new ProgressDialog(HomeActivity.this, R.style.ProgressBar);
                dialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent);
                dialog.setMessage("Please Wait....");
                dialog.setCancelable(false);
                dialog.show();
            } else {
                this.dialog.show();
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (dialog.isShowing()) {
                dialog.cancel();
            }
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            dbHelper.insertPatientDetails(new Patient(getDataFromJsonObj(patientJson, "fname"), getDataFromJsonObj(patientJson, "lname"), getDataFromJsonObj(patientJson, "age"), getDataFromJsonObj(patientJson, "pnumber"),
                    getDataFromJsonObj(patientJson, "email"), getDataFromJsonObj(patientJson, "addr1"), getDataFromJsonObj(patientJson, "addr2"),
                    getDataFromJsonObj(patientJson, "city"), getDataFromJsonObj(patientJson, "state"), getGender(getDataFromJsonObj(patientJson, "gender")), null, getDataFromJsonObj(patientJson, "pnumber"), getDataFromJsonObj(patientJson, "last_updated"), valids, renew).withPoc(getDataFromJsonObj(patientJson, "patient_poc")));

            return null;
        }
    }
*/

}