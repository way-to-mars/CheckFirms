package com.way2mars.ij.java.checkfirms.data;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.way2mars.ij.java.checkfirms.model.FirmData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;


public final class FirmDataJson {
    private static final String LOG_TAG = FirmDataJson.class.getSimpleName();

    private static class EgrulRecord{
        String mDate;
        String mText;

        EgrulRecord(String date, String text){
            mDate = date;
            mText = text;
        }
    }

    private static String getAttribute(@NonNull JSONObject json, String nameAttr, String defaultString){
        try {
            if(json.has("@attributes")){
                JSONObject obj1 = json.getJSONObject("@attributes");
                if(obj1.has(nameAttr)) {
                    return obj1.getString(nameAttr);
                }
            }
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON in {getAttribute} " + nameAttr);
        }
        return defaultString;
    }


    public static FirmData work_with_json(@Nullable String input_string){
        if(input_string == null) return null;
        if(input_string.length()==0) return null;

        FirmData firmData = new FirmData();

        // Если есть ключ с таким именем, значит фирма ликвидирована
        Boolean isLiquidated = input_string.contains("СвПрекрЮЛ");
        // Если есть ключ с таким именем, значит есть недостоверность адреса
        Boolean hasAddressWarning = input_string.contains("СвНедАдресЮЛ");

        try {
            JSONObject baseJsonResponse = new JSONObject(input_string);

            // Если вдруг нет такого раздела, возвращаем null
            if(!baseJsonResponse.has("СвЮЛ")) return null;

            JSONObject json_SvUL = baseJsonResponse.getJSONObject("СвЮЛ");

            // List<EgrulRecord> egrulRecords = getAllEgrulRecords(json_SvUL);
            applyLastRecord(firmData, getAllEgrulRecords(json_SvUL));
        }
        catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return firmData;
    }

    private static List<EgrulRecord> getAllEgrulRecords(JSONObject jsonSvUL){
        List<EgrulRecord> results = new ArrayList<>();

        try {
            if (!jsonSvUL.has("СвЗапЕГРЮЛ")) return results;

            JSONArray records = jsonSvUL.getJSONArray("СвЗапЕГРЮЛ");

            for (int i = 0; i < records.length(); i++) {
                JSONObject currentRecord = records.getJSONObject(i);
                JSONObject obj_1 = currentRecord.getJSONObject("@attributes");
                JSONObject jsonVidZap = currentRecord.getJSONObject("ВидЗап");
                JSONObject obj_2 = jsonVidZap.getJSONObject("@attributes");
                String strDate = obj_1.getString("ДатаЗап");
                String strText = obj_2.getString("НаимВидЗап");

                EgrulRecord newRecord = new EgrulRecord(strDate, strText);
                results.add(newRecord);
            }
        }
        catch (JSONException e) {
            System.out.println("Problem parsing the JSON in {getAllEgrulRecords}");
        }
        return results;
    }

    private static void applyLastRecord(FirmData firmData, List<EgrulRecord> listRecords){
        if(listRecords != null && !listRecords.isEmpty()) {
            EgrulRecord lastRecord = listRecords.get(listRecords.size()-1);
            firmData.setKeyValue(FirmData.DATE_LAST_RECORD, lastRecord.mDate);
            firmData.setKeyValue(FirmData.TEXT_LAST_RECORD, lastRecord.mText);
        }
    }




}
