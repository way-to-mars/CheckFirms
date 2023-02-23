package com.way2mars.ij.java.checkfirms.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FirmStorage implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "INN")
    public String inn;

    @ColumnInfo(name = "SHORT_NAME")
    public String shortName;

    @ColumnInfo(name = "DATE_LAST_RECORD")
    public String dateLastRecord;

    @ColumnInfo(name = "TEXT_LAST_RECORD")
    public String textLastRecord;

    @ColumnInfo(name = "DATE_LIQUIDATION")
    public String dateLiquidation;

    @ColumnInfo(name = "REASON_LIQUIDATION")
    public String textLiquidation;

    @ColumnInfo(name = "BOOLEAN_ADDRESS_WARNING")
    public Boolean addressWarning;

    //TODO
    // last arbitral process
    @ColumnInfo(name = "DATE_LAST_COURT_ACTION")
    public String dateLastCourtAction;
    @ColumnInfo(name = "NUMBER_LAST_COURT_ACTION")
    public String numberLastCourtAction;


    public FirmStorage(String inn, String shortName, String dateLastRecord, String textLastRecord,
                       String dateLiquidation, String textLiquidation, Boolean addressWarning){
        this.setInn(inn);
        this.setShortName(shortName);
        this.setDateLastRecord(dateLastRecord);
        this.setTextLastRecord(textLastRecord);
        this.setDateLiquidation(dateLiquidation);
        this.setTextLiquidation(textLiquidation);
        this.setAddressWarning(addressWarning);

        this.setDateLastCourtAction("1993-08-01");
        this.setNumberLastCourtAction("А77-12345/2100");
    }

    protected FirmStorage(Parcel in) {
        uid = in.readInt();
        inn = in.readString();
        shortName = in.readString();
        dateLastRecord = in.readString();
        textLastRecord = in.readString();
        dateLiquidation = in.readString();
        textLiquidation = in.readString();
        addressWarning = in.readInt()==1;  // cast Integer to Boolean
        dateLastCourtAction = in.readString();
        numberLastCourtAction = in.readString();
    }

    public static final Creator<FirmStorage> CREATOR = new Creator<FirmStorage>() {
        @Override
        public FirmStorage createFromParcel(Parcel in) {
            return new FirmStorage(in);
        }

        @Override
        public FirmStorage[] newArray(int size) {
            return new FirmStorage[size];
        }
    };

    public String getInn() {
        return inn;
    }

    public void setInn(@Nullable String inn) {
        this.inn = (inn==null) ? "" : inn;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(@Nullable String shortName) {
        this.shortName = (shortName==null) ? "" : shortName;
    }

    public String getDateLastRecord() {
        return dateLastRecord;
    }

    public void setDateLastRecord(@Nullable String dateLastRecord) {
        this.dateLastRecord = (dateLastRecord==null) ? "" : dateLastRecord;
    }

    public String getTextLastRecord() {
        return textLastRecord;
    }

    public void setTextLastRecord(@Nullable String textLastRecord) {
        this.textLastRecord = (textLastRecord==null) ? "" : textLastRecord;
    }

    public String getDateLiquidation() {
        return dateLiquidation;
    }

    public void setDateLiquidation(@Nullable String dateLiquidation) {
        this.dateLiquidation = (dateLiquidation==null) ? "" : dateLiquidation;
    }

    public String getTextLiquidation() {
        return textLiquidation;
    }

    public void setTextLiquidation(@Nullable String textLiquidation) {
        this.textLiquidation = (textLiquidation==null) ? "" : textLiquidation;
    }

    public void setAddressWarning(Boolean key){this.addressWarning=key;}

    public Boolean getAddressWarning(){ return addressWarning; }

    public String getDateLastCourtAction() {
        return dateLastCourtAction;
    }

    public void setDateLastCourtAction(@Nullable String dateLastCourtAction) {
        this.dateLastCourtAction = (dateLastCourtAction==null) ? "" : dateLastCourtAction;
    }

    public String getNumberLastCourtAction() {
        return numberLastCourtAction;
    }

    public void setNumberLastCourtAction(@Nullable String numberLastCourtAction) {
        this.numberLastCourtAction = (numberLastCourtAction==null) ? "" : numberLastCourtAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FirmStorage that = (FirmStorage) o;

        return inn.equals(that.inn);
    }

    @Override
    public int hashCode() {
        return inn.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(uid);
        parcel.writeString(inn);
        parcel.writeString(shortName);
        parcel.writeString(dateLastRecord);
        parcel.writeString(textLastRecord);
        parcel.writeString(dateLiquidation);
        parcel.writeString(textLiquidation);
        parcel.writeInt(addressWarning ? 1 : 0); // cast Boolean to Integer
        parcel.writeString(dateLastCourtAction);
        parcel.writeString(numberLastCourtAction);
    }
}