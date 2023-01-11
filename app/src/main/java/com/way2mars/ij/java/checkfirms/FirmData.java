package com.way2mars.ij.java.checkfirms;

import org.jetbrains.annotations.NotNull;

public class FirmData {
    private String mShortName;
    private String mInn;
    private String mDateLastChange;
    private String mTextLastChange;
    private Boolean isLuqiudated;
    private String mDateOfLiquidaton;
    private String mReasonOfLiquidaton;

    public FirmData() {
        mShortName = "";
        mInn = "";
        mDateLastChange = "";
        mTextLastChange = "";
        isLuqiudated = false;
        mDateOfLiquidaton = "";
        mReasonOfLiquidaton = "";
    }

    public FirmData(String name, String inn, String date, String text) {
        mShortName = name;
        mInn = inn;
        mDateLastChange = date;
        mTextLastChange = text;
        isLuqiudated = false;
        mDateOfLiquidaton = "";
        mReasonOfLiquidaton = "";
    }

    public FirmData(String name, String inn, String date, String text, String dateLiq, String textLiq) {
        mShortName = name;
        mInn = inn;
        mDateLastChange = date;
        mTextLastChange = text;
        isLuqiudated = true;
        mDateOfLiquidaton = dateLiq;
        mReasonOfLiquidaton = textLiq;
    }

    public String getShortName() {
        return mShortName;
    }

    public String getInn() {
        return mInn;
    }

    public String getDateLastChange() {
        return mDateLastChange;
    }

    public String getTextLastChange() {
        return mTextLastChange;
    }

    public Boolean isLuqiudated() {
        return isLuqiudated;
    }

    public String getDateOfLiquidaton() {
        return mDateOfLiquidaton;
    }

    public String getReasonOfLiquidaton() {
        return mReasonOfLiquidaton;
    }

    @NotNull
    public String toString() {
        return "Имя: " + mShortName + "\n" +
                "ИНН: " + mInn + "\n" +
                "ДатаПослИзм: " + mDateLastChange + "\n" +
                "ТекстПослИзм: " + mTextLastChange + "\n" +
                "Ликв?: " + isLuqiudated.toString() + "\n" +
                "ДатаЛикв: " + mDateOfLiquidaton + "\n" +
                "ПричЛикв: " + mReasonOfLiquidaton;
    }
}
