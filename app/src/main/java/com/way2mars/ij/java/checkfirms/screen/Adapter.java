package com.way2mars.ij.java.checkfirms.screen;

import android.annotation.SuppressLint;
import android.service.autofill.TextValueSanitizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.way2mars.ij.java.checkfirms.R;
import com.way2mars.ij.java.checkfirms.model.FirmStorage;

import org.w3c.dom.Text;

public class Adapter extends RecyclerView.Adapter<> {


    static class FirmViewHolder extends RecyclerView.ViewHolder {

        TextView textName;
        TextView textInn;
        View linearLiquidated;
        View linearAddress;
        TextView textDateEgrul;
        TextView textEgrul;
        TextView textDateCourt;
        TextView textCourt;

        FirmStorage firmStorage;

        public FirmViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.item_name);
            textInn = itemView.findViewById(R.id.item_inn);
            linearLiquidated = itemView.findViewById(R.id.item_liquidated);
            linearAddress = itemView.findViewById(R.id.item_address_warning);
            textDateEgrul = itemView.findViewById(R.id.item_date_egrul);
            textEgrul = itemView.findViewById(R.id.item_text_egrul);
            textDateCourt = itemView.findViewById(R.id.item_date_court);
            textCourt = itemView.findViewById(R.id.item_text_court);
        }

        public void bind(FirmStorage firm)
        {
            this.firmStorage = firm;
            textName.setText(firm.shortName);
            textInn.setText("ИНН " + firm.inn);

            if (firm.addressWarning)
                    linearAddress.setVisibility(View.VISIBLE);
            else
                    linearAddress.setVisibility(View.GONE);

            if(firm.dateLiquidation.length()>0)
                linearLiquidated.setVisibility(View.VISIBLE);
            else
                linearLiquidated.setVisibility(View.GONE);
            textDateEgrul.setText(firm.dateLastRecord);
            textEgrul.setText(firm.textLastRecord);

            textDateCourt.setText(firm.dateLastCourtAction);
            textCourt.setText(firm.numberLastCourtAction);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
