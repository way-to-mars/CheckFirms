package com.way2mars.ij.java.checkfirms;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;

public class AddFirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_firm);
        Button searchButton = findViewById(R.id.anf_button);
        EditText editText = findViewById(R.id.anf_edittext_inn);
        searchButton.setOnClickListener(view -> {
                String strInn = editText.getText().toString();


            });
    }

    // the length of INN must be 10 or 12
    private boolean checkInnString(@NotNull String stringInn){
        int strlen = stringInn.length();

        if ( strlen == 10 || strlen == 12 ) return true;

        Toast.makeText(this, "ИНН должен состоять из 10 или 12 цифр", Toast.LENGTH_SHORT).show();
        return false;
    }

}
