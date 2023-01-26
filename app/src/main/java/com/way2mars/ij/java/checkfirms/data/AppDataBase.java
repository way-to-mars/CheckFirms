package com.way2mars.ij.java.checkfirms.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.way2mars.ij.java.checkfirms.model.FirmStorage;

@Database(entities = {FirmStorage.class}, version =  1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    public abstract FirmStorageDao firmStorageDao();
}
