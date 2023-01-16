package com.way2mars.ij.java.checkfirms;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;


public class AddFirmActivity extends AppCompatActivity {
    private Toast localToast=null;
    private final static String urlPart1 = "https://egrul.itsoft.ru/";
    private final static String urlPart2 = ".json";

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
        Button searchButton = findViewById(R.id.anf_button);
        EditText editText = findViewById(R.id.anf_edittext_inn);
        searchButton.setOnClickListener(view -> loadFirm(editText.getText().toString()));
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

        QueryUtils.fetchFirmData(urlString);

    }
}
