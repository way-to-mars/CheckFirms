package com.way2mars.ij.java.checkfirms;

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
import org.jetbrains.annotations.NotNull;


public class AddFirmActivity extends AppCompatActivity {
    private static final String LOG_TAG = AddFirmActivity.class.getSimpleName();

    private final static String urlPart1 = "https://egrul.itsoft.ru/";
    private final static String urlPart2 = ".json";

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

    // convert inn to "https://egrul.itsoft.ru/{inn}.json.gz"
    @NotNull
    private String convertToUrlString(@NotNull String stringInn){
        return urlPart1 + stringInn + urlPart2;
    }

    private void loadFirm(@NotNull String stringInn){
        if( !checkInnString(stringInn) ) return;

        String urlString = convertToUrlString(stringInn);

        showToast("Строка запроса: " + urlString);

        setView(SEARCHING);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                FirmData firm;  //null
                try {
                    firm = QueryUtils.fetchFirmData(urlString);
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

    private void displayNewFirm(FirmData firmData){
        if(firmData == null){
            setView(START);
            showToast("Организация с таким ИНН не найдена!");
            return;
        }

        TextView shortName = findViewById(R.id.anf_shortname);
        TextView longName = findViewById(R.id.anf_longname);

        shortName.setText(firmData.getShortName());
        longName.setText(firmData.getKeyValue("longName"));

        setView(READY);
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
