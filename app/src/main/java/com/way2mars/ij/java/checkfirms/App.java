package com.way2mars.ij.java.checkfirms;

import android.app.Application;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.way2mars.ij.java.checkfirms.data.AppDataBase;
import com.way2mars.ij.java.checkfirms.data.FirmStorageDao;

public class App extends Application {

    private AppDataBase dataBase;
    private FirmStorageDao firmStorageDao;

    private static App instance;

    public static App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        dataBase = Room.databaseBuilder(getApplicationContext(),
                AppDataBase.class, "firm-db-main")
                .allowMainThreadQueries()
                .build();

        firmStorageDao = dataBase.firmStorageDao();
    }

    public AppDataBase getDataBase() {
        return dataBase;
    }

    public FirmStorageDao getFirmStorageDao() {
        return firmStorageDao;
    }
}
