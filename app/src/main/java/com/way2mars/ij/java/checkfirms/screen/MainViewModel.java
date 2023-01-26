package com.way2mars.ij.java.checkfirms.screen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.way2mars.ij.java.checkfirms.App;
import com.way2mars.ij.java.checkfirms.model.FirmStorage;

import java.util.List;

public class MainViewModel extends ViewModel {
    private LiveData<List<FirmStorage>> firmStorageLiveData = App.getInstance().getFirmStorageDao().getAllLiveData();

    public LiveData<List<FirmStorage>> getFirmStorageLiveData() {
        return firmStorageLiveData;
    }
}
