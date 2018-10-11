package org.telemedicine.bluetooth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.telemedicine.Constants;
import org.telemedicine.R;
//import org.telemedicine.view.customControls.CustomFontTextView;

/**
 * Created by Naveen on 2/29/2016.
 */
public class BluetoothInstructionsActivity extends Activity {
   // org.telemedicine.view.customControls.CustomFontTextView tv_dev_desc;
    LinearLayout ll_dev_inst;
    String devType;
    Button bt_ok;
    LinearLayout ll_bgm, ll_bp, ll_eet, ll_fetal, ll_steh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);\
        setContentView(R.layout.bluetooth_instructions);
        this.setFinishOnTouchOutside(false);
        bt_ok = (Button) findViewById(R.id.bt_ok);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            devType = bundle.getString("device");
        }
        ll_bgm = (LinearLayout) findViewById(R.id.ll_bgm);
        ll_bp = (LinearLayout) findViewById(R.id.ll_bp);
        ll_eet = (LinearLayout) findViewById(R.id.ll_eet);
        ll_fetal = (LinearLayout) findViewById(R.id.ll_fetal);
        ll_steh = (LinearLayout) findViewById(R.id.ll_steh);

        switch (devType) {
            case "bgm":
                changeVisibility(ll_bgm);
                TextView bgm_step2 = (TextView) findViewById(R.id.bgm_step2);
                SpannableStringBuilder builder = new SpannableStringBuilder();
                builder.append(bgm_step2.getText().toString()).append(" ");
                builder.setSpan(new ImageSpan(this, R.drawable.bgm_1),
                        builder.length() - 1, builder.length(), 0);
                builder.append(" to do the blood collection");
                bgm_step2.setText(builder);

                changeTitle("Blood Glucose Meter");
                break;
            case "bp":
                changeVisibility(ll_bp);
                changeTitle("Electronic Sphygmomanometer");
                break;
            case "eet":
                changeVisibility(ll_eet);
                TextView eet_1 = (TextView) findViewById(R.id.eet_1);
                SpannableStringBuilder builder_e1 = new SpannableStringBuilder();
                builder_e1.append(eet_1.getText().toString()).append(" ");
                builder_e1.setSpan(new ImageSpan(this, R.drawable.eet_1),
                        builder_e1.length() - 1, builder_e1.length(), 0);
                builder_e1.append(" ON/OFF button to turn on the device");
                eet_1.setText(builder_e1);
                changeTitle("Ear Thermometer");
                break;
            case "fetal":
                changeVisibility(ll_fetal);
                changeTitle("Fetal Doppler");

                TextView fetal_1 = (TextView) findViewById(R.id.fetal_1);
                SpannableStringBuilder builder_f1 = new SpannableStringBuilder();
                builder_f1.append(fetal_1.getText().toString()).append(" ");
                builder_f1.setSpan(new ImageSpan(this, R.drawable.fetal_1),
                        builder_f1.length() - 1, builder_f1.length(), 0);
                builder_f1.append(" to do the blood collection");
                fetal_1.setText(builder_f1);

                TextView fetal_2 = (TextView) findViewById(R.id.fetal_2);
                SpannableStringBuilder builder_f2 = new SpannableStringBuilder();
                builder_f2.append(fetal_2.getText().toString()).append(" ");
                builder_f2.setSpan(new ImageSpan(this, R.drawable.fetal_2),
                        builder_f2.length() - 1, builder_f2.length(), 0);
                builder_f2.append(" to get id and number on LCD display");
                fetal_2.setText(builder_f2);

                TextView fetal_7 = (TextView) findViewById(R.id.fetal_7);
                SpannableStringBuilder builder_f3 = new SpannableStringBuilder();
                builder_f3.append(fetal_7.getText().toString()).append(" ");
                builder_f3.setSpan(new ImageSpan(this, R.drawable.fetal_7),
                        builder_f3.length() - 1, builder_f3.length(), 0);
                fetal_7.setText(builder_f3);

                TextView fetal_8 = (TextView) findViewById(R.id.fetal_8);
                SpannableStringBuilder builder_f4 = new SpannableStringBuilder();
                builder_f4.append(fetal_8.getText().toString()).append(" ");
                builder_f4.setSpan(new ImageSpan(this, R.drawable.fetal_8),
                        builder_f4.length() - 1, builder_f4.length(), 0);
                builder_f4.append(" in case of message ' do you want to delete records'");
                fetal_8.setText(builder_f4);

                break;
            case "steh":
                changeVisibility(ll_steh);
                changeTitle("E-Stethoscope");
                TextView steh_1 = (TextView) findViewById(R.id.tv_steh1);
                SpannableStringBuilder builder_s1 = new SpannableStringBuilder();
                builder_s1.append(steh_1.getText().toString()).append(" ");
                builder_s1.setSpan(new ImageSpan(this, R.drawable.steh_1),
                        builder_s1.length() - 1, builder_s1.length(), 0);
                builder_s1.append(" for 3 sec to turn on the device and graph will be displayed");
                steh_1.setText(builder_s1);
                break;
        }
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Constants.INSTRUCTS_INFO_RESULT_CODE, new Intent().putExtra("device", devType));
                finish();
            }
        });
//        tv_dev_desc = (CustomFontTextView) findViewById(R.id.tv_bt_dev_desc);
//        ll_dev_inst = (LinearLayout) findViewById(R.id.ll_bt_dev_inst);
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            devType = bundle.getString("device");
//        }
//        switch (devType.toLowerCase()) {
//            case "bp":
////                loadData();
//                break;
//        }
//    }
//
//    private void loadData(String desc, ArrayList<String> list) {
//        tv_dev_desc.setText(desc);
//        for (String str : list) {
//            TextView tv_diseaseName = new TextView(this);
//            tv_diseaseName.setText(str);
//            tv_diseaseName.setTextColor(org.ei.telemedicine.Context.getInstance().getColorResource(R.color.light_blue));
//            tv_diseaseName.setTextSize(25);
//            tv_diseaseName.setPadding(10, 10, 10, 10);
//            ll_dev_inst.addView(tv_diseaseName);
//        }
    }

    private void changeVisibility(LinearLayout visibleLayout) {
        ll_bgm.setVisibility(View.GONE);
        ll_bp.setVisibility(View.GONE);
        ll_eet.setVisibility(View.GONE);
        ll_fetal.setVisibility(View.GONE);
        ll_steh.setVisibility(View.GONE);
        visibleLayout.setVisibility(View.VISIBLE);
    }

    private void changeTitle(String title) {
        if (this.getActionBar() != null)
            getActionBar().setTitle(title);
    }
}
