package org.telemedicine.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import org.telemedicine.R;
import org.telemedicine.model.Patient;

import java.util.ArrayList;

/**
 * Created by Naveen.k on 8/10/2016.
 */
public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.MyViewHolder> {
    private final OnItemClickListener listener;
    ArrayList<Patient> patientsList;
    String image;
    Bitmap icon;
    String dat;
    Context context;

    public PatientsAdapter(Context context, ArrayList<Patient> patientsList, OnItemClickListener listener) {
        this.patientsList = patientsList;
        this.listener = listener;
        this.context = context;

    }

    public void setFilter(ArrayList<Patient> filteredModelList) {
        patientsList = new ArrayList<>();
        patientsList.addAll(filteredModelList);
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patient_list_item_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(patientsList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return patientsList.size();
    }

    /*Converting string image to bitmap*/
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
//            Log.d("encodinfbitmaptostring", "=====" + encodedString);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(Patient itemPatient);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, gender, mobile, email, last_update, validity, renewal;
        ImageView logo;

        public MyViewHolder(View view) {
            super(view);

            last_update = (TextView) view.findViewById(R.id.txt_last_Update);
            validity = (TextView) view.findViewById(R.id.txt_validity);
            name = (TextView) view.findViewById(R.id.name);
            gender = (TextView) view.findViewById(R.id.gender);
            mobile = (TextView) view.findViewById(R.id.mobile);
            email = (TextView) view.findViewById(R.id.email);
            logo = (ImageView) view.findViewById(R.id.logo);
            renewal = (TextView) view.findViewById(R.id.renewal_Update);
        }

        public void bind(final Patient patient, final OnItemClickListener listener) {

           /* Calendar ca = Calendar.getInstance();
            SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = sd.format(ca.getTime());

            dat=patient.getRenewal_date();


            if (dat!=null&&dat.length() != 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Calendar c = Calendar.getInstance();
                try {

                    c.setTime(sdf.parse(dat));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }*/


            name.setText(patient.getFirstName() + " " + patient.getLastName());
            gender.setText(patient.getGender());
            mobile.setText(patient.getPhoneNo());
            email.setText(patient.getEmailId());
            last_update.setText(patient.getLastupdated());
            validity.setText(patient.getValid_date());
            renewal.setText(patient.getRenewal_date());

//            Log.d("patientstable", "=======" + patient.getFirstName() + "===" + patient.getLastupdated());

            logo.setImageDrawable(TextDrawable.builder().buildRound((patient.getFirstName().charAt(0) + "").toUpperCase(), Color.parseColor("#607D8B")));
           /* Calendar c = Calendar.getInstance();
            final SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
            final String correct = sdf1.format(c.getTime());*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  try {
                        if(sdf1.parse(correct).after(sdf1.parse(patient.getValid_date())))
                            Toast.makeText(context,"Account is Expired", Toast.LENGTH_SHORT).show();*/
                      /*  else*/
                    listener.onItemClick(patient);
/*
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*/
                }
            });

        }
    }

}
