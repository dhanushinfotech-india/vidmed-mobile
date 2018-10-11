package org.telemedicine.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.telemedicine.R;
import org.telemedicine.model.Consultationlist;

import java.util.ArrayList;

/**
 * Created by bhavana on 3/15/17.
 */
public class ConsultationAdapter extends BaseAdapter {

    ArrayList<Consultationlist> consultationlists;
    Context context;

    public ConsultationAdapter(ArrayList<Consultationlist> consultationlists, Context context) {
        this.consultationlists = consultationlists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return consultationlists.size();
    }

    @Override
    public Object getItem(int position) {
        return consultationlists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder {
        TextView tv_srno,tv_patientname,tv_typeof,tv_docname,tv_pocdate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.consultation_list_item_row, null);

        }
        final ViewHolder holder = new ViewHolder();
        holder.tv_srno = (TextView) view.findViewById(R.id.srno);
        holder.tv_patientname = (TextView) view.findViewById(R.id.patientName);
        holder.tv_typeof = (TextView) view.findViewById(R.id.type_cons);
        holder.tv_docname = (TextView) view.findViewById(R.id.doc_name);
        holder.tv_pocdate= (TextView) view.findViewById(R.id.poc_date);
        Consultationlist item = consultationlists.get(position);

        holder.tv_srno.setText(item.getSrno());
        holder.tv_patientname.setText(item.getPatientname());
        holder.tv_typeof.setText(item.getTypeofconsultation());
        holder.tv_docname.setText(item.getDocName());
        holder.tv_pocdate.setText(item.getPocDate());


        return view;
    }
}
