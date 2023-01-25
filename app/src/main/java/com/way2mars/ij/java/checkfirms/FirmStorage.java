package com.way2mars.ij.java.checkfirms;

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
    public String mInn;

    @ColumnInfo(name = "SHORT_NAME")
    public String mShortName;

    @ColumnInfo(name = "DATE_LAST_RECORD")
    public String mDateLastRecord;

    @ColumnInfo(name = "TEXT_LAST_RECORD")
    public String mTextLastRecord;

    @ColumnInfo(name = "DATE_LIQUIDATION")
    public String mDateLiquidation;

    @ColumnInfo(name = "REASON_LIQUIDATION")
    public String mTextLiquidation;

    //TODO
    // last arbitral process
    @ColumnInfo(name = "DATE_LASTCOURT_ACTION")
    public String mDateLastCourtAction;
    @ColumnInfo(name = "NUMBER_LAST_COURT_ACTION")
    public String mNumberLastCourtAction;


    public FirmStorage(String inn, String shortName, String dateLastRecord, String textLastRecord, String dateLiquidation, String textLiquidation){
        this.setInn(inn);
        this.setShortName(shortName);
        this.setDateLastRecord(dateLastRecord);
        this.setTextLastRecord(textLastRecord);
        this.setDateLiquidation(dateLiquidation);
        this.setTextLiquidation(textLiquidation);

        this.setDateLastCourtAction("03.07.2100");
        this.setNumberLastCourtAction("А77-12345/2100");
    }

    protected FirmStorage(Parcel in) {
        uid = in.readInt();
        mInn = in.readString();
        mShortName = in.readString();
        mDateLastRecord = in.readString();
        mTextLastRecord = in.readString();
        mDateLiquidation = in.readString();
        mTextLiquidation = in.readString();
        mDateLastCourtAction = in.readString();
        mNumberLastCourtAction = in.readString();
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
        return mInn;
    }

    public void setInn(@Nullable String inn) {
        this.mInn = (inn==null) ? "" : inn;
    }

    public String getShortName() {
        return mShortName;
    }

    public void setShortName(@Nullable String shortName) {
        this.mShortName = (shortName==null) ? "" : shortName;
    }

    public String getDateLastRecord() {
        return mDateLastRecord;
    }

    public void setDateLastRecord(@Nullable String dateLastRecord) {
         this.mDateLastRecord = (dateLastRecord==null) ? "" : dateLastRecord;
    }

    public String getTextLastRecord() {
        return mTextLastRecord;
    }

    public void setTextLastRecord(@Nullable String textLastRecord) {
        this.mTextLastRecord = (textLastRecord==null) ? "" : textLastRecord;
    }

    public String getDateLiquidation() {
        return mDateLiquidation;
    }

    public void setDateLiquidation(@Nullable String dateLiquidation) {
        this.mDateLiquidation = (dateLiquidation==null) ? "" : dateLiquidation;
    }

    public String getTextLiquidation() {
        return mTextLiquidation;
    }

    public void setTextLiquidation(@Nullable String textLiquidation) {
        this.mTextLiquidation = (textLiquidation==null) ? "" : textLiquidation;
    }

    public String getDateLastCourtAction() {
        return mDateLastCourtAction;
    }

    public void setDateLastCourtAction(@Nullable String dateLastCourtAction) {
        this.mDateLastCourtAction = (dateLastCourtAction==null) ? "" : dateLastCourtAction;
    }

    public String getNumberLastCourtAction() {
        return mNumberLastCourtAction;
    }

    public void setNumberLastCourtAction(@Nullable String numberLastCourtAction) {
        this.mNumberLastCourtAction = (numberLastCourtAction==null) ? "" : numberLastCourtAction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FirmStorage that = (FirmStorage) o;

        return mInn.equals(that.mInn);
    }

    @Override
    public int hashCode() {
        return mInn.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(uid);
        parcel.writeString(mInn);
        parcel.writeString(mShortName);
        parcel.writeString(mDateLastRecord);
        parcel.writeString(mTextLastRecord);
        parcel.writeString(mDateLiquidation);
        parcel.writeString(mTextLiquidation);
        parcel.writeString(mDateLastCourtAction);
        parcel.writeString(mNumberLastCourtAction);
    }
}
