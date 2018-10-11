/*
package org.telemedicine.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.telemedicine.DrugListView;
import org.telemedicine.POCViewActivity;
import org.telemedicine.R;

import java.util.ArrayList;

import static org.telemedicine.R.id.tb_days;
import static org.telemedicine.R.id.tb_direct;
import static org.telemedicine.R.id.tb_dosage;
import static org.telemedicine.R.id.tb_frequency;
import static org.telemedicine.R.id.tb_instruction;


public class PendingDrugListAdapter extends BaseAdapter {
    ArrayList<DrugListView> pendindingdruglist;
    Context context;



    public PendingDrugListAdapter(Context context, ArrayList<DrugListView> pendindingdruglist) {
        this.context = context;
        this.pendindingdruglist = pendindingdruglist;

    }


    @Override
    public int getCount() {
        return pendindingdruglist.size();
    }

    @Override
    public DrugListView getItem(int position) {
        return pendindingdruglist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {


        CheckBox tb_drug;
        TextView tb_direct,tb_dosage,tb_frequency,tb_instruction,tb_days;



    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.row_item, null);
        }


        final ViewHolder holder = new ViewHolder();



        holder.tb_drug = (CheckBox) view.findViewById(R.id.tb_drug);
        holder.tb_direct = (TextView) view.findViewById(tb_direct);
        holder.tb_dosage = (TextView) view.findViewById(tb_dosage);
        holder.tb_frequency = (TextView) view.findViewById(tb_frequency);
        holder.tb_instruction= (TextView) view.findViewById(tb_instruction);
        holder.tb_days = (TextView) view.findViewById(tb_days);
        holder.tb_drug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pendindingdruglist.size()>0)


                POCViewActivity.drugListViews.add(new DrugListView(getItem(position).getDrug(),getItem(position).getDirect(),
                        getItem(position).getDosage(),getItem(position).getFrequency(),getItem(position).getInstruction(),getItem(position).getDays()));

                POCViewActivity.drug_listview.setAdapter(POCViewActivity.editdruglistadapter);
                POCViewActivity.editdruglistadapter.notifyDataSetChanged();

                pendindingdruglist.remove(position);
                POCViewActivity.pendingdrugadapter.notifyDataSetChanged();

            }
        });




        holder.tb_drug.setText(getItem(position).getDrug());
        holder.tb_direct.setText(getItem(position).getDirect());
        holder.tb_dosage.setText(getItem(position).getDosage());
        holder.tb_frequency.setText(getItem(position).getFrequency());
        holder.tb_instruction.setText(getItem(position).getInstruction());
        holder.tb_days.setText(getItem(position).getDays());


        return view;
    }
}
*/
