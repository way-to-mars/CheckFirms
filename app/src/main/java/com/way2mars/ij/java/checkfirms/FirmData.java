package com.way2mars.ij.java.checkfirms;

import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@RequiresApi(api = Build.VERSION_CODES.O) // LocalDate is only able since Android 8.0 "Oreo"
public class FirmData implements Comparable<FirmData>{
    private String mShortName;
    private String mInn;
    private String mTextLastChange;
    @Nullable private LocalDate mDateLastChange;
    private Boolean isLuqiudated;
    private Boolean isNedostovAddress;
    @Nullable private LocalDate mDateLiquidation;
    private String mReasonLiquidaton;
    private Map<String, String> mapValues;

    // The null-constructor for a firm
    public FirmData() {
        mShortName = null;
        mInn = null;
        mTextLastChange = null;
        mDateLastChange = null;
        isLuqiudated = false;
        mDateLiquidation = null;
        mReasonLiquidaton = null;
        mapValues = new HashMap<>();
    }

    // The constructor for "alive" firm
    public FirmData(String name, String inn, String date, String text) {
        mShortName = name;
        mInn = inn;
        mTextLastChange = text;
        mDateLastChange = string2date(date);
        isLuqiudated = false;
        mDateLiquidation = null;
        mReasonLiquidaton = "";
        mapValues = new HashMap<>();
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
        mapValues = new HashMap<>();
    }

    public String getShortName() {
        return (mShortName == null) ? "нет данных" : mShortName;
    }

    public String getInn() {
        return (mInn == null) ? "нет данных" : mInn;
    }

    public String getDateLastChange() {
        return date2string(mDateLastChange);
    }

    public String getTextLastChange() {
        return (mTextLastChange == null) ? "нет данных" : mTextLastChange;
    }

    public Boolean isLuqiudated() {
        return isLuqiudated;
    }

    public String getDateLiquidaton() {
        return date2string(mDateLiquidation);
    }

    public String getReasonLiquidaton() {
        return (mReasonLiquidaton == null) ? "нет данных" : mReasonLiquidaton;
    }

    public String getKeyValue(String keyName){ return mapValues.getOrDefault(keyName, "Пустая строка {HashMap}"); }

    public void setShortName(String name){ mShortName = name;}

    public void setInn(String inn){ mInn = inn; }

    public void setTextLastChange(String text){ mTextLastChange = text; }

    public void setDateLastChange(String textDate){ mDateLastChange = string2date(textDate); }

    public void setLiquidationStatus(Boolean flag){ isLuqiudated=flag;}

    public void setDateLiquidation(String textDate){ mDateLiquidation = string2date(textDate); }

    public void setReasonLiquidaton(String text){ mReasonLiquidaton = text; }

    public void setKeyValue(String keyName, String value){ if(keyName != null && value != null) if(keyName.length()>0 && value.length()>0) mapValues.put(keyName, value); }

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

    @Override
    public int compareTo(FirmData right) {
        // TODO: maybe null
        // Дата по убыванию
        assert this.mDateLastChange != null;
        int result = - this.mDateLastChange.compareTo(right.mDateLastChange);

        // Название по алфавиту
        if( result == 0)
        {
            result = this.mShortName.compareTo(right.mShortName);
        }
        return result;
    }
}



