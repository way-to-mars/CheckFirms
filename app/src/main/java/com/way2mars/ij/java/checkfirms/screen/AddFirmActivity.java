package com.way2mars.ij.java.checkfirms.screen;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.way2mars.ij.java.checkfirms.data.QueryUtils;
import com.way2mars.ij.java.checkfirms.R;
import com.way2mars.ij.java.checkfirms.model.FirmData;
import org.jetbrains.annotations.NotNull;


public class AddFirmActivity extends AppCompatActivity {
    private static final String LOG_TAG = AddFirmActivity.class.getSimpleName();

    private final static String urlPart1 = "https://egrul.itsoft.ru/";
    private final static String urlPart2 = ".json";

    private final static String urlShortPart1 = "https://egrul.itsoft.ru/short_data/?";
    private final static String urlShortPart2 = ".json";

    // Constants to enumerate states of ActivityView
    public static final int START = 1;
    public static final int SEARCHING = 2;
    public static final int READY = 3;
    private boolean start_passed = false;

    private Toast localToast=null;


    private void showToast(String string){
        if(string == null) return;
        if(string.length()==0) return;
        if(localToast != null) localToast.cancel();
        localToast = Toast.makeText(this, string, Toast.LENGTH_LONG);
        localToast.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_firm);

        EditText editText = findViewById(R.id.anf_edittext_inn);
        Button buttonSearch = findViewById(R.id.anf_search_button);
        buttonSearch.setOnClickListener(view -> loadFirm(editText.getText().toString()));

        setView(START);

    }

    // the length of INN must be 10 or 12
    private boolean checkInnString(@NotNull String stringInn){
        int strlen = stringInn.length();
        if ( strlen == 10 || strlen == 12 ) return true;
        showToast("ИНН должен состоять из 10 или 12 цифр");
        return false;
    }

    // convert inn to "https://egrul.itsoft.ru/{inn}.json"
    @NotNull
    private String convertToUrlString(@NotNull String stringInn){
        return urlPart1 + stringInn + urlPart2;
    }

    // convert inn to "https://egrul.itsoft.ru/short_data/?{inn}.json"
    @NotNull
    private String convertToUrlShortString(@NotNull String stringInn){
        return urlShortPart1 + stringInn + urlShortPart2;
    }

    private void loadFirm(@NotNull String stringInn){
        if( !checkInnString(stringInn) ) return;

        String urlString = convertToUrlString(stringInn);
        String urlShortString = convertToUrlShortString(stringInn);

        setView(SEARCHING);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FirmData firm;  //null
                try {
                    firm = QueryUtils.fetchFirmData(urlString, urlShortString);
                    // Thread.sleep(3000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                FirmData finalFirm = firm;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayNewFirm(finalFirm);
                    }
                });
            }
        };

        Thread webThread = new Thread(runnable);
        webThread.start();
    }

    private void displayNewFirm(final FirmData firmDataInput){
        FirmData firmData;

        TextView shortName = findViewById(R.id.anf_shortname);
        TextView longName = findViewById(R.id.anf_longname);
        TextView ogrn = findViewById(R.id.anf_ogrn);
        TextView ogrnDate = findViewById(R.id.anf_ogrn_date);
        TextView inn = findViewById(R.id.anf_inn);
        TextView kpp = findViewById(R.id.anf_kpp);
        TextView chiefPosition = findViewById(R.id.anf_chief_position);
        TextView chiefFullName = findViewById(R.id.anf_chief_fullname);
        TextView addressStatus = findViewById(R.id.anf_address_status);
        TextView address = findViewById(R.id.anf_address);
        TextView dateLiquidation = findViewById(R.id.anf_date_liquidation);
        TextView reasonLiquidation = findViewById(R.id.anf_reason_liquidation);
        TextView dateLastRecord = findViewById(R.id.anf_date_last_record);
        TextView textLastRecord = findViewById(R.id.anf_text_last_record);
        View layoutLiquidate = findViewById(R.id.anf_layout_liquidate);
        View layoutLiquireason = findViewById(R.id.anf_layout_liquireason);
        GradientDrawable addressCircle = (GradientDrawable) addressStatus.getBackground();

        if(firmDataInput == null){
            setView(START);
            showToast("Организация с таким ИНН не найдена!");
            firmData = new FirmData();
            addressStatus.setText(getString(R.string.anf_unknown_status_text));
            addressCircle.setColor(ContextCompat.getColor(this, R.color.address_unknown));
        }
        else{
            firmData = firmDataInput;
            Log.d(LOG_TAG, firmData.toString());
            setView(READY);
            if(firmData.hasAddressWarning()){
                addressStatus.setText("!");
                addressCircle.setColor(ContextCompat.getColor(this, R.color.address_warning));
            }
            else {
                addressStatus.setText("✔");
                addressCircle.setColor(ContextCompat.getColor(this, R.color.address_ok));
            }
        }

        shortName.setText(firmData.getValueDefault(FirmData.SHORT_NAME, "n/a"));
        longName.setText(firmData.getValueDefault(FirmData.FULL_NAME, "n/a"));
        ogrn.setText(firmData.getValueDefault(FirmData.OGRN, "n/a"));
        ogrnDate.setText(firmData.getValueDefault(FirmData.DATE_OGRN, "n/a"));
        inn.setText(firmData.getValueDefault(FirmData.INN, "n/a"));
        kpp.setText(firmData.getValueDefault(FirmData.KPP, "n/a"));
        chiefPosition.setText(firmData.getValueDefault(FirmData.CHIEF_POSITION, "Руководитель"));
        chiefFullName.setText(firmData.getValueDefault(FirmData.CHIEF_FULLNAME, "n/a"));
        address.setText(firmData.getValueDefault(FirmData.ADDRESS, "n/a"));
        dateLastRecord.setText(firmData.getValueDefault(FirmData.DATE_LAST_RECORD,"n/a"));
        textLastRecord.setText(firmData.getValueDefault(FirmData.TEXT_LAST_RECORD,"n/a"));

        if(firmData.isLiquidated()) {
            layoutLiquidate.setVisibility(View.VISIBLE);
            layoutLiquireason.setVisibility(View.VISIBLE);
            dateLiquidation.setText(firmData.getValueDefault(FirmData.DATE_LIQUIDATION, "n/a"));
            reasonLiquidation.setText(firmData.getValueDefault(FirmData.REASON_LIQUIDATION, "n/a"));
        }
        else{
            layoutLiquidate.setVisibility(View.GONE);
            layoutLiquireason.setVisibility(View.GONE);
        }

    }

    private void setView(int state){
        EditText editTextInn = findViewById(R.id.anf_edittext_inn);
        Button buttonSearch = findViewById(R.id.anf_search_button);
        Button buttonApply = findViewById(R.id.anf_apply_button);
        ScrollView scrollView = findViewById(R.id.anf_scroll_view);
        ProgressBar progressBar = findViewById(R.id.anf_progress_bar);


        switch (state){
            case START:
                editTextInn.setEnabled(true);
                buttonSearch.setEnabled(true);
                buttonApply.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;
            case SEARCHING:
                editTextInn.setEnabled(false);
                buttonSearch.setEnabled(false);
                buttonApply.setEnabled(false);
                scrollView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            case READY:
                editTextInn.setEnabled(true);
                buttonSearch.setEnabled(true);
                buttonApply.setVisibility(View.VISIBLE);
                buttonApply.setEnabled(true);
                scrollView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;
        }
    }

}
