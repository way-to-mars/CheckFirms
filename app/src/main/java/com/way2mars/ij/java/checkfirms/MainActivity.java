package com.way2mars.ij.java.checkfirms;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "MainActivity LOG";

    Toast mainToast=null;
    ArrayList<FirmData> mainArray=null;
    FirmListAdapter mainAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<FirmData> firms = QueryUtils.createFakeList();
        ListView listView = findViewById(R.id.firms_list);

        final FirmListAdapter adapter = new FirmListAdapter(this, firms);
        this.mainArray = firms;
        this.mainAdapter = adapter;

        listView.setAdapter(adapter);

        View viewHeader = getLayoutInflater().inflate(R.layout.firms_list_header, null);
        listView.addHeaderView(viewHeader);
        listView.addFooterView(viewHeader);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                showToast(view.getContext(), "itemClick: position = " + position + ", id = "
                        + id, Toast.LENGTH_SHORT);
                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
                        + id + view.getTag());
                if( id == -1) addItem();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d(LOG_TAG, "scrollState = " + scrollState);
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                Log.d(LOG_TAG, "scroll: firstVisibleItem = " + firstVisibleItem
                        + ", visibleItemCount" + visibleItemCount
                        + ", totalItemCount" + totalItemCount);
            }
        });
    }

    private void showToast(Context context, CharSequence text, int duration){
        if (this.mainToast != null) this.mainToast.cancel();
        this.mainToast = Toast.makeText(context, text, duration);
        this.mainToast.show();
    }

    public void addItem(){
        this.mainArray.add(0, new FirmData("new name", "new inn", "2024-12-12", "new text"));
        this.mainAdapter.notifyDataSetChanged();
    }

}