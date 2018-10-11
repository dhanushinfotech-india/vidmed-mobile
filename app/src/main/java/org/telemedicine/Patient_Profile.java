package org.telemedicine;

/**
 * Created by hareesh on 8/11/16.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Patient_Profile extends SuperActivity {

     private ImageView imageViewRound;
     CircleImageView circleView;
     TextView call;
     ImageView logout;
    String fname;


    @Override
    protected void onCreation(Bundle savedInstanceState) {
        setContentView(R.layout.profile_info_withpoc);
        logout= (ImageView) findViewById(R.id.logout_image);
        circleView = (CircleImageView) findViewById(R.id.circleView);
         Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.circularimage);

        /*fname=PatientRegisterationActivity.savefname;
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"IMG_"+fname+ ".jpg");
        Bitmap bmp = BitmapFactory.decodeFile(mediaStorageDir.getAbsolutePath());
        Log.d("imagepath_patient","=========="+mediaStorageDir);*/


        circleView.setImageBitmap(icon);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(Patient_Profile.this).setMessage("Do you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveStringInPref("isUserloged","no");
                                Intent in=new Intent(Patient_Profile.this,LoginActivity.class);
                                startActivity(in);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}



