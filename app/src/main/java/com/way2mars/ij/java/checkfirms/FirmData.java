package com.way2mars.ij.java.checkfirms;

import android.util.Log;
import androidx.annotation.Nullable;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class FirmData implements Comparable<FirmData> {
    public final static String DATE_EGRUL = "FD_DATE_EGRUL";
    public final static String SHORT_NAME = "FD_SHORT_NAME";
    public final static String FULL_NAME = "FD_FULL_NAME";
    public final static String INN = "FD_INN";
    public final static String KPP = "FD_KPP";
    public final static String OGRN = "FD_OGRN";
    public final static String DATE_OGRN = "FD_DATE_OGRN";
    public final static String ADDRESS = "FD_ADDRESS";
    public final static String CHIEF_POSITION = "FD_CHIEF_POSITION";
    public final static String CHIEF_FULLNAME = "FD_CHIEF_FULLNAME";
    public final static String TEXT_LAST_RECORD = "FD_TEXT_LAST_CHANGE";
    public final static String DATE_LAST_RECORD = "FD_DATE_LAST_CHANGE";
    public final static String REASON_LIQUIDATION = "FD_REASON_LIQUIDATION";
    public final static String DATE_LIQUIDATION = "FD_DATE_LIQUIDATION";
    private final static String BOOL_ADDRESS_WARNING = "FD_BOOLEAN_ADDRESS_WARNING";

    private final String LOG_TAG = "FirmData.class";

    private Map<String, String> mapValues;

    // The null-constructor for a firm
    public FirmData() {
        mapValues = new HashMap<>();
    }

    // The constructor for "alive" firm
    public FirmData(String name, String inn, String dateLastChange, String textLastChange) {
        mapValues = new HashMap<>();
        setKeyValue(SHORT_NAME, name);
        setKeyValue(INN, inn);
        setKeyValue(DATE_LAST_RECORD, textLastChange);
        setKeyValue(TEXT_LAST_RECORD, dateLastChange);
    }

    // The constructor for liquidated firm
    public FirmData(String name, String inn, String dateLastChange, String textLastChange, String dateLiq, String textLiq) {
        mapValues = new HashMap<>();
        setKeyValue(SHORT_NAME, name);
        setKeyValue(INN, inn);
        setKeyValue(DATE_LAST_RECORD, textLastChange);
        setKeyValue(TEXT_LAST_RECORD, dateLastChange);
        setKeyValue(DATE_LIQUIDATION, dateLiq);
        setKeyValue(REASON_LIQUIDATION, textLiq);
    }

    // Create from JSON
    public FirmData(@NotNull String jsonLong, @NotNull String jsonShort) {
        mapValues = new HashMap<>();

        /*
            Fetching data from https://egrul.itsoft.ru/short_data/?{inn}.json
         */
        Object docShort = Configuration.defaultConfiguration().jsonProvider().parse(jsonShort);

        setKeyValue(DATE_OGRN, fetchString(docShort, "$.reg_date"));
        setKeyValue(OGRN, fetchString(docShort, "$.ogrn"));
        setKeyValue(INN, fetchString(docShort, "$.inn"));
        setKeyValue(KPP, fetchString(docShort, "$.kpp"));
        setKeyValue(SHORT_NAME, fetchString(docShort, "$.short_name"));
        setKeyValue(FULL_NAME, fetchString(docShort, "$.full_name"));
        setKeyValue(ADDRESS, fetchString(docShort, "$.address"));
        setKeyValue(CHIEF_POSITION, fetchString(docShort, "$.chief_position"));
        setKeyValue(CHIEF_FULLNAME, fetchString(docShort, "$.chief"));

        /*
            Fetching data from https://egrul.itsoft.ru/{inn}.json
         */
        Object docLong = Configuration.defaultConfiguration().jsonProvider().parse(jsonLong);

        setKeyValue(DATE_EGRUL, fetchString(docLong, "$.СвЮЛ.ATTR.ДатаВып"));
        setAddressWarning(jsonLong.contains("СвНедАдресЮЛ"));
        setKeyValue(DATE_LIQUIDATION, fetchString(docLong, "$.СвЮЛ.СвПрекрЮЛ.ATTR.ДатаПрекрЮЛ"));
        setKeyValue(REASON_LIQUIDATION, fetchString(docLong, "$.СвЮЛ.СвПрекрЮЛ.СпПрекрЮЛ.ATTR.НаимСпПрекрЮЛ"));
        setKeyValue(DATE_LAST_RECORD, fetchString(docLong, "$.СвЮЛ.СвЗапЕГРЮЛ[-1].ATTR.ДатаЗап"));
        setKeyValue(TEXT_LAST_RECORD, fetchString(docLong, "$.СвЮЛ.СвЗапЕГРЮЛ[-1].ВидЗап.ATTR.НаимВидЗап"));

    }

    private String fetchString(Object o, String jsonPath) {
        try {
            return JsonPath.read(o, jsonPath);
        } catch (Exception e) {
            Log.d(LOG_TAG, "fetchString :: wrong path " + jsonPath);
        }
        return null;
    }


    /**
     * Basic setter and getter for FirmDate
     * keyName = constant String (SHORT_NAME / FULL_NAME / INN ...)
     * value = Text String
     */
    public String getValue(String keyName) {
        return mapValues.getOrDefault(keyName, "");
    }

    public String getValueDefault(String keyName, String defaultValue) {
        return mapValues.getOrDefault(keyName, defaultValue);
    }

    public Boolean hasAddressWarning() {
        return mapValues.containsKey(BOOL_ADDRESS_WARNING);
    }

    public Boolean isLiquidated() {
        return mapValues.containsKey(DATE_LIQUIDATION);
    }

    public void setKeyValue(String keyName, @Nullable String value) {
        if (keyName != null && value != null)
            if (keyName.length() > 0 && value.length() > 0) {
                mapValues.put(keyName, value);
                Log.d(LOG_TAG, String.format("setKeyValue ::  {%s} : {%s}", keyName, value));
                return;
            }
        Log.d(LOG_TAG, "setKeyValue :: wrong input :: " + String.format("{%s} : {%s}", keyName, value));
    }

    public void setKeyValueOnce(String keyName, String value) {
        if (keyName != null && value != null)
            if (keyName.length() > 0 && value.length() > 0)
                if (!mapValues.containsKey(keyName)) {
                    mapValues.put(keyName, value);
                    return;
                }
        Log.d(LOG_TAG, "setKeyValue ::  wrong input");
    }


    public void setAddressWarning(@NotNull Boolean state) {
        Log.d(LOG_TAG, String.format("setAddressStatus :: недостоверность %b", state));
        if (state) mapValues.put(BOOL_ADDRESS_WARNING, "True");
        else mapValues.remove(BOOL_ADDRESS_WARNING);
    }

    @NotNull
    public String toString() {
        StringBuilder result = new StringBuilder();
        //return mapValues.toString();
        Set<String> setKeys = mapValues.keySet();
        for (String k : setKeys) {
            result.append("" + k + "\t:\t" + mapValues.get(k) + "\n");
        }

        return result.toString();
    }

    @Override
    public int compareTo(@Nullable FirmData right) {
        if (right == null) return 0;

        // Date is descending
        int result = -this.getValueDefault(DATE_EGRUL, "0").
                compareTo(right.getValueDefault(DATE_EGRUL, "0"));

        // Name by alphabet
        if (result == 0) {
            result = this.getValueDefault(SHORT_NAME, "0").
                    compareTo(right.getValueDefault(SHORT_NAME, "0"));
        }
        return result;
    }

}



