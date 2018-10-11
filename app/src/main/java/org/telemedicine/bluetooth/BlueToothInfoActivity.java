package org.telemedicine.bluetooth;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.telemedicine.Constants;
import org.telemedicine.R;
import org.telemedicine.SuperActivity;
import org.telemedicine.bluetooth.blood.BloodBuf;
import org.telemedicine.bluetooth.bp.BPBuf;
import org.telemedicine.bluetooth.bp.MtBuf;
import org.telemedicine.bluetooth.eet.EETBuf;
import org.telemedicine.bluetooth.fetal.FetalBuf;
import org.telemedicine.bluetooth.pulse.BluetoothService;
import org.telemedicine.bluetooth.pulse.CallBack;
import org.telemedicine.bluetooth.pulse.ICallBack;
import org.telemedicine.bluetooth.pulse.PulseBuf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import static org.telemedicine.Constants.DRUGS;
import static org.telemedicine.Constants.DRUGS_INFO_RESULT_CODE;
import static org.telemedicine.Constants.INSTRUCTS_INFO_RESULT_CODE;

/*import org.telemedicine.domain.form.FieldOverrides;
import org.ei.telemedicine.domain.form.FormSubmission;
import org.ei.telemedicine.sync.DrishtiSyncScheduler;
import org.ei.telemedicine.view.activity.NativeANCSmartRegisterActivity;
import org.ei.telemedicine.view.activity.NativeANMPlanofCareActivity;
import org.ei.telemedicine.view.activity.NativeChildSmartRegisterActivity;
import org.ei.telemedicine.view.activity.NativeHomeActivity;
import org.ei.telemedicine.view.activity.NativePNCSmartRegisterActivity;
import org.ei.telemedicine.view.activity.SecuredActivity;*/

