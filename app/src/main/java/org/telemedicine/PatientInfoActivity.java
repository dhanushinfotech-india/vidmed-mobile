package org.telemedicine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import org.telemedicine.bluetooth.BlueToothInfoActivity;
import org.telemedicine.model.Doctor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Naveen.k on 8/12/2016.
 */
public class PatientInfoActivity extends SuperActivity implements JsonResult {

    String pharmacist, callStatus = "call is initiated", CALLER_URL;
    public static String callActionName = "org.telemedicine.call.status";
    public static ProgressDialog callProgressDialog;
    boolean isPeerUnavailalbe = false;
    private ImageView imageViewRound;
    LinearLayout linearLayout;
    String reg_date, valid_date;
    //ImageButton btn_sync;
    ImageView btn_sync;

    CircleImageView circleView;
    //ImageButton iv_call, edit_profile;
    ImageView iv_call, edit_profile;
    ImageView logout, patient_info_history, patient_bp_testing_img, payment;
    String pharmacistname, pharmacyid, patientAddrs2, patientAddrs1, patientnum, patientemail, historychief_first, historydruglist;
    String patientId, age, drugslist, chiefcompone, chiefcompsecond, patientsecondros, patientprovisional, doctorName,
            image, patientgroupname, patientsubgroupname, patientinvestigationname, doctorId;
    TextView emailid, phone, gender, tv_imgname, tv_doctorname, tv_problem, tv_date, tv_poc, call, vital_bp, vital_heartrate,
            vital_temperature, vital_weight, tv_diagnosis, tv_investigation, tv_chiefcomplaint, tv_roc,poc_type;
    JSONArray historyarray, problemarray, Previousarray;
    ArrayList historydates = new ArrayList();
    boolean isNewRegister = false;
    HashMap<String, Doctor> onlineUsers = new HashMap<String, Doctor>();
    String selectedDocName, selectedDocWSID, _bp, _temp;
    String[] selectedDoctorWSIDs;
    String userName;
    public static String userWSID;
    public static Thread ft;
    Bitmap icon;
    String bp;
    String heartrate;
    String temp;
    String bpOne = "";
    String heartrateone = "";
    String tempOne = "";
    JSONObject profile, pocJson;
    byte[] photo_patient;
    int emptyphoto;
    String profilefname;
    String weight;
    Dialog builderImage;
    ListView lv_druglist;
    DrugListAdapter drugListAdapter;
    ArrayList<DrugListView> druglistview;
    Cursor cursor;
    TextView drugCount;


    @Override
    public void onResumption() {
        loadingActivity();
    }

    public void loadingActivity() {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            patientId = bundle.getString("patientId");
            isNewRegister = bundle.getBoolean("isNewRegister");
            doctorName = getUserName();
            patientId = bundle.getString("patientId");
            doctorId = bundle.getString("doctorId");
            bp = bundle.getString("bp");
            temp = bundle.getString("temp");

//            Log.d("patientid", "====" + patientId);
        }
      /*  ArrayList <Patient> patientArrayList=new ArrayList<>();
        patientArrayList= dbHelper.getPatientDetails(Integer.parseInt(patientId));
        String valid=patientArrayList.get(0).getValid_date();


        //data=cursor.getString(cursor.getColumnIndex("valid"));
        Log.d("database", "+++" +valid);
*/
        if (isNewRegister) {
            getDoctorsList();
        }
        icon = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
        getDataservice(patientId);

        call = (TextView) findViewById(R.id.patient_call);
//        Log.d("patient druglist", "====" + drugslist);
        lv_druglist = (ListView) findViewById(R.id.drug_listview);
        lv_druglist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        druglistview = new ArrayList<>();
        drugListAdapter = new DrugListAdapter(this, druglistview);
        lv_druglist.setAdapter(drugListAdapter);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent _broswer = new Intent(Intent.ACTION_VIEW, Uri.parse("https://202.153.34.169:8010/join_call/pharma/doc1/9949433321/"));
                //      Intent _broswer = new Intent(Intent.ACTION_VIEW, Uri.parse("https://103.15.74.24:8000/join_call/pharma/doc1/9949433321/"));

