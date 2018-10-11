package org.telemedicine;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bhavana on 8/23/16.
 */
public class Doctor_profile extends SuperActivity {


    CircleImageView circleView_doc;
    TextView doc_name, doc_id, doc_email;
    String d_name;
    ImageView logout;

    @Override
    protected void onCreation(Bundle savedInstanceState) {
        setContentView(R.layout.doctor_profile);

        doc_name = (TextView) findViewById(R.id.doc_imgname);
        doc_id = (TextView) findViewById(R.id.id_doc);
        doc_email = (TextView) findViewById(R.id.email_doc);
        circleView_doc = (CircleImageView) findViewById(R.id.circleView);

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.doc_image);
        circleView_doc.setImageBitmap(icon);

        logout = (ImageView) findViewById(R.id.logout_image);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();

            }
        });

        Cursor profileCursor = dbHelper.getProfile();
        while (profileCursor.moveToNext()) {
            d_name = profileCursor.getString(profileCursor.getColumnIndex("firstName")) + profileCursor.getString(profileCursor.getColumnIndex("lastName"));
            doc_name.setText(d_name);
            doc_email.setText(profileCursor.getString(profileCursor.getColumnIndex("email")));
            doc_id.setText(profileCursor.getString(profileCursor.getColumnIndex("userId")));
        }
    }


    @Override
    public void onClick(View view) {

    }

}
