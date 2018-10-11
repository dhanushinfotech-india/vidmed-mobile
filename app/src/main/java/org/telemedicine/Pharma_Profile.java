package org.telemedicine;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bhavana on 8/24/16.
 */
public class Pharma_Profile extends SuperActivity {

    private ImageView imageViewRound;
    CircleImageView circleView_pharma;
    String p_name;
    TextView pharma_name, pharma_id, pharma_email;
    ImageView logout;

    DatePickerDialog.OnDateSetListener date;
    long fromdate;


    @Override
    protected void onCreation(Bundle savedInstanceState) {

        setContentView(R.layout.pharma_profile);

        pharma_name = (TextView) findViewById(R.id.pharma_imgname);
        pharma_id = (TextView) findViewById(R.id.id_pharma);
        pharma_email = (TextView) findViewById(R.id.email_pharma);
        circleView_pharma = (CircleImageView) findViewById(R.id.circleView);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
        circleView_pharma.setImageBitmap(icon);

        logout = (ImageView) findViewById(R.id.logout_image);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });


        Cursor profilePharmaCursor = dbHelper.getProfile();
        while (profilePharmaCursor.moveToNext()) {
            p_name = profilePharmaCursor.getString(profilePharmaCursor.getColumnIndex("firstName")) + profilePharmaCursor.getString(profilePharmaCursor.getColumnIndex("lastName"));
            pharma_name.setText(p_name);
            pharma_email.setText(profilePharmaCursor.getString(profilePharmaCursor.getColumnIndex("email")));
            pharma_id.setText(profilePharmaCursor.getString(profilePharmaCursor.getColumnIndex("userId")));
        }


    }

    @Override
    public void onClick(View view) {

    }


}
