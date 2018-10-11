/*
package org.telemedicine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import static org.telemedicine.R.id.tb_days;
import static org.telemedicine.R.id.tb_direct;
import static org.telemedicine.R.id.tb_dosage;
import static org.telemedicine.R.id.tb_frequency;
import static org.telemedicine.R.id.tb_instruction;

*/
/**
 * Created by suneel on 4/10/16.
 *//*


public class EditDrugAdapter extends BaseAdapter {
    ArrayList<DrugListView> druglistview;
    Context context;



    public EditDrugAdapter(Context context, ArrayList<DrugListView> druglistview) {
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


        CheckBox tb_drug;
        TextView tb_direct,tb_frequency,tb_days,tb_instruction,tb_dosage;



    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

       //     view = inflater.inflate(R.layout.edit_row_item, null);
        }


        final EditDrugAdapter.ViewHolder holder = new EditDrugAdapter.ViewHolder();



      //  holder.tb_drug = (CheckBox) view.findViewById(R.id.tb_drug);
        holder.tb_direct = (TextView) view.findViewById(tb_direct);
        holder.tb_dosage = (TextView) view.findViewById(tb_dosage);
        holder.tb_frequency = (TextView) view.findViewById(tb_frequency);
        holder.tb_instruction= (TextView) view.findViewById(tb_instruction);
        holder.tb_days = (TextView) view.findViewById(tb_days);
        holder.tb_drug.setChecked(true);
        holder.tb_drug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (druglistview.size()>0)


                POCViewActivity.pendingdrugslist.add(new DrugListView(getItem(position).getDrug(),getItem(position).getDirect(),
                        getItem(position).getDosage(),getItem(position).getFrequency(),getItem(position).getInstruction(),getItem(position).getDays()));

                POCViewActivity.pendingdrug_listview.setAdapter(POCViewActivity.pendingdrugadapter);
                POCViewActivity.pendingdrugadapter.notifyDataSetChanged();
                druglistview.remove(position);
                POCViewActivity.editdruglistadapter.notifyDataSetChanged();

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
