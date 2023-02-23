package com.way2mars.ij.java.checkfirms.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.way2mars.ij.java.checkfirms.model.FirmStorage;

import java.util.List;

@Dao
public interface FirmStorageDao {
    @Query("SELECT * FROM FirmStorage")
    List<FirmStorage> getAll();

    @Query("SELECT * FROM FirmStorage")
    LiveData<List<FirmStorage>> getAllLiveData();

    @Query("SELECT * FROM FirmStorage WHERE uid IN (:userIds)")
    List<FirmStorage> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM FirmStorage WHERE inn LIKE :innString LIMIT 1")
    FirmStorage findByInn(String innString);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FirmStorage firm);

    @Update
    void update(FirmStorage firm);

    @Delete
    void delete(FirmStorage firm);
}
