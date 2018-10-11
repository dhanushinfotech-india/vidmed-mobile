package org.telemedicine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.CameraProfile;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telemedicine.Web.Data;
import org.telemedicine.Web.HttpAgent;
import org.telemedicine.Web.JsonResult;
import org.telemedicine.Web.UrlList;
import org.telemedicine.model.Patient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by suneel on 17/10/16.
 */

public class EditProfile extends SuperActivity implements JsonResult{
    RadioGroup gender_radiogrup;
    Bitmap imagebitmap;
    CheckBox termsandconditions;
    TextView agreeservice;
    String bp="0/0-0";
    String temp="0.0";
    EditText firstname_edt, lastname_edt, age_edt, phone_edt, address1_edt, address2_edt, email_edt, idnum_edt;
    //EditText district_edt,pin_edt;
    RadioButton male_radio, female_radio;
    Button save_btn, clear_btn;
    ImageView patient_logout, bimage;
    Spinner sp_city, sp_state;
    private static final String IMAGE_DIRECTORY_NAME = "Vidmed";
    String userGender = "";
    ArrayList<String> statesList = new ArrayList<String>();
    ArrayList<String> citesList = new ArrayList<String>();
    String saveidnum;
    byte[] savephoto;
    static String savefname;
    String savelname,savephone,saveage,saveemail,saveaddr1,saveaddr2,savecity,savestate;
    String oldphone ,oldimage;
    String state,town,serverpatientid;
    Boolean isImagechanged=false;
    int lastinserted = 0;
    SharedPreferences sharedPreferences;
    ArrayAdapter stateAdapter, cityAdapter;
    TextView tv_heading;

    JSONObject locationJson = null;
    final CharSequence[] items = {"Doc1", "Doc2", "Doc3", "Doc4", "Doc5"};
    private static int TAKE_PICTURE = 1100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri; // file url to store image/video


