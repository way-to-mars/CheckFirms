package com.way2mars.ij.java.checkfirms;

import android.os.Build;
import android.util.Log;
import androidx.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class FirmData implements Comparable<FirmData> {
    public final String DATE_EGRUL = "FD_DATE_EGRUL";
    public final String SHORT_NAME = "FD_SHORT_NAME";
    public final String LONG_NAME = "FD_LONG_NAME";
    public final String INN = "FD_INN";
    public final String KPP = "FD_KPP";
    public final String OGRN = "FD_OGRN";
    public final String DATE_OGRN = "FD_DATE_OGRN";
    public final String ADDRESS = "FD_ADDRESS";
    public final String TEXT_LAST_CHANGE = "FD_TEXT_LAST_CHANGE";
    public final String DATE_LAST_CHANGE = "FD_DATE_LAST_CHANGE";
    public final String REASON_LIQUIDATION = "FD_REASON_LIQUIDATION";
    public final String DATE_LIQUIDATION = "FD_DATE_LIQUIDATION";
    private final String BOOL_ADDRESS = "FD_BOOLEAN_ADDRESS_WARNING";

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
        setKeyValue(DATE_LAST_CHANGE, textLastChange);
        setKeyValue(TEXT_LAST_CHANGE, dateLastChange);
    }

    // The constructor for liquidated firm
    public FirmData(String name, String inn, String dateLastChange, String textLastChange, String dateLiq, String textLiq) {
        mapValues = new HashMap<>();
        setKeyValue(SHORT_NAME, name);
        setKeyValue(INN, inn);
        setKeyValue(DATE_LAST_CHANGE, textLastChange);
        setKeyValue(TEXT_LAST_CHANGE, dateLastChange);
        setKeyValue(DATE_LIQUIDATION, dateLiq);
        setKeyValue(REASON_LIQUIDATION, textLiq);
    }

    // Create from JSON
    public FirmData(String jsonString){
        mapValues = new HashMap<>();

        StringBuilder matching_key = new StringBuilder();
        int len = jsonString.length();
        int i=1; // point to start (index of String)
        int j=0;

        while (true) {
            /*
                Step 1. Searching for '{"' or ',"' as a start of a key name
            */
            if (i >= len-2) break;
            int start_i = jsonString.indexOf('"', i);
            if( start_i == -1 || start_i >= len-1 ) break; // reach the end of json-string

            char prev = jsonString.charAt(start_i - 1);
            if (prev == '{' || prev == ',')  // search for '{"' or ',"' as a start of a key name
            {
                int end_i = jsonString.indexOf("\":", i+1);
                if( end_i == -1 || end_i >= len-1 ) break; // reach the end of json-string

                /*
                    Step 2. Retrieving key-string
                */
                if( end_i < start_i+2) break; // key-string must be at least 1 character long
                String key_string = jsonString.substring(start_i+1, end_i);
                j++;


                /*
                  Step 3. Check if this key-string is needed for FirmData constructor
                 */
                matching_key.setLength(0);
                switch (key_string){
                    case "ДатаВып": matching_key.append(DATE_EGRUL);
                        break;
                    case "НаимСокр": matching_key.append(SHORT_NAME);
                        break;
                    case "НаимЮЛПолн": matching_key.append(LONG_NAME);
                        break;
                    case "ИНН": matching_key.append(INN);
                        break;
                    case "КПП": matching_key.append(KPP);
                        break;
                    case "ОГРН": matching_key.append(OGRN);
                        break;
                    case "ДатаОГРН": matching_key.append(DATE_OGRN);
                        break;
                    case "ДатаПрекрЮЛ": matching_key.append(DATE_LIQUIDATION);
                        break;
                    case "НаимСпПрекрЮЛ": matching_key.append(REASON_LIQUIDATION);
                        break;
                    case "СвНедАдресЮЛ": this.setKeyValue(BOOL_ADDRESS,"True");
                        Log.d(LOG_TAG, "ADD >> Недостоверность адреса");
                        break;
//                  public final String ADDRESS = "FD_ADDRESS";
                }

                if( matching_key.length() == 0){
                    i = end_i+1;
                    continue;
                }

                System.out.printf("[Total %d] - %d -[%d, %d] %s", len, j, start_i, end_i, key_string);

                /*
                  Step 4. if key-string is what we need then
                           trying to get value-string
                 */

                start_i = end_i+3;
                if (start_i >= len-2) break; // reach the end of json-string

                // two possible endings for value-string: '"}' and '",'
                end_i = minIndex(   jsonString.indexOf("\"}", start_i),
                                    jsonString.indexOf("\",", start_i));
                if( end_i == -1 ) break; // broken json-string

                String value_string = jsonString.substring(start_i, end_i).replaceAll("\\\\", "");
                this.setKeyValue(matching_key.toString(), value_string);
                System.out.printf("\t%s\n", value_string);

                i = end_i+1;
            }
            else {
                i = i+1;
            }
        }

    }

    /**
     * Basic setter and getter for FirmDate
     * keyName = constant String (SHORT_NAME / LONG_NAME / INN ...)
     * value = Text String
     */
    public String getValue(String keyName) {
        return mapValues.getOrDefault(keyName, "");
    }

    public String getValueDefault(String keyName, String defaultValue) {
        return mapValues.getOrDefault(keyName, defaultValue);
    }

    public Boolean hasAddressWarning(){
        return mapValues.containsKey(BOOL_ADDRESS);
    }

    public Boolean isLiquidated(){
        return mapValues.containsKey(DATE_LIQUIDATION);
    }

    public void setKeyValue(String keyName, String value) {
        if (keyName != null && value != null)
            if (keyName.length() > 0 && value.length() > 0) {
                mapValues.put(keyName, value);
                return;
            }
        Log.d(LOG_TAG, "setKeyValue ::  wrong input");
    }

    public void setKeyValueOnce(String keyName, String value) {
        if (keyName != null && value != null)
            if (keyName.length() > 0 && value.length() > 0)
                if( !mapValues.containsKey(keyName) ){
                    mapValues.put(keyName, value);
                    return;
                }
        Log.d(LOG_TAG, "setKeyValue ::  wrong input");
    }


    public void setAddressStatus(@NotNull Boolean state){
        if( state ) mapValues.put(BOOL_ADDRESS, "True");
        else mapValues.remove(BOOL_ADDRESS);
    }

    @NotNull
    public String toString() {
        return mapValues.toString();
    }

    @Nullable
    private LocalDate string2date(String stringDate) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(stringDate, fmt);
        } catch (Exception RuntimeException) {
            return null;
        }
    }

    @NotNull
    public String date2string(@Nullable LocalDate date) {
        if (date == null) return "нет данных";

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(fmt);
    }

    @Override
    public int compareTo(@Nullable FirmData right) {
        if(right == null) return 0;
        return 0;
        // Дата по убыванию
//        assert this.mDateLastChange != null;
//        int result = -this.mDateLastChange.compareTo(right.mDateLastChange);
//
//        // Название по алфавиту
//        if (result == 0) {
//            result = this.mShortName.compareTo(right.mShortName);
//        }
//        return result;
    }

    /**
     * Searching min(a, b), but remember that:
     * @param a can be -1
     * @param b can be -1
     * @return min(a, b) if they are positive or return -1
     */
    private static int minIndex(int a, int b){
        if( a == -1) return Math.max(-1, b);
        if( b == -1) return Math.max(-1, a);
        return Math.min(a, b);
    }
}