                startActivity(_broswer);

            }
        });

        pharmacist = getUserId();
        //   tv_poc = (TextView) findViewById(R.id.latestpoc);
        circleView = (CircleImageView) findViewById(R.id.circleView);
        vital_bp = (TextView) findViewById(R.id.tv_bp);
        vital_heartrate = (TextView) findViewById(R.id.tv_heart_rate);
        vital_temperature = (TextView) findViewById(R.id.tv_temperature);
        vital_weight = (TextView) findViewById(R.id.tv_weight);
        tv_diagnosis = (TextView) findViewById(R.id.tv_tdiagnosis);
        tv_chiefcomplaint = (TextView) findViewById(R.id.tv_tchiefcomplaint);
        linearLayout= (LinearLayout) findViewById(R.id.druglistcount);
        poc_type= (TextView) findViewById(R.id.tv_poctype);
        linearLayout.setVisibility(View.GONE);
        //  tv_roc = (TextView) findViewById(R.id.tv_troc);
        tv_investigation = (TextView) findViewById(R.id.tv_tinvestigationt);
        drugCount = (TextView) findViewById(R.id.drugcount);


        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((StringToBitMap(image) == null)) {

                    Toast.makeText(getApplicationContext(), "No Profile Image", Toast.LENGTH_SHORT).show();

                }
                showImage();
            }
        });
        logout = (ImageView) findViewById(R.id.logout_image);
        payment = (ImageView) findViewById(R.id.pay);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientInfoActivity.this, Paytm_Payment_Activity.class));
            }
        });
        patient_info_history = (ImageView) findViewById(R.id.patient_view_history_img);

        patient_info_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertdilog = new AlertDialog.Builder(PatientInfoActivity.this);
                ArrayAdapter historyadapter = new ArrayAdapter(PatientInfoActivity.this, android.R.layout.simple_list_item_1, historydates);


                    alertdilog.setAdapter(historyadapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            drugCount.setText("");
                            druglistview.clear();
                            drugListAdapter.notifyDataSetChanged();
                            loadData(which);

                        }


                    });
                if (historydates.size()!=0) {
                    alertdilog.show();
                }
                else
                    Toast.makeText(getApplicationContext(),"No POC Are Available",Toast.LENGTH_SHORT).show();


               /* try {
                    if (profile.has("poc") && profile.getJSONArray("poc").length() != 0) {
                        Intent inte = new Intent(PatientInfoActivity.this, History_POC.class);
                        inte.putExtra("patientgender", gender.getText().toString());
                        inte.putExtra("patientage", age);
                        inte.putExtra("patientname", tv_imgname.getText().toString());
                        inte.putExtra("pharmacistname", pharmacistname);
                        Log.d("pharmacistname", "======" + pharmacistname);
                        Log.d("doctorname", "======" + doctorName + getUserName());

                        inte.putExtra("pharmacyid", pharmacyid);
                        inte.putExtra("patientaddress2", patientAddrs2);
                        inte.putExtra("patientaddress1", patientAddrs1);
                        inte.putExtra("patientnum", patientnum);
                        inte.putExtra("patientemail", patientemail);
                        inte.putExtra("doctorName", tv_doctorname.getText().toString());
                        inte.putExtra("date", tv_date.getText().toString());
                        inte.putExtra("patientid", patientId);
                        inte.putExtra("doctorid", doctorId);
                        inte.putExtra("pocarray", Previousarray.toString());
                        inte.putExtra("historydate", historydates);
                        startActivity(inte);
                    } else
                        Toast.makeText(PatientInfoActivity.this, "No Previous Visits", Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }*/


            }
        });

        patient_bp_testing_img = (ImageView) findViewById(R.id.patient_test_img);

        patient_bp_testing_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(PatientInfoActivity.this, BlueToothInfoActivity.class);
                startActivity(inten);
            }
        });


        //      img_next = (ImageView) findViewById(R.id.nextimg);
//        img_next.setOnClickListener(this);
        iv_call = (ImageView) findViewById(R.id.iv_call);
        edit_profile = (ImageView) findViewById(R.id.edit);

//        Log.d("============", "........" + getStrValueFromPref("role"));
        if (getStrValueFromPref("role").equalsIgnoreCase("patient")) {
            edit_profile.setVisibility(View.GONE);
            iv_call.setVisibility(View.GONE);
        }

/*
        if (getStrValueFromPref("role").equalsIgnoreCase("doctor")) {
            iv_call.setVisibility(View.GONE);
        }*/

        if (getStrValueFromPref("role").equalsIgnoreCase("doctor")) {
            iv_call.setVisibility(View.GONE);
        }


        Calendar c = Calendar.getInstance();
        final SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        final String correct = sdf1.format(c.getTime());
