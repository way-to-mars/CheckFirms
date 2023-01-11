package com.way2mars.ij.java.checkfirms;

import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// LocalDate is only able since Android 8.0 "Oreo"
@RequiresApi(api = Build.VERSION_CODES.O)
public class FirmData {
    private final String mShortName;
    private final String mInn;
    private final String mTextLastChange;
    @Nullable private final LocalDate mDateLastChange;
    private final Boolean isLuqiudated;
    @Nullable private final LocalDate mDateLiquidation;
    private final String mReasonLiquidaton;

    // The constructor for "alive" firm
    public FirmData(String name, String inn, String date, String text) {
        mShortName = name;
        mInn = inn;
        mTextLastChange = text;
        mDateLastChange = string2date(date);
        isLuqiudated = false;
        mDateLiquidation = null;
        mReasonLiquidaton = "";
    }

    // The constructor for liquidated firm
    public FirmData(String name, String inn, String date, String text, String dateLiq, String textLiq) {
        mShortName = name;
        mInn = inn;
        mTextLastChange = text;
        mDateLastChange = string2date(date);
        isLuqiudated = true;
        mDateLiquidation = string2date(dateLiq);
        mReasonLiquidaton = textLiq;
    }

    public String getShortName() {
        return mShortName;
    }

    public String getInn() {
        return mInn;
    }

    public String getDateLastChange() {
        return date2string(mDateLastChange);
    }

    public String getTextLastChange() {
        return mTextLastChange;
    }

    public Boolean isLuqiudated() {
        return isLuqiudated;
    }

    public String getDateLiquidaton() {
        return date2string(mDateLiquidation);
    }

    public String getReasonLiquidaton() {
        return mReasonLiquidaton;
    }

    @NotNull
    public String toString() {
        return "Имя: " + mShortName + "\n" +
                "ИНН: " + mInn + "\n" +
                "ДатаПослИзм: " + date2string(mDateLastChange) + "\n" +
                "ТекстПослИзм: " + mTextLastChange + "\n" +
                "Ликв?: " + isLuqiudated.toString() + "\n" +
                "ДатаЛикв: " + date2string(mDateLiquidation) + "\n" +
                "ПричЛикв: " + mReasonLiquidaton;
    }

    @Nullable
    private LocalDate string2date(String stringDate){
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(stringDate, fmt);
        }catch (Exception RuntimeException){
            return null;
        }
    }

    @NotNull
    public String date2string(@Nullable LocalDate date){
        if(date == null) return "нет данных";

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(fmt);
    }
}