public  class BlueToothInfoActivity extends SuperActivity implements OnClickListener,
        ICallBack, OnBluetoothResult, org.telemedicine.bluetooth.bp.ICallBack,
        org.telemedicine.bluetooth.eet.ICallBack, org.telemedicine.bluetooth.fetal.ICallBack, org.telemedicine.bluetooth.blood.ICallBack {
    public static boolean isBluetooth = false;
   Button bt_save,bt_cancel;
    String entityId, instanceId, formName, risks, pncRisks, childSigns, filePath = "";
    int subFormCount;
    boolean isDeviceSearched = false, isProgressRunning = false;
    ImageView iv_bp, iv_steh, iv_bgm, iv_eet, iv_fetal, iv_poc;
    EditText et_bp_sys, et_bp_dia, et_steh, et_bgm, et_bgm_mg, et_fetal, et_eet, et_bp_heart;
    TextView tv_eet_cen, tv_fetal;
    LinearLayout ll_fetal_data, ll_fetal, ll_eet, ll_bp, ll_bgm, ll_poc, ll_steh, ll_eet_child, ll_eet_child_data;
    FrameLayout frame_fetal;
    ProgressDialog playDialog;
    org.telemedicine.bluetooth.pulse.BluetoothService pulseService;
    org.telemedicine.bluetooth.pulse.CallBack pulsecall;

    org.telemedicine.bluetooth.bp.CallBack bpCall;
    org.telemedicine.bluetooth.bp.BluetoothService bpService;

    org.telemedicine.bluetooth.eet.BluetoothService eetService;
    org.telemedicine.bluetooth.eet.CallBack eetCall;

    org.telemedicine.bluetooth.fetal.BluetoothService fetalService;
    org.telemedicine.bluetooth.fetal.CallBack fetalCall;

    org.telemedicine.bluetooth.blood.BluetoothService bloodService;
    org.telemedicine.bluetooth.blood.CallBack bloodCall;

    ImageButton ib_record, ib_play;
    private String TAG = "BlueToothInfoActivity";
    public static String progrs = null;
    private static int device = 0;
    private static int tempartureFor = 0;
    private static int opacity = 128;
    BluetoothAdapter bluetoothAdapter;
    public static BluetoothSocket btSocket;
    ProgressDialog progressDialog, recordProgressDialog;
    public static String bpSystolic = "bpSystolic";
    public static String bpDiastolic = "bpDiastolic";
    public static String temperature = "temperature";
    public static String fetal_data = "fetalData";
    public static String blood_glucose_data = "bloodGlucoseData";
    public static String pstechoscope_data = "pstechoscopeData";
    public static String anm_poc = "anmPoc";
    static String anmPocInfo = "";
    private static int time = 10000;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;

    @Override
    protected void onStart() {
        super.onStart();

        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        if (pulseService == null) {
            pulsecall = new CallBack(new PulseBuf(), this);
            pulseService = new BluetoothService(BlueToothInfoActivity.this, pulsecall);
        }
        if (bpService == null) {
            bpCall = new org.telemedicine.bluetooth.bp.CallBack(new MtBuf(), this);
            bpService = new org.telemedicine.bluetooth.bp.BluetoothService(
                    BlueToothInfoActivity.this, bpCall);
        }
        if (eetService == null) {
            eetCall = new org.telemedicine.bluetooth.eet.CallBack(new EETBuf(), this);
            eetService = new org.telemedicine.bluetooth.eet.BluetoothService(
                    BlueToothInfoActivity.this, eetCall);
        }
        if (fetalService == null) {
            fetalCall = new org.telemedicine.bluetooth.fetal.CallBack(new FetalBuf(), this);
            fetalService = new org.telemedicine.bluetooth.fetal.BluetoothService(
                    BlueToothInfoActivity.this, fetalCall);
        }
        if (bloodService == null) {
            bloodCall = new org.telemedicine.bluetooth.blood.CallBack(new BloodBuf(), this);
            bloodService = new org.telemedicine.bluetooth.blood.BluetoothService(
                    BlueToothInfoActivity.this, bloodCall);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onCreation(Bundle saveInstanceState) {
        {
            setContentView(R.layout.bluetooth_info_layout);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            if (getActionBar() != null) {
                getActionBar().setTitle("Vital Information");
            }
            Bundle extras = getIntent().getExtras();
           /* if (extras != null && extras.containsKey(AllConstants.ENTITY_ID) && extras.containsKey(AllConstants.INSTANCE_ID_PARAM) && extras.containsKey(AllConstants.ANCVisitFields.RISKS)) {
                entityId = extras.getString(AllConstants.ENTITY_ID, "");
                instanceId = extras.getString(AllConstants.INSTANCE_ID_PARAM, "");
                formName = extras.getString(AllConstants.FORM_NAME_PARAM, "");
                subFormCount = extras.getInt(AllConstants.SUB_FORM_COUNT, 0);
                risks = extras.getString(AllConstants.ANCVisitFields.RISKS, "");
                pncRisks = extras.getString(AllConstants.PNCVisitFields.PNC_RISKS, "");
                childSigns = extras.getString(AllConstants.ChildIllnessFields.CHILD_SIGNS, "");*/

                iv_eet = (ImageView) findViewById(R.id.iv_eet);
                iv_bgm = (ImageView) findViewById(R.id.iv_bgm);
                iv_fetal = (ImageView) findViewById(R.id.iv_fetal);
                iv_bp = (ImageView) findViewById(R.id.iv_bp);
                iv_steh = (ImageView) findViewById(R.id.iv_steh);
                ib_play = (ImageButton) findViewById(R.id.ib_play);
                ib_record = (ImageButton) findViewById(R.id.ib_record);
                tv_fetal = (TextView) findViewById(R.id.tv_fetal);
                ll_fetal_data = (LinearLayout) findViewById(R.id.ll_fetal_data);
                iv_poc = (ImageView) findViewById(R.id.iv_poc);

                et_bp_heart = (EditText) findViewById(R.id.et_bp_heart);
                et_bgm = (EditText) findViewById(R.id.et_bgm_mmoi);
                et_bgm_mg = (EditText) findViewById(R.id.et_bgm_mg);
                et_bp_sys = (EditText) findViewById(R.id.et_bp_sys);
                et_bp_dia = (EditText) findViewById(R.id.et_bp_dia);
                et_fetal = (EditText) findViewById(R.id.et_fetal);
//            et_steh = (EditText) findViewById(R.id.et_steh);
                et_eet = (EditText) findViewById(R.id.et_eet);
                tv_eet_cen = (TextView) findViewById(R.id.tv_eet_cen);
                //tv_eet_cen.setText(context.allSettings().fetchANMConfiguration("temperature").startsWith("c") ? "C" : "F");
                bt_save = (Button) findViewById(R.id.bt_info_save);
            bt_cancel= (Button) findViewById(R.id.bt_info_cancel);

            frame_fetal = (FrameLayout) findViewById(R.id.frame_fetal);
                ll_fetal = (LinearLayout) findViewById(R.id.ll_fetal);
                ll_bgm = (LinearLayout) findViewById(R.id.ll_bgm);
                ll_bp = (LinearLayout) findViewById(R.id.ll_bp);
                ll_eet = (LinearLayout) findViewById(R.id.ll_eet);
                ll_steh = (LinearLayout) findViewById(R.id.ll_steh);
                ll_poc = (LinearLayout) findViewById(R.id.ll_poc);
                ll_eet_child = (LinearLayout) findViewById(R.id.ll_eet_child);
                ll_eet_child_data = (LinearLayout) findViewById(R.id.ll_eet_child_data);

             /*   et_bgm.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String text = et_bgm.getText().toString().toLowerCase(Locale.getDefault());
                        if (et_bgm_mg != null && et_bgm.getText().toString().trim().length() != 0)
                            et_bgm_mg.setText(conversionBGM(text));

                    }
                });*/
/*

                if (formName.equalsIgnoreCase(Constants.FormNames.PNC_VISIT)) {
                    ll_fetal.setVisibility(INVISIBLE);
                    ll_eet_child.setVisibility(VISIBLE);
                    if (subFormCount == 0) {
                        ll_eet_child.setVisibility(INVISIBLE);
                    }
                    for (int i = 1; i <= subFormCount; i++) {
                        final int value = i;
                        LinearLayout ll_eet_child_dat = new LinearLayout(this);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.CENTER;
                        ll_eet_child_dat.setWeightSum(4);
                        ll_eet_child_dat.setGravity(Gravity.CENTER);
                        ll_eet_child_dat.setLayoutParams(params);

                        TextView textView = new TextView(this);
                        textView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.5f));
                        textView.setText("Child-" + i + " Temp");
                        textView.setTextSize(20);

                        final EditText editText = new EditText(this);
                        editText.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.5f));
                        editText.setTag("Child temperature");
                        editText.setId(value);

                        TextView textView2 = new TextView(this);
                        textView2.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 0.5f));
                        textView2.setText((context.allSettings().fetchANMConfiguration("temperature").startsWith("c") ? "C" : "F"));
                        textView2.setTextSize(20);

                        ImageButton imageButton = new ImageButton(this);
                        imageButton.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 0.5F));
                        imageButton.setTag("ib_eet-" + value);
                        imageButton.setId(value);
                        imageButton.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tempartureFor = value;
                                device = org.telemedicine.bluetooth.Constants.EET_DEVICE_NUM;
                                iv_eet.setImageDrawable(getResources().getDrawable(R.drawable.eet_enable));
                                startDiscovery();
                            }
                        });

                        ll_eet_child_dat.addView(textView);
                        ll_eet_child_dat.addView(editText);
                        ll_eet_child_dat.addView(textView2);
                        ll_eet_child_dat.addView(imageButton);

                        ll_eet_child.addView(ll_eet_child_dat);
                    }
                } else if (formName.equalsIgnoreCase(AllConstants.FormNames.CHILD_ILLNESS) || formName.equalsIgnoreCase(AllConstants.FormNames.CHILD_ILLNESS_EDIT)) {
//                    (FrameLayout) findViewById(R.id.frame_fetal).setVisibility(GONE);
                    frame_fetal.setVisibility(GONE);
                    ll_fetal.setVisibility(GONE);
                    ll_bgm.setVisibility(GONE);
                    ll_bp.setVisibility(GONE);
                    ll_steh.setVisibility(GONE);
                    ll_poc.setVisibility(VISIBLE);
                } else if (formName.equalsIgnoreCase(AllConstants.FormNames.ANC_INVESTIGATIONS)) {
                    ll_poc.setVisibility(GONE);
                }
*/

                iv_eet.setOnClickListener(this);
//                iv_fetal.setOnClickListener(this);
//                iv_steh.setOnClickListener(this);
    //            iv_bgm.setOnClickListener(this);
                iv_bp.setOnClickListener(this);
                bt_save.setOnClickListener(this);
                bt_cancel.setOnClickListener(this);
//                ib_play.setOnClickListener(this);
     //           ib_record.setOnClickListener(this);
         //       iv_poc.setOnClickListener(this);
//                File file = new File(getFilePath());
//                if (file.exists()) {
//                    ib_play.setVisibility(VISIBLE);
//                }

                progressDialog = new ProgressDialog(BlueToothInfoActivity.this);
                progressDialog.setTitle("Device Connecting");
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (bluetoothAdapter != null) {
                    IntentFilter intent = new IntentFilter();
                    intent.addAction(BluetoothDevice.ACTION_FOUND);
//                    intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                    intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
                    intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
                    intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
                    registerReceiver(searchDevices, intent);
                }
            }

        }
    //}



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Cming", "BLuetooth Acti-----------------" + resultCode);
        if (resultCode == DRUGS_INFO_RESULT_CODE) {
            Log.e("drugs", data.getExtras().getString(DRUGS));
            anmPocInfo = data.getExtras().getString(DRUGS);
        } else if (resultCode == INSTRUCTS_INFO_RESULT_CODE) {
            switch (data.getExtras().getString("device")) {
//                case "bgm":
//                    device = org.telemedicine.bluetooth.Constants.BLOOD_DEVICE_NUM;
//                    iv_bgm.setImageDrawable(getResources().getDrawable(R.drawable.bgm_enable));
//                    startDiscovery();
//                    break;
//                case "bp":
//                    device = org.telemedicine.bluetooth.Constants.BP_DEVICE_NUM;
//                    iv_bp.setImageDrawable(getResources().getDrawable(R.drawable.bp_enable));
//                    startDiscovery();
//                    break;
               /* case "steh":
                    device = 0;
                    iv_steh.setImageDrawable(getResources().getDrawable(R.drawable.steh_enable));
                    startRecord(getFilePath());
                    break;*/
//                case "fetal":
//                    device = org.telemedicine.bluetooth.Constants.FET_DEVICE_NUM;
//                    iv_fetal.setImageDrawable(getResources().getDrawable(R.drawable.fetal_enable));
//                    startDiscovery();
//                    break;
//                case "eet":
//                    tempartureFor = 0;
//                    device = org.telemedicine.bluetooth.Constants.EET_DEVICE_NUM;
//                    iv_eet.setImageDrawable(getResources().getDrawable(R.drawable.eet_enable));
//                    startDiscovery();
//                    break;
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (bluetoothAdapter != null)
            bluetoothAdapter.cancelDiscovery();
        try {
            if (searchDevices != null)
                unregisterReceiver(searchDevices);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("Form Back press", formName + "----" + BlueToothInfoActivity.isBluetooth);
       /* if (formName.equalsIgnoreCase(AllConstants.FormNames.ANC_VISIT) || formName.equalsIgnoreCase(AllConstants.FormNames.ANC_VISIT_EDIT)) {
//            startActivity(new Intent(this, DummyActivity.class).putExtra("entityId", entityId).putExtra("screen", "bluet"));
            startFormActivity(AllConstants.FormNames.ANC_VISIT_EDIT, entityId, new FieldOverrides(context.anmLocationController().getFormInfoJSON()).getJSONString());
//            startFormActivity(AllConstants.FormNames.ANC_VISIT_EDIT, entityId, new FieldOverrides(context.anmLocationController().getFormInfoJSON()).getJSONString());
        } else if (formName.equalsIgnoreCase(AllConstants.FormNames.PNC_VISIT) || formName.equalsIgnoreCase(AllConstants.FormNames.PNC_VISIT_EDIT)) {
//            startActivity(new Intent(this, DummyActivity.class).putExtra("entityId", entityId).putExtra("screen", "bluet"));
            startFormActivity(AllConstants.FormNames.PNC_VISIT_EDIT, entityId, new FieldOverrides(context.anmLocationController().getFormInfoJSON()).getJSONString());
//            startFormActivity(AllConstants.FormNames.ANC_VISIT_EDIT, entityId, new FieldOverrides(context.anmLocationController().getFormInfoJSON()).getJSONString());
        } else if (formName.equalsIgnoreCase(AllConstants.FormNames.CHILD_ILLNESS) || formName.equalsIgnoreCase(AllConstants.FormNames.CHILD_ILLNESS_EDIT)) {
//            startActivity(new Intent(this, DummyActivity.class).putExtra("entityId", entityId).putExtra("screen", "bluet"));
            startFormActivity(AllConstants.FormNames.CHILD_ILLNESS_EDIT, entityId, new FieldOverrides(context.anmLocationController().getFormInfoJSON()).getJSONString());
//            startFormActivity(AllConstants.FormNames.ANC_VISIT_EDIT, entityId, new FieldOverrides(context.anmLocationController().getFormInfoJSON()).getJSONString());
        }*/
//        BlueToothInfoActivity.this.finish();
//        super.onBackPressed();
    }

    public BroadcastReceiver searchDevices = new BroadcastReceiver() {
        ArrayList<String> bluetoothDevicesList = new ArrayList<String>();

        public void onReceive(Context context, Intent intent) {
            progressDialog.setTitle("Search is initiated");
            String action = intent.getAction();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] lstName = bundle.keySet().toArray();
                for (int i = 0; i < lstName.length; i++) {
                    String keyName = lstName[i].toString();
                    Log.e(TAG, keyName + "-----" + String.valueOf(bundle.get(keyName)));
                }
            }
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                bluetoothDevicesList.add(device.getName() + "\n" + device.getAddress());
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
//                    Log.i("face", device.getName() + "\n" + device.getAddress());
//                }

                if (device.getName() != null
                        && device.getName().contains(org.telemedicine.bluetooth.Constants.PULSE_DEVICE) && BlueToothInfoActivity.device == org.telemedicine.bluetooth.Constants.PULSE_DEVICE_NUM) {

                    if (pulseService != null)
                        pulseService.stop();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pulseService.start();
                    pulseService.connect(device, context,
                            org.telemedicine.bluetooth.Constants.PULSE_DEVICE_NUM);
                } else if (device.getName() != null
                        && device.getName().startsWith(org.telemedicine.bluetooth.Constants.BP_DEVICE) && BlueToothInfoActivity.device == org.telemedicine.bluetooth.Constants.BP_DEVICE_NUM) {
                    Log.e(TAG, "Connect" + "BP Device");
                    progressDialog.setTitle("BP is connected");
                    if (bpService != null)
                        bpService.stop();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    bpService.start();
                    bpService.connect(device, BlueToothInfoActivity.this, org.telemedicine.bluetooth.Constants.BP_DEVICE_NUM);
                } else if (device.getName() != null
                        && device.getName().startsWith(org.telemedicine.bluetooth.Constants.EET_DEVICE) && BlueToothInfoActivity.device == org.telemedicine.bluetooth.Constants.EET_DEVICE_NUM) {
                    Log.e(TAG, "Connect" + "EET Device");

                    if (eetService != null)
                        eetService.stop();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    eetService.start();
                    eetService.connect(device, BlueToothInfoActivity.this,
                            org.telemedicine.bluetooth.Constants.EET_DEVICE_NUM);
                } else if (device.getName() != null
                        && device.getName().startsWith(org.telemedicine.bluetooth.Constants.FET_DEVICE) && BlueToothInfoActivity.device == org.telemedicine.bluetooth.Constants.FET_DEVICE_NUM) {
                    Log.e(TAG, "Connect" + "FET Device");

                    if (fetalService != null)
                        fetalService.stop();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    fetalService.start();
                    fetalService.connect(device, BlueToothInfoActivity.this, org.telemedicine.bluetooth.Constants.FET_DEVICE_NUM);
                } else if (device.getName() != null
                        && device.getName().startsWith(org.telemedicine.bluetooth.Constants.BLOOD_DEVICE)
                        && BlueToothInfoActivity.device == org.telemedicine.bluetooth.Constants.BLOOD_DEVICE_NUM) {
                    Log.e(TAG, "Connect" + "Blood Device");
                    progressDialog.setTitle("Blood Device is connected");
                    progressDialog.setTitle("Blood Device is connected");
                    if (bloodService != null)
                        bloodService.stop();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    bloodService.start();
                    bloodService.connect(device, BlueToothInfoActivity.this, org.telemedicine.bluetooth.Constants.BLOOD_DEVICE_NUM);
                }
            }
//            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equalsIgnoreCase(action)) {
//                Log.e("Finished discovery", bluetoothDevicesList.size() + "-----");
//                if (bluetoothDevicesList.size() == 0) {
//                    if (progressDialog != null)
//                        progressDialog.dismiss();
//                    Toast.makeText(BlueToothInfoActivity.this, "No Device", Toast.LENGTH_SHORT).show();
//                }
//                for (String device : bluetoothDevicesList) {
//                    if (device.startsWith(Constants.BLOOD_DEVICE) || device.startsWith(Constants.FET_DEVICE) || device.startsWith(Constants.BP_DEVICE) || device.startsWith(Constants.EET_DEVICE)) {
//                    } else {
//                        if (progressDialog != null)
//                            progressDialog.dismiss();
//                        Toast.makeText(BlueToothInfoActivity.this, "No Device", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            } else {
////                Toast.makeText(BlueToothInfoActivity.this, "No Device", Toast.LENGTH_SHORT).show();
//                if (progressDialog != null)
//                    progressDialog.dismiss();
//                if (bluetoothAdapter != null)
//                    bluetoothAdapter.cancelDiscovery();
//            }
        }
    };

    private void startReceivingData(final String deviceName, Object serviceObj, final BluetoothDevice device) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    switch (deviceName) {
                        case "bgm":
                            bloodService.start();
                            bloodService.connect(device, BlueToothInfoActivity.this,
                                    org.telemedicine.bluetooth.Constants.BLOOD_DEVICE_NUM);
                            break;
                    }
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            /*@Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                new AlertDialog.Builder(BlueToothInfoActivity.this).setCancelable(false).setMessage("Stop.").setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopRecording();
                    }
                }).show();
            }*/
        }.execute();

    }

    public void startDiscovery() {
        progressDialog.show();
        isProgressRunning = true;
        Log.e("Discovrty", "Startd");
//        progressDialog.setCancelable(false);
        bluetoothAdapter.startDiscovery();
        Thread checkDiscoveryStatus = new Thread() {
            public void run() {
                try {
                    Thread.sleep(30000);
                    checkForDevice();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        checkDiscoveryStatus.start();

    }

    private void checkForDevice() {
        if (isProgressRunning) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                if (bluetoothAdapter != null)
                    bluetoothAdapter.cancelDiscovery();
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BlueToothInfoActivity.this, "Unable to connect. Please retry", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_bp:
                device = org.telemedicine.bluetooth.Constants.BP_DEVICE_NUM;
                iv_bp.setImageDrawable(getResources().getDrawable(R.drawable.bp_enable));
                startDiscovery();
                break;
            case R.id.iv_bgm:
                device = org.telemedicine.bluetooth.Constants.BLOOD_DEVICE_NUM;
                iv_bgm.setImageDrawable(getResources().getDrawable(R.drawable.bgm_enable));
                startDiscovery();
                break;
            case R.id.iv_steh:
                startActivityForResult(new Intent(this, BluetoothInstructionsActivity.class).putExtra("device", "steh"), Constants.INSTRUCTS_INFO_RESULT_CODE);

                break;
            case R.id.iv_eet:
                tempartureFor = 0;
                device = org.telemedicine.bluetooth.Constants.EET_DEVICE_NUM;
                iv_eet.setImageDrawable(getResources().getDrawable(R.drawable.eet_enable));
                startDiscovery();

                break;
            case R.id.iv_fetal:
                startActivityForResult(new Intent(this, BluetoothInstructionsActivity.class).putExtra("device", "fetal"), Constants.INSTRUCTS_INFO_RESULT_CODE);

                break;
          /*  case R.id.iv_poc:
                risks = risks.equals("") ? pncRisks : risks;
                risks = risks.equals("") ? childSigns : risks;
                if (risks.trim().length() != 0) {
                    Intent intent = new Intent(this, NativeANMPlanofCareActivity.class);
                    intent.putExtra(AllConstants.ANCVisitFields.RISKS, risks.trim().replace(" ", ",").replace("_", " "));
                    startActivityForResult(intent, DRUGS_INFO_RESULT_CODE);
                } else
                    Toast.makeText(this, "No Symptoms observed.", Toast.LENGTH_SHORT).show();
                break;*/
            case R.id.bt_info_save:
                if (et_bp_dia.getText().toString().equals("") && et_bp_sys.getText().toString().equals("") &&tv_eet_cen.getText().toString().equalsIgnoreCase("")) {
                    new AlertDialog.Builder(this).setTitle("Do you want save with out vital reading?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveData();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                } else {
                    saveData();
                }

                break;

            case R.id.bt_info_cancel:
                onDestroy();
                finish();

          /*  case R.id.ib_play:
                startPlaying(this.filePath);
                break;*/
            default:

                break;
        }
    }

    private void saveData() {
        setResult(Constants.BP_RESULT_CODE, new Intent().putExtra("bp",et_bp_sys.getText().toString()+"/"+et_bp_dia.getText().toString()).putExtra("heartrate",et_bp_heart.getText().toString())
                .putExtra("temp",et_eet.getText().toString()));
        Log.d("temperature","test"+et_eet.getText().toString());
        finish();
//        try {
//            if (searchDevices != null)
//                unregisterReceiver(searchDevices);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        /*try {
            saveDevicesData(entityId);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("Exception", "UnSupported Encoding Exception");
        }*/
    }

    /*public void turnSpeaker() {
        AudioManager audioManager = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.STREAM_MUSIC);
        audioManager.setSpeakerphoneOn(true);
    }

    private void startPlaying(String filePath) {
//        try {
//            turnSpeaker();
//        } catch (Exception e) {
//            Toast.makeText(BlueToothInfoActivity.this, "Unable to play, check audio settings", Toast.LENGTH_SHORT).show();
//        }
        playDialog = new ProgressDialog(BlueToothInfoActivity.this);
        playDialog.setMessage("Record is Playing");
        playDialog.setCancelable(false);
        playDialog.setButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer.release();
                }
                dialog.dismiss();
            }
        });

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(filePath);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.prepare();

            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    playDialog.show();
                    mPlayer.start();
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (playDialog != null && playDialog.isShowing())
                        playDialog.dismiss();
                    mp.stop();
                    mp.release();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startRecord(String filePath) {
        this.filePath = filePath;
        File file = new File(filePath);
        if (!file.exists()) {
//            showListDialog(filePath);
            new AsyncTaskRunner().execute(filePath);
        }
    }*/
/*
    public String getFilePath() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Constants.DRISTHI_DIRECTORY_NAME;
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }
        path += "/" + System.currentTimeMillis() + ".wav";
        Log.e("Audio path", path);
        return path;
    }*/

/*
    private class AsyncTaskRunner extends AsyncTask<String, Long, String> {

        private String resp;

        @Override
        protected void onPreExecute() {
//            progressDialog.setTitle("Recording Audio");
//            progressDialog.show();
            recordProgressDialog = new ProgressDialog(BlueToothInfoActivity.this);
            recordProgressDialog.setTitle("Recording Audio");
            recordProgressDialog.show();
//
        }*/

/*
        @Override
        protected String doInBackground(String... params) {
            try {
                // Do your long operations here and return the result
                // Sleeping for given time period
//                startRecording(params[0], Integer.parseInt(params[1]));
                startRecording(params[0]);

                Thread.sleep(time);
                resp = "Slept for " + time + " milliseconds";
            } catch (InterruptedException e) {
                e.printStackTrace();
                resp = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            recordProgressDialog.dismiss();
            new AlertDialog.Builder(BlueToothInfoActivity.this).setCancelable(false).setMessage("Record Successful.").setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stopRecording();
                }
            }).show();
//            Toast.makeText(BlueToothInfoActivity.this, "Record Successful", Toast.LENGTH_SHORT).show();
        }
    }


    private void startRecording(String filePath) {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//        mRecorder.setAudioSource(i);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(filePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("Recording", "prepare() failed");
        }
        mRecorder.start();
    }

    private void stopRecording() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            } catch (Exception e) {
                Log.e("Media", "is already released");
            }
        }
        if (ib_play != null)
            ib_play.setVisibility(VISIBLE);
    }


    private void saveDevicesData(String entityId) throws JSONException, UnsupportedEncodingException {
//        Toast.makeText(BlueToothInfoActivity.this, "cleic", Toast.LENGTH_SHORT).show();
        org.ei.telemedicine.Context context = org.ei.telemedicine.Context.getInstance();
        Log.e(TAG, "Entity ID----" + entityId);
        Log.e(TAG, "BP Dia----" + et_bp_dia.getText().toString() + "/////Bp Sys----" + et_bp_sys.getText().toString() + "--Temp----" + et_eet.getText().toString() + "---Fetal---" + et_fetal.getText().toString() + "---BGM---" + et_bgm.getText().toString());
        if (entityId != null) {
            FormSubmission formSubmission = context.formDataRepository().fetchFromSubmissionUseEntity(entityId);
            Log.e(TAG, "Instance Id " + formSubmission.instanceId());
            Log.e(TAG, "Form Name " + formSubmission.formName());
            JSONObject formData = new JSONObject(formSubmission.instance());
            JSONObject instanceData = formData.getJSONObject("form");
            JSONArray fieldsJsonArray = instanceData.getJSONArray("fields");
            JSONArray instancesArray = instanceData.has("sub_forms") ? instanceData.getJSONArray("sub_forms").getJSONObject(0).getJSONArray("instances") : null;
            if (instancesArray != null && subFormCount == 0) {
                for (int i = 1; i <= subFormCount; i++) {
                    int value = i;
                    EditText editText = (EditText) findViewById(value);
                    instancesArray.getJSONObject(i - 1).put("childTemperature", editText.getText().toString());
                }
                instanceData.getJSONArray("sub_forms").getJSONObject(0).put("instances", instancesArray);
            }
            for (int i = 0; i < fieldsJsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject(fieldsJsonArray.get(i).toString());

                if (jsonObject.has("name")) {
                    if (jsonObject.get("name").equals(bpDiastolic))
                        jsonObject.put("value", et_bp_dia.getText().toString());
                    else if (jsonObject.get("name").equals(bpSystolic))
                        jsonObject.put("value", et_bp_sys.getText().toString());
                    else if (jsonObject.get("name").equals(AllConstants.ANCVisitFields.PULSERATE))
                        jsonObject.put("value", et_bp_heart.getText().toString());
                    else if (jsonObject.get("name").equals(temperature))
                        jsonObject.put("value", et_eet.getText().toString() + (context.allSettings().fetchANMConfiguration("temperature").startsWith("c") ? "-C" : "-F"));
                    else if (jsonObject.get("name").equals(AllConstants.PNCVisitFields.CHILD_TEMPERATURE))
                        jsonObject.put("value", et_eet.getText().toString() + (context.allSettings().fetchANMConfiguration("temperature").startsWith("c") ? "-C" : "-F"));
                    else if (jsonObject.get("name").equals(fetal_data))
                        jsonObject.put("value", et_fetal.getText().toString());
                    else if (jsonObject.get("name").equals(blood_glucose_data))
                        jsonObject.put("value", et_bgm.getText().toString());
                    else if (jsonObject.get("name").equals(pstechoscope_data))
                        jsonObject.put("value", this.filePath);
                    else if (jsonObject.get("name").equals(anm_poc))
                        jsonObject.put("value", anmPocInfo);
                    fieldsJsonArray.put(i, jsonObject);
                }
            }
            Log.e(TAG, "After Putting values ---- " + fieldsJsonArray.toString());
            instanceData.put("fields", fieldsJsonArray);
            formData.put("form", instanceData);
            context.formDataRepository().updateInstance(instanceId, formData.toString());
        }
        Log.e(TAG, "Over");
//        File file = new File(getFilePath());
//        if (file.exists()) {
//            if (file.delete())
//                Toast.makeText(BlueToothInfoActivity.this, "File Deleted", Toast.LENGTH_SHORT).show();
//        }
        Toast.makeText(BlueToothInfoActivity.this, "Visit data is captured.", Toast.LENGTH_SHORT).show();
        String userRole = context.userService().getUserRole();
        DrishtiSyncScheduler.startOnlyIfConnectedToNetwork(getApplicationContext(), userRole);
        BlueToothInfoActivity.isBluetooth = false;
        this.finish();
        if (context.userService().getFormName().equalsIgnoreCase(AllConstants.FormNames.ANC_VISIT) || context.userService().getFormName().equalsIgnoreCase(AllConstants.FormNames.ANC_VISIT_EDIT))
            startActivity(new Intent(this, NativeANCSmartRegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        else if (context.userService().getFormName().equalsIgnoreCase(AllConstants.FormNames.PNC_VISIT) || context.userService().getFormName().equalsIgnoreCase(AllConstants.FormNames.PNC_VISIT_EDIT))
            startActivity(new Intent(this, NativePNCSmartRegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        else if (context.userService().getFormName().equalsIgnoreCase(AllConstants.FormNames.CHILD_ILLNESS) || context.userService().getFormName().equalsIgnoreCase(AllConstants.FormNames.CHILD_ILLNESS_EDIT))
            startActivity(new Intent(this, NativeChildSmartRegisterActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        else
            startActivity(new Intent(BlueToothInfoActivity.this, NativeHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }*/

    @Override
    public void call() {
        Vector<Integer> _ver = BPBuf.m_buf;
        for (int i = 0; i < _ver.size(); i++) {
            Log.i("........", Integer.toHexString(_ver.get(i) & 0xFF));
        }
    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      isBluetooth = true;
        if (bluetoothAdapter != null & bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.cancelDiscovery();
            bluetoothAdapter.disable();
        }

    }


    private String conversionBGM(String data) {
        try {
            float resultFloat = 18 * Float.parseFloat(data);
            BigDecimal result;
//            result = Math.round(resultFloat, 2);
            return Math.round(resultFloat * 100.0) / 100.0 + "";
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(BlueToothInfoActivity.this, "Blood Glucose Meter: Please enter valid data", Toast.LENGTH_SHORT).show();
        }
        return "";
    }
    @Override
    public void onResumption() {
        BlueToothInfoActivity.isBluetooth = true;
    }


    @Override
    public void onResult(final byte[] resultData, final int deviceNum) {
        Log.e(TAG, "Coming Interface" + deviceNum);
        isProgressRunning = false;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                    bluetoothAdapter.cancelDiscovery();
                }
                if (deviceNum != org.telemedicine.bluetooth.Constants.NO_DEVICE && resultData != null) {
                    if (deviceNum == org.telemedicine.bluetooth.Constants.PULSE_DEVICE_NUM && deviceNum == BlueToothInfoActivity.device) {
                        Log.e(TAG, "SPO2= " + resultData[6]
                                + " BPM= " + resultData[7]);
                    }
                    if (deviceNum == org.telemedicine.bluetooth.Constants.BP_DEVICE_NUM && deviceNum == BlueToothInfoActivity.device) {
                        Log.e(TAG, "Bp Data=" + new String(resultData));
                        for (int i = 0; i < resultData.length; i++) {
                            Log.e(TAG, "Bp Data" + i + resultData[i] + "");
                        }
                        int sys = (resultData[0] << 8 | resultData[1]) & 255;

                        et_bp_sys.setText(sys + "");
                        et_bp_dia.setText(resultData[2] + "");
                        et_bp_heart.setText(resultData[3] + "");
                        new AlertDialog.Builder(BlueToothInfoActivity.this).setTitle("BP").setMessage("BP is captured.").setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }
                    if (deviceNum == org.telemedicine.bluetooth.Constants.EET_DEVICE_NUM && deviceNum == BlueToothInfoActivity.device) {
                        String result = new String(resultData);
                        String result_in_faren = String.format("%.02f", (Float.parseFloat(result) * 1.8) + 32);
                        String tempVal = tv_eet_cen.getText().toString().equalsIgnoreCase("c") ? result : result_in_faren;
                        Log.e(TAG, "Data EET = " + tempVal);
                        if (tempartureFor == 0)
                            et_eet.setText(tempVal);
                        else {
                            EditText editText = (EditText) findViewById(tempartureFor);
                            editText.setText(tempVal);
                        }
                        new AlertDialog.Builder(BlueToothInfoActivity.this).setTitle("Temperature").setMessage("Temperature is captured.").setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }
                    if (deviceNum == org.telemedicine.bluetooth.Constants.FET_DEVICE_NUM && deviceNum == BlueToothInfoActivity.device) {
                        Log.e(TAG, "Data FET= " + Arrays.toString(resultData));
                        String fetalStr = new String(resultData);
                        et_fetal.setText(fetalStr.replace("-", "").trim());
                        new AlertDialog.Builder(BlueToothInfoActivity.this).setTitle("Fetal").setMessage("Fetal heart rate is captured.").setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }
                    if (deviceNum == org.telemedicine.bluetooth.Constants.BLOOD_DEVICE_NUM && deviceNum == BlueToothInfoActivity.device) {
                        String result = new String(resultData);
                        Log.e(TAG, "Data Blood= " + new String(resultData));
                        if (result.trim().length() != 0)
                            try {
                                et_bgm_mg.setText(conversionBGM(result));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(BlueToothInfoActivity.this, "Blood Glucose Meter: Please enter valid data", Toast.LENGTH_SHORT).show();
                            }
                        et_bgm.setText(result + "");
                        new AlertDialog.Builder(BlueToothInfoActivity.this).setTitle("BGM").setMessage("Blood glucose is captured.").setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }
                } else {
                    Toast.makeText(BlueToothInfoActivity.this, "Unable to connect. Please retry",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
