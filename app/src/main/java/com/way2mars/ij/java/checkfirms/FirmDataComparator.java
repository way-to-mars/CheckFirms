package com.way2mars.ij.java.checkfirms;

import java.util.Comparator;

public class FirmDataComparator implements Comparator<FirmData>{

    @Override
    public int compare(FirmData firmData, FirmData t1) {
        return firmData.compareTo(t1);
    }

}
