package com.way2mars.ij.java.checkfirms.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.way2mars.ij.java.checkfirms.R;
import com.way2mars.ij.java.checkfirms.model.FirmData;

import java.util.List;

public class FirmListAdapter extends ArrayAdapter<FirmData> {

    private static final String NOTEXT = "нет данных";
    private static final String NOVALUE = "0";

    public FirmListAdapter(@NonNull Context context, @NonNull List<FirmData> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FirmData currentFirm = getItem(position);

        View listItemView;
        ImageView imageButton;

        if (currentFirm.isLiquidated()) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.firms_list_dead_item, parent, false);
            if (listItemView == null) return convertView;
            ((TextView) listItemView.findViewById(R.id.fld_item_name)).
                    setText(currentFirm.getValueDefault(currentFirm.SHORT_NAME, NOTEXT));
            ((TextView) listItemView.findViewById(R.id.fld_item_inn)).
                    setText(currentFirm.getValueDefault(currentFirm.INN, NOVALUE));
            ((TextView) listItemView.findViewById(R.id.fld_item_last_date)).
                    setText(currentFirm.getValueDefault(currentFirm.DATE_LAST_RECORD, NOTEXT));
            ((TextView) listItemView.findViewById(R.id.fld_item_last_text)).
                    setText(currentFirm.getValueDefault(currentFirm.TEXT_LAST_RECORD, NOTEXT));
            ((TextView) listItemView.findViewById(R.id.fld_item_liquidation_date)).
                    setText(currentFirm.getValueDefault(currentFirm.DATE_LIQUIDATION, NOTEXT));
            ((TextView) listItemView.findViewById(R.id.fld_item_liquidation_text)).
                    setText(currentFirm.getValueDefault(currentFirm.REASON_LIQUIDATION, NOTEXT));
            imageButton = listItemView.findViewById(R.id.fld_item_button);
        } else {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.firms_list_alive_item, parent, false);
            if (listItemView == null) return convertView;
            ((TextView) listItemView.findViewById(R.id.fla_item_name)).
                    setText(currentFirm.getValueDefault(currentFirm.SHORT_NAME, NOTEXT));
            ((TextView) listItemView.findViewById(R.id.fla_item_inn)).
                    setText(currentFirm.getValueDefault(currentFirm.INN, NOVALUE));
            ((TextView) listItemView.findViewById(R.id.fla_item_last_date)).
                    setText(currentFirm.getValueDefault(currentFirm.DATE_LAST_RECORD, NOTEXT));
            ((TextView) listItemView.findViewById(R.id.fla_item_last_text)).
                    setText(currentFirm.getValueDefault(currentFirm.TEXT_LAST_RECORD, NOTEXT));
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


    @Override
    public void notifyDataSetChanged() {
        this.setNotifyOnChange(false);

//        this.sort(new Comparator<FirmData>() {
//            @Override
//            public int compare(FirmData firmData1, FirmData firmData2) {
//                return firmData1.compareTo(firmData2);
//            }
//        });

        this.sort(new FirmDataComparator());

        this.setNotifyOnChange(true);

        super.notifyDataSetChanged();
    }
}
