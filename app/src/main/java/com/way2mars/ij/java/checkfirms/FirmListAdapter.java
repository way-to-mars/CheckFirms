package com.way2mars.ij.java.checkfirms;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class FirmListAdapter extends ArrayAdapter<FirmData> {

    public FirmListAdapter(@NonNull Context context, @NonNull List<FirmData> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FirmData currentFirm = getItem(position);

        View listItemView;
        ImageView imageButton;

        if (currentFirm.isLuqiudated()) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.firms_list_dead_item, parent, false);
            if (listItemView == null) return convertView;
            ((TextView) listItemView.findViewById(R.id.fld_item_name)).setText(currentFirm.getShortName());
            ((TextView) listItemView.findViewById(R.id.fld_item_inn)).setText(currentFirm.getInn());
            ((TextView) listItemView.findViewById(R.id.fld_item_last_date)).setText(currentFirm.getDateLastChange());
            ((TextView) listItemView.findViewById(R.id.fld_item_last_text)).setText(currentFirm.getTextLastChange());
            ((TextView) listItemView.findViewById(R.id.fld_item_liquidation_date)).
                    setText(currentFirm.getDateOfLiquidaton());
            ((TextView) listItemView.findViewById(R.id.fld_item_liquidation_text)).
                    setText(currentFirm.getReasonOfLiquidaton());
            imageButton = listItemView.findViewById(R.id.fld_item_button);
        } else {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.firms_list_alive_item, parent, false);
            if (listItemView == null) return convertView;
            ((TextView) listItemView.findViewById(R.id.fla_item_name)).setText(currentFirm.getShortName());
            ((TextView) listItemView.findViewById(R.id.fla_item_inn)).setText(currentFirm.getInn());
            ((TextView) listItemView.findViewById(R.id.fla_item_last_date)).setText(currentFirm.getDateLastChange());
            ((TextView) listItemView.findViewById(R.id.fla_item_last_text)).setText(currentFirm.getTextLastChange());
            imageButton = listItemView.findViewById(R.id.fla_item_button);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("001100");
            }
        });
        return listItemView;
    }
}
