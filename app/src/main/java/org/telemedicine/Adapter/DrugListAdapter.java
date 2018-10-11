package org.telemedicine.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.telemedicine.DrugListView;
import org.telemedicine.R;

import java.util.ArrayList;


public class DrugListAdapter extends BaseAdapter {
    ArrayList<DrugListView> druglistview;
    Context context;



    public DrugListAdapter(Context context, ArrayList<DrugListView> druglistview) {
        this.context = context;
        this.druglistview = druglistview;

    }


    @Override
    public int getCount() {
        return druglistview.size();
    }

    @Override
    public DrugListView getItem(int position) {
        return druglistview.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {


        TextView tb_drug,tb_direct,tb_dosage,tb_frequency,tb_instruction,tb_days;



    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.drugrow_item, null);
        }


        final ViewHolder holder = new ViewHolder();



        holder.tb_drug = (TextView) view.findViewById(R.id.tb_drug);
        holder.tb_direct = (TextView) view.findViewById(R.id.tb_direct);
        holder.tb_dosage = (TextView) view.findViewById(R.id.tb_dosage);
        holder.tb_frequency = (TextView) view.findViewById(R.id.tb_frequency);
        holder.tb_instruction= (TextView) view.findViewById(R.id.tb_instruction);
        holder.tb_days = (TextView) view.findViewById(R.id.tb_days);



        holder.tb_drug.setText(getItem(position).getDrug());
        holder.tb_direct.setText(getItem(position).getDirect());
        holder.tb_dosage.setText(getItem(position).getDosage());
        holder.tb_frequency.setText(getItem(position).getFrequency());
        holder.tb_instruction.setText(getItem(position).getInstruction());
        holder.tb_days.setText(getItem(position).getDays());



        return view;
    }
}
