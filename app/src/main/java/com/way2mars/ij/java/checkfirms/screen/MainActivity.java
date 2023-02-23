package com.way2mars.ij.java.checkfirms.screen;

import android.bluetooth.le.AdvertisingSetParameters;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.way2mars.ij.java.checkfirms.data.QueryUtils;
import com.way2mars.ij.java.checkfirms.R;
//import com.way2mars.ij.java.checkfirms.databinding.ActivityMainBinding;
import com.way2mars.ij.java.checkfirms.model.FirmData;
import com.way2mars.ij.java.checkfirms.data.FirmListAdapter;

import android.view.Menu;
import android.view.MenuItem;
import com.way2mars.ij.java.checkfirms.model.FirmStorage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final String LOG_TAG = "MainActivity LOG";

    private RecyclerView recyclerView;

    Toast mainToast=null;
    ArrayList<FirmData> mainArray=null;
    FirmListAdapter mainAdapter=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<FirmData> firms = QueryUtils.createFakeList();
//        ListView listView = findViewById(R.id.firms_list);
//
//        final FirmListAdapter adapter = new FirmListAdapter(this, firms);
//        this.mainArray = firms;
//        this.mainAdapter = adapter;
//
//        listView.setAdapter(adapter);
//
//        View viewHeader = getLayoutInflater().inflate(R.layout.firms_list_header, null);
//        listView.addHeaderView(viewHeader);
//        listView.addFooterView(viewHeader);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                showToast(view.getContext(), "itemClick: position = " + position + ", id = "
//                        + id, Toast.LENGTH_SHORT);
//                Log.d(LOG_TAG, "itemClick: position = " + position + ", id = "
//                        + id + view.getTag());
//                if( id == -1) addItem();
//            }
//        });
//
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                Log.d(LOG_TAG, "scrollState = " + scrollState);
//            }
//
//            public void onScroll(AbsListView view, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//                Log.d(LOG_TAG, "scroll: firstVisibleItem = " + firstVisibleItem
//                        + ", visibleItemCount" + visibleItemCount
//                        + ", totalItemCount" + totalItemCount);
//            }
//        });


//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
         //   setContentView(R.layout.activity_main);

//        setSupportActionBar(binding.toolbar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.main_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        FloatingActionButton fab = findViewById(R.id.fab);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        fab.setOnClickListener(view -> {
            AddFirmActivity.start(this, null);
                Log.d("main", "fab");
        });

       // MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getFirmStorageLiveData().observe(this, new Observer<List<FirmStorage>>() {
            @Override
            public void onChanged(List<FirmStorage> firmStorages) {
                adapter.setItem(firmStorages);
            }
        });
    }

    private void showToast(Context context, CharSequence text, int duration){
        if (this.mainToast != null) this.mainToast.cancel();
        this.mainToast = Toast.makeText(context, text, duration);
        this.mainToast.show();
    }

    public void addItem(){
//        String name = "ООО " + this.faker.funnyName().name();
//        this.mainArray.add(0, new FirmData(name, "new inn", "2022-09-15" , "new text"));
//        this.mainAdapter.notifyDataSetChanged();

        Intent intent = new Intent(MainActivity.this, AddFirmActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}