//        Log.d("correct", "------" + correct + valid_date);
        //  Log.d("vnsoft","____"+correct+PatientsAdapter.new_valid);

        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    registerBroadCast();
//                    Log.d("haaaaa", "++++" + valid_date);
                    if (sdf1.parse(correct).after(sdf1.parse(valid_date))) {

                        new AlertDialog.Builder(PatientInfoActivity.this).setMessage("This consultation expired\n" +
                                "Please confirm renewal to proceed with the video call?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        renewPatient();
                                        new AlertDialog.Builder(PatientInfoActivity.this).setMessage("Do you want to check BP, Pulse, TEMP?")
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        startActivityForResult(new Intent(PatientInfoActivity.this, BlueToothInfoActivity.class), Constants.BP_RESULT_CODE);

                                                    }
                                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                getDoctorsList();

                                            }
                                        }).show();


                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                                dialog.dismiss();
                            }
                        }).show();
                    } else
                        new AlertDialog.Builder(PatientInfoActivity.this).setMessage("Do you want to check BP, Pulse, TEMP?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivityForResult(new Intent(PatientInfoActivity.this, BlueToothInfoActivity.class), Constants.BP_RESULT_CODE);

                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getDoctorsList();
                            }
                        }).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientInfoActivity.this, EditProfile.class);
                intent.putExtra("firstname", profilefname);
                try {
                    intent.putExtra("lastname", profile.getString("lname"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("phone", phone.getText().toString());
                intent.putExtra("patientage", age);
                intent.putExtra("gender", gender.getText().toString());
                intent.putExtra("email", emailid.getText().toString());
                intent.putExtra("address1", patientAddrs1);
                intent.putExtra("address2", patientAddrs2);

                intent.putExtra("image", image);
                intent.putExtra("gender", gender.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void registerBroadCast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(callActionName);
        registerReceiver(callStatusBroadCast, filter);
    }

    private void renewPatient() {

        HttpAgent asyncTaskData = new HttpAgent(this);
        Data data = new Data();

        // data.setBaseUrl(String.format(UrlList.poc_url_patient,patientId));
        data.setBaseUrl(UrlList.renew_url_patient + patientId);
        data.setUrlId(UrlList.renewurl_patient_id);
        data.setIsGET(true);
        data.setParams(new JSONObject());
        asyncTaskData.executeWs(data);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.BP_RESULT_CODE) {
            Log.e("bpresult", data.getExtras().getString("bp"));
            bp = data.getExtras().getString("bp");
            temp = data.getExtras().getString("temp");
            bpOne = data.getExtras().getString("bp");
            heartrateone = data.getExtras().getString("heartrate");
            tempOne = data.getExtras().getString("temp");
//            Log.d("temperature1", "test" + temp);
            getDoctorsList();
        }

    }

    public void showImage() {
        builderImage = new Dialog(this);
        builderImage.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builderImage.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(StringToBitMap(image));
        builderImage.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        builderImage.show();
    }


    private void displayOnlineUsers() {
        final ArrayList<Doctor> doctors = new ArrayList<Doctor>(onlineUsers.values());
        final String[] doctorNames = new String[doctors.size()];
        for (int i = 0; i < doctors.size(); i++) {
            doctorNames[i] = doctors.get(i).getDocFullName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(PatientInfoActivity.this);
        builder.setTitle("Choose Doctor");
        builder.setItems(doctorNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Doctor selectedDoctor = doctors.get(which);
                selectedDocName = selectedDoctor.getDocUserId();
                selectedDocWSID = "";
                selectedDoctorWSIDs = selectedDoctor.getDocWSID().split(",");
//                Log.d("Failing vital3", "Test" + temp + bp);
                JSONObject jsonInfo = null;
                try {
                    jsonInfo = new JSONObject();
                    jsonInfo.put("callerWSID", userWSID).put("callerName", userName).put("callerFullName", getUserName());//caller == Pharmacist
                    jsonInfo.put("receiverWSID", selectedDocWSID).put("receiverName", selectedDocName);//Receiver== Doctor
//                    Log.d("Failing vital", "Test" + tempOne + bpOne);
                    bp = bpOne;
                    temp = tempOne;
                    heartrate = heartrateone;
//                    Log.d("Failing vital5", "Test" + temp + "-" + bp + "-" + heartrate);
                    if (bp == null) {
                        bp = "0/0-0";
                    }
                    if (heartrate == null) {
                        heartrate = "0";
                    }
                    if (temp == null) {
                        temp = "0.0";
                    }
                    jsonInfo.put("vital", bp);
                    jsonInfo.put("heartrate", heartrate);
                    jsonInfo.put("temperature", temp);

                    jsonInfo.put("status", "call").put("patientID", patientId);
                    jsonInfo.put("pharmaname", getStrValueFromPref("pharmaname"));


                    getNotifyService(jsonInfo, selectedDoctorWSIDs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                reserveRoom();
            }
        });
        builder.create().show();

    }

    public void callDoctor() {
        callProgressDialog = new ProgressDialog(this);
        callProgressDialog.setCancelable(false);
        callProgressDialog.setButton("End Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                disconnectCall(true);

            }
        });
        callProgressDialog.setMessage("Call to " + selectedDocName);
        callProgressDialog.show();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        ft = new Thread() {
            public void run() {
                try {
                    Thread.sleep(45000);
                    disconnectCall(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ft.start();
//
//        long start = System.currentTimeMillis();
//        long end = start + 45 * 1000; // 60 seconds * 1000 ms/sec
//        while (System.currentTimeMillis() > end) {
//            disconnectCall(false);
//        }
    }

    private void disconnectCall(boolean isEndCall) {
        if (callProgressDialog != null && callProgressDialog.isShowing())
            callProgressDialog.dismiss();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        if (isEndCall)
            try {
                disconnectService(selectedDoctorWSIDs);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    private void disconnectService(String[] callerWSIDs) throws JSONException {
        HttpAgent httpAgent = new HttpAgent(this);
        List<NameValuePair> _params = new ArrayList<NameValuePair>();
        _params.add(new BasicNameValuePair("info", new JSONObject().put("callerWSID", userWSID).put("callerName", userName).put("callerFullName", getUserName()).put("status", "disconnect").put("patientID", patientId).put("pharmaname", getStrValueFromPref("pharmaname")).toString()));
        JSONArray doctors = new JSONArray();
        for (String str : callerWSIDs) {
            doctors.put(str);
        }
        _params.add(new BasicNameValuePair("receivers", doctors.toString()));
        String encodedParams = URLEncodedUtils.format(_params, "utf-8");
        Data data = new Data();
        data.setBaseUrl(UrlList.DISCONNECTURL + encodedParams);
        data.setUrlId(UrlList.DISCONNECT_URL_ID);
        data.setSecured(true);
        data.setIsGET(true);
        data.setSSLSecured(true);
        data.setParams(new JSONObject());
        httpAgent.executeWs(data);
    }


    private void disconnectService(String selectedDocWSID) throws JSONException {
        HttpAgent httpAgent = new HttpAgent(this);
        List<NameValuePair> _params = new ArrayList<NameValuePair>();
        _params.add(new BasicNameValuePair("info", new JSONObject().put("callerName", userName).put("status", "disconnect").put("callerWSID", userWSID).put("patientID", patientId).toString()));
        String encodedParams = URLEncodedUtils.format(_params, "utf-8");
        Data data = new Data();
        data.setBaseUrl(String.format(UrlList.DISCONNECTURL, selectedDocWSID) + encodedParams);
        data.setUrlId(UrlList.DISCONNECT_URL_ID);
        data.setSecured(true);
        data.setIsGET(true);
        data.setSSLSecured(true);
        data.setParams(new JSONObject());
        httpAgent.executeWs(data);
    }

    private void reserveRoom() {
        HttpAgent asyncTaskData = new HttpAgent(this);
        Data data = new Data();
        data.setBaseUrl(UrlList.RESERVEROOMURL);
        data.setUrlId(UrlList.RESERVE_ROOM_URL_ID);
        data.setIsGET(true);
        data.setParams(new JSONObject());
        asyncTaskData.executeWs(data);
    }

    private void getDoctorsList() {
        HttpAgent asyncTaskData = new HttpAgent(this);
        Data data = new Data();
        data.setBaseUrl(UrlList.ONLINEUSERS);
        data.setUrlId(UrlList.online_users_id);
        data.setIsGET(true);
        data.setSSLSecured(true);//For SSL URLs
        data.setParams(new JSONObject());
        asyncTaskData.executeWs(data);
    }

    public void getDataservice(String patientId) {
        HttpAgent asyncTaskData = new HttpAgent(this);
        Data data = new Data();

        // data.setBaseUrl(String.format(UrlList.poc_url_patient,patientId));
        data.setBaseUrl(UrlList.poc_url_patient + patientId);
        data.setUrlId(UrlList.pocurl_patient_id);
        data.setIsGET(true);
        data.setParams(new JSONObject());
        asyncTaskData.executeWs(data);

    }
   /* private void refreshItems() {
        // Load items
        // ...
        Intent in=new Intent(PatientInfoActivity.this,HomeActivity.class);
        startActivity(in);
       *//* getDataservice(patientId);*//*
        // Load complete

    }*/


    @Override
    protected void onCreation(Bundle savedInstanceState) {
        setContentView(R.layout.profile_info_withpoc);

        btn_sync = (ImageView) findViewById(R.id.btn_sync);
        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNewRegister = false;


                loadingActivity();

            }
        });

    }

    @Override
    public void onClick(View v) {
        //   Intent intent = new Intent(PatientInfoActivity.this, POCViewActivity.class);
//        intent.putExtra("patientid", patientId);
//        intent.putExtra("patientname", tv_imgname.getText().toString());
//        intent.putExtra("age", age);
//        intent.putExtra("gender", gender.getText().toString());
//        intent.putExtra("phonenumber", phone.getText().toString());
//        intent.putExtra("pocdate", tv_date.getText().toString());
//        intent.putExtra("doctorname", tv_doctorname.getText().toString());
//        intent.putExtra("jsonarray", String.valueOf(problemarray));
//        intent.putExtra("drugsrecommanded", drugslist);
//        intent.putExtra("patsecondros", patientsecondros);
//        intent.putExtra("provisional", patientprovisional);
//        intent.putExtra("group_name", patientgroupname);
//        intent.putExtra("subgroup_name", patientsubgroupname);
//        intent.putExtra("investigation_name", patientinvestigationname);
//
//        // intent.putExtra("dates",historydate);
        // Log.d("dates1","+++"+historydate);
        //    Log.d("druglist", "view" + drugslist);


        //   startActivity(intent);

    }

    @Override
    public void getData(JSONArray response, int urlID) throws JSONException {
        JSONObject jsonData;
        switch (urlID) {
            case UrlList.online_users_id:
                userName = getUserId();
                ArrayList<Doctor> doctors = new ArrayList<Doctor>();
                JSONArray onlineUsersArray = new JSONArray(response.getJSONArray(0).toString());
                onlineUsers.clear();
                for (int i = 0; i < onlineUsersArray.length(); i++) {
                    JSONObject infoJson = onlineUsersArray.getJSONObject(i);
                    JSONObject detailsJson = infoJson.getJSONObject("details");
                    String role = getDataFromJsonStr(detailsJson.toString(), "role");
                    Doctor doctor = new Doctor(getDataFromJsonStr(detailsJson.toString(), "name"), getDataFromJsonStr(infoJson.toString(), "id"),
                            getDataFromJsonStr(detailsJson.toString(), "fullname"));
                 /*   if (doctor.isSamePharamacist(userName))
                        userWSID = doctor.getDocWSID();*/
                    userWSID = doctor.getDocWSID();
                    if (role.trim().length() != 0 && (role.equalsIgnoreCase("Doctor")||role.equalsIgnoreCase("Pediatrician"))) {
                        if (doctor.isValidDoctor(userName)) {
                            if (onlineUsers.containsKey(doctor.getDocUserId()))
                            { onlineUsers.put(doctor.getDocUserId(), doctor.appendWSID(onlineUsers.get(doctor.getDocUserId()).getDocWSID()));}
                            else
                            {  onlineUsers.put(doctor.getDocUserId(), doctor);}
                        }
                    }
                }
                if (userWSID == null || userWSID.trim().length() == 0) {
                    if (isNetworkAvailable()) {
                        LoginActivity.connectWS(PatientInfoActivity.this);
                        Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Check your internet", Toast.LENGTH_SHORT).show();
                    }
                } else
                    displayOnlineUsers();
                break;
            case UrlList.RESERVE_ROOM_URL_ID:
                JSONObject resultInfo = response.getJSONObject(0);
                JSONObject roomInfoJson = new JSONObject(resultInfo.toString());
                String meetingURL = roomInfoJson.getString("meeting_url");
                JSONObject jsonInfo = new JSONObject().put("status", "call").put("meetingURL", meetingURL).put("callerName", userName).put("callerWSID", userWSID).put("patientID", patientId).put("receiverWSID", selectedDocWSID);
//                getNotifyService(jsonInfo, selectedDocWSID);
                break;
            case UrlList.NOTIFY_STATUS_URL_ID:
                callDoctor();
                break;
            case UrlList.renewurl_patient_id:

                //  getDoctorsList();
                break;

            case UrlList.pocurl_patient_id:
                JSONObject patintinfo = response.getJSONObject(0);

                JSONArray pationarray = patintinfo.getJSONArray("patientsinformation");
                profile = pationarray.getJSONObject(0);

                Previousarray = profile.getJSONArray("poc");


                historydates.clear();

                for (int i = 0; i < Previousarray.length(); i++) {

                    JSONObject obj = new JSONObject(Previousarray.getString(i));

                    historydates.add("POC on  " + obj.getString("date"));


                }


//                Log.d("patientinfo", "responsde" + profile);
                //View view = findViewById(R.id.pationttable);
                // View patienttext = findViewById(R.id.patient_imgname);
                // View prevlist = findViewById(R.id.view_prev_visit);
                // prevlist.setOnClickListener(this);

                tv_imgname = (TextView) findViewById(R.id.imgname);
                emailid = (TextView) findViewById(R.id.emailp);
                phone = (TextView) findViewById(R.id.mobilep);

                gender = (TextView) findViewById(R.id.genderp);
                //  View v = findViewById(R.id.patient_prev_list);


                emailid.setText(profile.getString("email"));
                phone.setText(profile.getString("pnumber"));
                profilefname = (profile.getString("fname"));
                tv_imgname.setText(profile.getString("fname") + " " + profile.getString("lname"));
                gender.setText(getGender(profile.getString("gender")));
                age = profile.getString("age");
                image = profile.getString("image");
                pharmacyid = profile.getString("pharmacist_id");
                patientAddrs2 = profile.getString("addr2");
                patientAddrs1 = profile.getString("addr1");
                patientnum = profile.getString("pnumber");
                patientemail = profile.getString("email");


                reg_date = profile.getString("payment_date");
//                Log.d("five", "+++" + reg_date);
                if (reg_date != null && reg_date.length() != 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    Calendar c = Calendar.getInstance();

                    try {
                        c.setTime(sdf.parse(reg_date));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //Integer.parseInt(getStrValueFromPref("valid"))

                    c.add(Calendar.DATE, 10);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                    valid_date = sdf1.format(c.getTime());
//                    Log.d("dates_new", "++++" + valid_date);
                } else
                    valid_date = "";

                tv_doctorname = (TextView) findViewById(R.id.tv_tdoctorname);

                tv_date = (TextView) findViewById(R.id.tv_tdate);


//                Log.d("pathphotoblob_to_bmp1", "=======" + StringToBitMap(profile.getString("image")));
                circleView.setImageBitmap(StringToBitMap(image));

                if (!(StringToBitMap(image) == null)) {
                    circleView.setImageBitmap(StringToBitMap(image));
                } else {
                    circleView.setImageBitmap(icon);
                }

                if (profile.has("poc") && profile.getJSONArray("poc").length() != 0) {
                    JSONArray historyarray = profile.getJSONArray("poc");



            /*for (int j = 0; j < historyarray.length(); j++) {

                JSONObject hobje = new JSONObject(historyarray.getString(j));
                if (historydate.size()==0)
                    historydate.add("Select");

                historydate.add("POC on  "  +hobje.getString("date"));
            }*/
                    pocJson = new JSONObject(historyarray.getString(historyarray.length() - 1));//historyarray.length()-1

//                    Log.d("Hareesh", "Test" + pocJson);


                    tv_doctorname.setText(getDataFromJson(pocJson, "doctorName"));
                    pharmacistname = getDataFromJson(pocJson, "pharmacistname");
                    vital_bp.setText(getDataFromJson(pocJson, "bp"));
                    vital_heartrate.setText(getDataFromJson(pocJson, "pulse"));
//                    Log.d("value", "Test" + getDataFromJson(pocJson, "temperature"));
                    vital_temperature.setText(getDataFromJson(pocJson, "temperature"));
                    weight = getDataFromJson(pocJson, "weight");
//                    Log.d("Weight", "test" + getDataFromJson(pocJson, "weight"));
                    if (weight.equalsIgnoreCase("")) {

                        vital_weight.setText("");
                    } else
                        vital_weight.setText(getDataFromJson(pocJson, "weight"));
                    tv_date.setText(getDataFromJson(pocJson, "date"));
                    poc_type.setText(getDataFromJson(pocJson,"poc_type"));

                    drugslist = getDataFromJson(pocJson, "druglist");
                    Log.d("patientdruglist", "====" + getDataFromJson(pocJson, "druglist").trim().length());

                    // Toast.makeText(getApplicationContext(),drugslist.trim().length(),Toast.LENGTH_SHORT).show();
//                    Log.d("patientdruglist", "====" + drugslist);

                    if (drugslist.trim().length() != 0) {
//                        Log.d("patientdruglist", "====" + drugslist);
                        //
                        String[] drugsData = drugslist.split(",");
                        linearLayout.setVisibility(View.VISIBLE);
                        drugCount.setText(String.valueOf(drugsData.length));
                        for (int i = 0; i < drugsData.length; i++) {

                            String drugInfo = drugsData[i];
                            try {
                                drugInfo = (new JSONArray(drugsData[i])).get(0).toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String[] drugDetails = drugInfo.split("_");

                            if (drugDetails.length > 4)

                                druglistview.add(new DrugListView(drugDetails[0].replace("" + "[" + "", " ").replace("" + "\"" + "", " "), drugDetails[1], drugDetails[2], drugDetails[3], drugDetails[4], drugDetails[5].replace("" + "]" + "", " ").replace("" + "\"" + "", " ")));

                        }
//                        Log.d("patient druglist", "====" + drugslist);
                        drugListAdapter.notifyDataSetChanged();

                    }
                    else
                        drugCount.setText("0");

                    Log.e("test", "sample6" + drugslist);
                    patientsecondros = getDataFromJson(pocJson, "secondros");
                    Log.e("test", "sample644" + patientsecondros);
                    patientprovisional = getDataFromJson(pocJson, "provisional");
                    tv_diagnosis.setText(patientprovisional);
                    patientgroupname = getDataFromJson(pocJson, "groupname");
                    patientsubgroupname = getDataFromJson(pocJson, "subgroupname");
                    patientinvestigationname = getDataFromJson(pocJson, "investigationname");
                    tv_investigation.setText(patientinvestigationname);
                    problemarray = new JSONArray(pocJson.getString("problems"));
                    JSONObject prbobj = problemarray.getJSONObject(0);

                    chiefcompone = prbobj.getString("symtoms");
//                    tv_chiefcomplaint.setMovementMethod(new ScrollingMovementMethod());
                    tv_chiefcomplaint.setText(chiefcompone);
                    tv_roc.setText(patientsecondros);


                    if (problemarray.length() > 1) {
                        JSONObject prbobject = problemarray.getJSONObject(1);

                        chiefcompsecond = prbobject.getString("symtoms");

                    }
                    if (chiefcompsecond == null || chiefcompsecond.equalsIgnoreCase(null)) {
                        tv_problem.setText(chiefcompone);
                    } else
                        tv_problem.setText(chiefcompone + "," + chiefcompsecond);
//                    Log.d("chiefcomplaints", "111" + chiefcompone + "," + chiefcompsecond);


                } else {
                    // v.setVisibility(View.INVISIBLE);
                    tv_poc.setText("No POC Available");

                }
                break;
        }

    }


    public String getDataFromJson(JSONObject jsonObject, String key) {
        try {
            return jsonObject != null && jsonObject.has(key) ? jsonObject.getString(key) : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public void onBackPressed() {
        if (getStrValueFromPref("role").equalsIgnoreCase("patient"))
            this.finish();
        else {
            super.onBackPressed();
            startActivity(new Intent(PatientInfoActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (callStatusBroadCast != null) {
                unregisterReceiver(callStatusBroadCast);
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void sendStatus(Context context, String callStatus) {
        Intent intent = new Intent(callActionName);
        intent.putExtra("status", callStatus);
        context.sendBroadcast(intent);
    }

    private void loadData(int selectPoc) {

        try {
            //  hist_pocarray = new JSONArray(historypocarray);
            JSONObject obj = new JSONObject(Previousarray.getString(selectPoc));
            // count.setText(Previousarray.length());
            tv_date.setText(getDataFromJsonObj(obj, "date"));
            historydruglist = getDataFromJsonObj(obj, "druglist");
            Log.d("druglist", "====" + historydruglist);
            tv_diagnosis.setText(getDataFromJsonObj(obj, "provisional"));
            tv_doctorname.setText(getDataFromJson(obj, "doctorName"));
            vital_bp.setText(getDataFromJsonObj(obj, "bp"));
            vital_heartrate.setText(getDataFromJsonObj(obj, "pulse"));
//                    Log.d("value", "Test" + getDataFromJson(pocJson, "temperature"));
            vital_temperature.setText(getDataFromJsonObj(obj, "temperature"));
            weight = getDataFromJsonObj(obj, "weight");
            if (weight.equalsIgnoreCase("")) {

                vital_weight.setText("");
            } else
                vital_weight.setText(getDataFromJsonObj(obj, "weight"));
//                    Log.d("Weight", "test" + getDataFromJson(po
            // history_group.setText(getDataFromJsonObj(obj, "groupname"));
            //history_subgroup.setText(getDataFromJsonObj(obj, "subgroupname"));
            tv_investigation.setText(getDataFromJsonObj(obj, "investigationname"));
            poc_type.setText(getDataFromJson(obj,"poc_type"));
            // tv_chiefcomplaint.setText();
//            JSONObject prof2 = historyarray.getJSONObject(1);
//            historychief_second = prof2.getString("symtoms");
//            cheifcomplaint_second.setText(historychief_second);

            //historysecondros_list = getDataFromJsonObj(obj, "secondros");
            // String[] history_rossecond = historysecondros_list.replace("[", "").replace("]", "").split(",");
            // for (int i = 0; i < history_rossecond.length; i++) {

            //historysecondros.add(history_rossecond[i]);

            // }
            if (historydruglist.trim().length() != 0) {

                // ((LinearLayout) findViewById(R.id.history_druglist)).setVisibility(View.VISIBLE);
                //  count.setText(historydruglist.trim().length());
                String[] historydrugs = historydruglist.split(",");
                linearLayout.setVisibility(View.VISIBLE);
                drugCount.setText(String.valueOf(historydrugs.length));
                for (int i = 0; i < historydrugs.length; i++) {

                    String drugInfo = historydrugs[i];
                    try {
                        drugInfo = (new JSONArray(historydrugs[i])).get(0).toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String[] drugDetail = drugInfo.split("_");
                    if (drugDetail.length > 4)
                        druglistview.add(new DrugListView(drugDetail[0].replace("" + "[" + "", " ").replace("" + "\"" + "", " "), drugDetail[1], drugDetail[2], drugDetail[3], drugDetail[4], drugDetail[5].replace("" + "]" + "", " ").replace("" + "\"" + "", " ")));

                }


                historyarray = new JSONArray(getDataFromJsonObj(obj, "problems"));
                JSONObject prbob = historyarray.getJSONObject(0);

                historychief_first = prbob.getString("symtoms");
//                Log.d("cheifHUR", "test" + prbob.getString("symtoms"));
                tv_chiefcomplaint.setText(historychief_first);



              /*  for (int i = 0; i < historydrugs.length; i++) {
                    String drugInfo = historydrugs[i];
                    String[] drugDetail = drugInfo.split("_");
                    drugListViews.add(new DrugListView(drugDetail[0], drugDetail[1], drugDetail[2], drugDetail[3], drugDetail[4]));

                }*/
            }
            else
                drugCount.setText("0");
            // historyarray = new JSONArray(problemarray);
            //  historyros_first = prbobjs.getString("ros");
            // ros_first.setText(historyros_first);
            //   array = prbobjs.getString("selectedroslist").replace("[", "").replace("]", "").split(",");
            //JSONArray array1 = new JSONArray(array);
//
            //  for (int i = 0; i < array1.length(); i++) {

            // historyfirstchiefroslist.add(array1.getString(i));


            // }

            // historyinstructionlist.add(prbobjs.getString("instruction"));
            // instruction_poc.setText(prbobjs.getString("doctorNote"));
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

}