    @Override
    protected void onCreation(Bundle savedInstanceState) {
        setContentView(R.layout.patientregistration);

         /* Hiding keyboard  when loyout with scroll view */
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
tv_heading= (TextView) findViewById(R.id.headingr);
        firstname_edt = (EditText) findViewById(R.id.edt_firstName);
        lastname_edt = (EditText) findViewById(R.id.edt_lastName);
        age_edt = (EditText) findViewById(R.id.edt_age);
        phone_edt = (EditText) findViewById(R.id.edt_phone);
        agreeservice= (TextView) findViewById(R.id.agree_terms);
        agreeservice.setVisibility(View.GONE);
        CameraProfile cameraProfile = new CameraProfile();
        termsandconditions = (CheckBox) findViewById(R.id.check_agree);
        termsandconditions.setVisibility(View.GONE);
        email_edt = (EditText) findViewById(R.id.edt_email);
        patient_logout = (ImageView) findViewById(R.id.patient_reg_logout_image);

        patient_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        male_radio= (RadioButton) findViewById(R.id.radio_male);
        female_radio= (RadioButton) findViewById(R.id.radio_female);
        tv_heading.setText("Edit Profile");
        userGender=getIntent().getStringExtra("gender");
        if (getIntent().getStringExtra("gender").equalsIgnoreCase("Male"))
        {
            male_radio.setChecked(true);
        }
        else {
            female_radio.setChecked(true);
        }

        bimage = (ImageView) findViewById(R.id.Capture_photo);
        firstname_edt.setText(getIntent().getStringExtra("firstname"));
        lastname_edt.setText(getIntent().getStringExtra("lastname"));
        age_edt.setText(getIntent().getStringExtra("patientage"));
        oldphone=getIntent().getStringExtra("phone");
        phone_edt.setText(oldphone);
        email_edt.setText(getIntent().getStringExtra("email"));



        bimage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!firstname_edt.getText().toString().equalsIgnoreCase("")) {
                    savefname = firstname_edt.getText().toString().trim();
                    isImagechanged=true;
                    captureImage();
                } else {
                    Toast.makeText(getApplicationContext(), "please enter name", Toast.LENGTH_LONG).show();
                }

            }
        });
        address1_edt = (EditText) findViewById(R.id.edt_address1);
        address2_edt = (EditText) findViewById(R.id.edt_address2);
        sp_city = (Spinner) findViewById(R.id.sp_city);
        sp_state = (Spinner) findViewById(R.id.sp_state);


        gender_radiogrup = (RadioGroup) findViewById(R.id.radiogroup_group);

        address1_edt.setText(getIntent().getStringExtra("address1"));
        address2_edt.setText(getIntent().getStringExtra("address2"));
        if (getIntent().getStringExtra("image").length()!=0)
        {
            oldimage=getIntent().getStringExtra("image");
            bimage.setImageBitmap(StringToBitMap(oldimage=getIntent().getStringExtra("image")));
        } else {
            bimage.setImageResource(R.drawable.photo);
        }



        save_btn = (Button) findViewById(R.id.btn_Save);
        save_btn.setText("Update");
        save_btn.setOnClickListener(this);
        clear_btn = (Button) findViewById(R.id.btn_Clear);
        clear_btn.setText("Cancel");
        clear_btn.setOnClickListener(this);

        sharedPreferences = getSharedPreferences("MYPREFS", MODE_PRIVATE);
        lastinserted = sharedPreferences.getInt("LAST_INSERTED", 1);
        Cursor location = dbHelper.getProfile();
        statesList.clear();
        statesList.add("Select");
        while (location.moveToNext()) {

            try {

                String locations = location.getString(location.getColumnIndex("locations"));

                locationJson = new JSONObject(locations);
                Iterator<String> keys = locationJson.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    statesList.add(key);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Cursor c = dbHelper.getstatePatient(oldphone);

        c.moveToFirst();

        while (!c.isAfterLast()) {

            state = c.getString(c.getColumnIndex("state"));
            town = c.getString(c.getColumnIndex("town"));

            c.moveToNext();
        }

       Log.d("state","bbbbbb"+state+town);
        Log.d("town","bbbbbb"+town);

        stateAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, statesList);
        cityAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, citesList);

        sp_state.setAdapter(stateAdapter);
        sp_city.setAdapter(cityAdapter);
        sp_state.setSelection(stateAdapter.getPosition(state));
        sp_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String state = statesList.get(position);

                if (locationJson != null) {
                    try {
                        JSONArray citiesArray = locationJson.getJSONArray(state);
                        citesList.clear();
                        citesList.add("Select");
                        for (int i = 0; i < citiesArray.length(); i++) {

                            citesList.add(citiesArray.getString(i));
                        }

                        cityAdapter.notifyDataSetChanged();
                        sp_city.setSelection(cityAdapter.getPosition(town));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gender_radiogrup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_male) {
                    userGender = "male";
                } else if (checkedId == R.id.radio_female) {
                    userGender = "female";

                }
            }
        });

    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "");
        Log.d("storpath", "=========" + mediaStorageDir);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            // mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "IMG_" + timeStamp + ".png");
            mediaFile = new File(mediaStorageDir.getPath() + "IMG_" + savefname + ".png");
            Log.d("path", "===========" + String.valueOf(mediaFile));
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* // if the result is capturing Image
        if (resultCode == Constants.BP_RESULT_CODE) {
            Log.e("bpresult", data.getExtras().getString("bp"));
            bp=data.getExtras().getString("bp");
            temp=data.getExtras().getString("temp");
            getDoctorsList(bp,temp);

        }*/
        Log.d("test vitals","++"+bp+temp);

        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
        //        byte[] b = baos.toByteArray();
        //        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
    }

    // convert from byte array to bitmap
    public static Bitmap getPhoto(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);


    }
   /* private void getDoctorsList(final String bp, final String temp) {
        new AlertDialog.Builder(EditProfile.this).setMessage("Do you need a call with doctor?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(EditProfile.this, PatientInfoActivity.class).putExtra("patientId", phone_edt.getText().toString()).putExtra("bp", bp).putExtra("temp", temp).putExtra("isNewRegister", true));
                EditProfile.this.finish();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditProfile.this.finish();
            }
        }).show();
    }
*/

    private void previewCapturedImage() {
        try {
            // hide video preview
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            imagebitmap = bitmap;
            Log.d("pathimage", "===========" + fileUri.getPath());

            bimage.setImageBitmap(bitmap);
            savephoto = getBytes(bitmap);

            Log.d("pathbytes", "==========" + getBytes(bitmap));

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }




    private void getDataService(String id, String fname, String lname, String phone, String age, String email, String address1, String address2, String city, String state, String gender, byte[] photo, String phrma_id) throws JSONException {
        HttpAgent asyncTaskData = new HttpAgent(this);
        Data data = new Data();
        //   data.setBaseUrl(UrlList.save_url_patient + id + "&fname=" + fname + "&lname=" + lname + "&phone=" + phone + "&age=" + age + "&email=" + email + "&addr1=" + address1 + "&addr2=" + address2 + "&city=" + city + "&state=" + state + "&gender=" + gender + "&pharmacist_id=" + getUserId());
        data.setBaseUrl(UrlList.update_url_patient);
        data.setUrlId(UrlList.updateurl_patient_id);
        // data.setIsGET(true);
        data.setIsGET(false);
        JSONObject jsonData = new JSONObject();
        data.setParams(jsonData);
        Log.d("photo===========", "=============" + photo);
        jsonData.put("id", id);
        jsonData.put("fname", fname);
        jsonData.put("lname", lname);
        if (oldphone.equalsIgnoreCase(phone)){
            jsonData.put("phone", "");
        }else {
        jsonData.put("phone", phone);}
        jsonData.put("age", age);
        jsonData.put("email", email);
        jsonData.put("addr1", address1);
        jsonData.put("addr2", address2);
        jsonData.put("city", city);
        jsonData.put("state", state);
        jsonData.put("gender", gender);
        jsonData.put("oldphone",oldphone);

        Log.d("imagebitmap", "----===---" + imagebitmap);
        if (imagebitmap != null && isImagechanged) {
            Log.d("oldimage","==if=="+oldimage);
            jsonData.put("image", BitMapToString(imagebitmap));
        }
        else {
            //if (imagebitmap==null){ jsonData.put("image", "");}

//            else {
                jsonData.put("image", oldimage);
          //  }
        }
        Log.d("oldimage","==out="+oldimage);
        jsonData.put("pharmacist_id", phrma_id);
        Log.d("json data", "*********" + jsonData);
        asyncTaskData.executeWs(data);
    }

    @Override
    public void getData(JSONArray response, int urlID) throws JSONException {
        if (urlID == UrlList.updateurl_patient_id) {
            JSONObject object = response.getJSONObject(0);
            Log.e("json object", "{{{{json object}}}} " + object);
            //roleId = object.getString("RoleId");
            if (object.has("result")) {

                 if (object.getString("result").equalsIgnoreCase("success")) {
                     dbHelper.updatePatient(new Patient(savefname, savelname, saveage, savephone, saveemail, saveaddr1, saveaddr2, savecity, savestate, userGender, savephoto, saveidnum,"","",""),oldphone);
                     startActivity(new Intent(EditProfile.this, PatientInfoActivity.class).putExtra("patientId", savephone).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                 }


//                    new AlertDialog.Builder(EditProfile.this).setMessage("Do you want to Vital?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    startActivityForResult(new Intent(EditProfile.this, BlueToothInfoActivity.class),Constants.BP_RESULT_CODE);
//
//                                }
//                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                           // getDoctorsList("","");
//                        }
//                    }).show();
                 else {
                     String _response = object.getString("result");
                     Toast.makeText(EditProfile.this, _response.toUpperCase(), Toast.LENGTH_SHORT).show();
                     if (!_response.contains("failed"))
                         phone_edt.setError("Mobile Number Is Already Registered");
                     save_btn.setText("Retry");
                 }

            }
            }



    }


    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Save:
                savefname = firstname_edt.getText().toString().trim();
                savelname = lastname_edt.getText().toString().trim();
                savephone = phone_edt.getText().toString().trim();
                saveage = age_edt.getText().toString().trim();
                saveemail = email_edt.getText().toString().trim();
                saveidnum = phone_edt.getText().toString().trim();
                saveaddr1 = address1_edt.getText().toString().trim();
                saveaddr2 = address2_edt.getText().toString().trim();

                savestate = sp_state.getSelectedItem().toString().trim();
                Log.d("updating record","--========");

                if (firstname_edt.getText().toString().equalsIgnoreCase("") || lastname_edt.getText().toString().equalsIgnoreCase("") || phone_edt.getText().toString().equalsIgnoreCase("")
                        || age_edt.getText().toString().equalsIgnoreCase("")  || address1_edt.getText().toString().equalsIgnoreCase("")
                        || address2_edt.getText().toString().equalsIgnoreCase("") || sp_state.getSelectedItem().toString().equalsIgnoreCase("Select")
                        ) {

                    Toast.makeText(EditProfile.this, "Please fill the mandatory fields", Toast.LENGTH_SHORT).show();
                } else if (firstname_edt.getText().toString().length() < 3) {

                    Toast.makeText(EditProfile.this, "First Name Should be Atleast Three Letter", Toast.LENGTH_SHORT).show();

                } else if (lastname_edt.getText().toString().length() < 3) {

                    Toast.makeText(EditProfile.this, "Last Name Should be Atleast Three Letter", Toast.LENGTH_SHORT).show();

                } else if (savephone.length() != 10) {
                    Toast.makeText(EditProfile.this, "Enter Valid Phone", Toast.LENGTH_SHORT).show();
                } else if ((email_edt.getText().toString().length() > 0) && (!isValidEmaillId(email_edt.getText().toString()))) {

                    Toast.makeText(EditProfile.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();


                }
//                else if (!termsandconditions.isChecked()) {
//
//                    Toast.makeText(this, "Please agree the terms and services", Toast.LENGTH_SHORT).show();
//                }
                else
                    try {
                        Log.d("getdatamethod", "======");
                        savecity = sp_city.getSelectedItem().toString().trim();



                        getDataService(saveidnum, savefname, savelname, savephone, saveage, saveemail, saveaddr1, saveaddr2, savecity, savestate, getGenderCode(userGender), savephoto, getUserId());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                break;
            case R.id.btn_Clear:
                finish();
        }

    }

    private boolean isValidEmaillId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

}