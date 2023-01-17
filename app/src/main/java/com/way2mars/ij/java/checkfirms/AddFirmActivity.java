package com.way2mars.ij.java.checkfirms;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class AddFirmActivity extends AppCompatActivity {
    private static final String LOG_TAG = AddFirmActivity.class.getSimpleName();

    private final static String urlPart1 = "https://egrul.itsoft.ru/";
    private final static String urlPart2 = ".json";

    private Toast localToast=null;
    private Button searchButton = null, applyButton = null;
    // private Document mDoc;

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
        searchButton = findViewById(R.id.anf_search_button);
        applyButton = findViewById(R.id.anf_apply_button);
        EditText editText = findViewById(R.id.anf_edittext_inn);
        searchButton.setOnClickListener(view -> loadFirm(editText.getText().toString()));
       // applyButton.setVisibility(View.GONE);
        applyButton.setEnabled(false);
    }

    // the length of INN must be 10 or 12
    private boolean checkInnString(@NotNull String stringInn){
        int strlen = stringInn.length();
        if ( strlen == 10 || strlen == 12 ) return true;
        showToast("ИНН должен состоять из 10 или 12 цифр");
        return false;
    }

    // convert inn to "https://egrul.itsoft.ru/{inn}.json.gz"
    @NotNull
    private String convertToUrlString(@NotNull String stringInn){
        return urlPart1 + stringInn + urlPart2;
    }

    private void loadFirm(@NotNull String stringInn){
        if( !checkInnString(stringInn) ) return;

        searchButton.setEnabled(false);

        String urlString = convertToUrlString(stringInn);

        showToast("Строка запроса: " + urlString);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FirmData firm = null;
                try {
                    firm = QueryUtils.fetchFirmData(urlString);
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

        applyButton.setEnabled(true);
        searchButton.setEnabled(true);
       // applyButton.setVisibility(View.VISIBLE);
    }

    private void displayNewFirm(FirmData firmData){
        TextView shortName = findViewById(R.id.anf_shortname);
        TextView longName = findViewById(R.id.anf_longname);

        shortName.setText(firmData.getShortName());
        longName.setText(firmData.getKeyValue("longName"));
    }
}
