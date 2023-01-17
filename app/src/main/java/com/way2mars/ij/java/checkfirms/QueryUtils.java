package com.way2mars.ij.java.checkfirms;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static class EgrulRecord{
        String mDate;
        String mText;

        EgrulRecord(String date, String text){
            mDate = date;
            mText = text;
        }
    }

    public static ArrayList<FirmData> createFakeList() {
        ArrayList<FirmData> firmList = new ArrayList<>();

        firmList.add(new FirmData("АО Газпром", "2464010203", "2022-12-24",
                "Бла-бла-бла 1234567890", "2023-01-01", "По решению суда в случае допущенных при его создании грубых нарушений закона, если эти нарушения носят неустранимый характер, либо в случае осуществления деятельности без надлежащего разрешения (лицензии) либо деятельности, запрещенной законом"));
        firmList.add(new FirmData("ПАО Билайн", "2464010203", "2022-11-24", "Бла-бла-бла 1234567890"));
        firmList.add(new FirmData("ЗАО АлиЭкспресс", "2464010203", "2022-10-24", "Бла-бла-бла 1234567890"));
        firmList.add(new FirmData("АО РЖД", "2464010203", "2022-12-20", "Бла-бла-бла 1234567890"));
        firmList.add(new FirmData("ООО Трава у Дома", "2464010203", "2020-10-20",
                "Бла-бла-бла 1234567890", "2023-01-03", "The grass is green"));
        firmList.add(new FirmData("ООО Мясо Молоко", "2464010203", "2017-01-12", "Бла-бла-бла 1234567890"));
        firmList.add(new FirmData("ООО СевСнабТяжМясНИИ", "2464010203", "2021-05-09", "Бла-бла-бла 1234567890"));
        firmList.add(new FirmData("ООО Наша пряжа", "2464090807", "2021-07-19", "Бла-бла-бла какой-то иной текст для создания разнообразного разнообразия разнообразных вариантов написания бессмысленного текста"));
        firmList.add(new FirmData("УК ЖКС Мотылек", "2464112233", "2017-01-12", "Бла-бла-бла 1234567890"));
        firmList.add(new FirmData("ООО ДальТранс", "2463123321", "2021-05-09", "Бла-бла-бла 1234567890"));

        firmList.sort(new FirmDataComparator());

        return firmList;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            // Charset.forName("UTF-8")
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    @Nullable
    public static FirmData fetchFirmData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);
        if(url == null) return null;

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        FirmData firm = work_with_json(jsonResponse);
        if(firm!=null) Log.d(LOG_TAG,firm.toString());
        return firm;
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
            System.out.println("Problem parsing the JSON in {getAttribute} " + nameAttr);
        }
        return defaultString;
    }


    public static FirmData work_with_json(@Nullable String input_string){
        if(input_string == null) return null;
        if(input_string.length()==0) return null;

        FirmData firmData = new FirmData();

        // Если есть ключ с таким именем, значит фирма ликвидирована
        firmData.setLiquidationStatus( input_string.contains("СвПрекрЮЛ") );

        // TODO сведения о недостоверности адреса
        // input_string.contains("СвНедАдресЮЛ");

        try {
            JSONObject baseJsonResponse = new JSONObject(input_string);

            // if "error" exists than return null
            if(baseJsonResponse.has("error")) return null;

            // Если вдруг нет такого раздела, возвращаем неполные данные
            if(!baseJsonResponse.has("СвЮЛ"))
            {
                firmData.setShortName("Нет данных. JSON error.");
                return firmData;
            }
            JSONObject json_SvUL = baseJsonResponse.getJSONObject("СвЮЛ");

            if(firmData.isLuqiudated()) getLiquiData(firmData, json_SvUL);
            getInnData(firmData, json_SvUL);
            getShortName(firmData, json_SvUL);

            // List<EgrulRecord> egrulRecords = getAllEgrulRecords(json_SvUL);
            applyLastRecord(firmData, getAllEgrulRecords(json_SvUL));
        }
        catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return firmData;
    }

    private static void getLiquiData(FirmData firmData, JSONObject jsonSvUL){
        try {
            JSONObject jsonSvPrekrUL = jsonSvUL.getJSONObject("СвПрекрЮЛ");
            JSONObject obj1 = jsonSvPrekrUL.getJSONObject("@attributes");
            firmData.setDateLiquidation(obj1.getString("ДатаПрекрЮЛ"));

            JSONObject obj2 = jsonSvPrekrUL.getJSONObject("СпПрекрЮЛ").getJSONObject("@attributes");
            firmData.setReasonLiquidaton(obj2.getString("НаимСпПрекрЮЛ"));
        }
        catch (JSONException e) {
            System.out.println("Problem parsing the JSON in {getLiquiData}");
        }
    }

    private static void getInnData(FirmData firmData, JSONObject jsonSvUL){
        firmData.setInn(getAttribute(jsonSvUL,"ИНН", "0"));
    }

    @Nullable
    private static void getShortName(FirmData firmData, JSONObject jsonSvUL){
        String result = null;
        try {
            if(jsonSvUL.has("СвНаимЮЛ")){
                JSONObject jsonSvNaimUL = jsonSvUL.getJSONObject("СвНаимЮЛ");
                if(jsonSvNaimUL.has("СвНаимЮЛСокр")) {
                    JSONObject jsonSvNaimULSokr = jsonSvNaimUL.getJSONObject("СвНаимЮЛСокр");
                    result = getAttribute(jsonSvNaimULSokr, "НаимСокр",
                            "Нет сокращённого наименования");
                }
                firmData.setKeyValue("longName",
                        getAttribute(jsonSvNaimUL,"НаимЮЛПолн", result));
            }
        }
        catch (JSONException e) {
            System.out.println("Problem parsing the JSON in {getShortName}");
        }
        firmData.setShortName(result);
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
            firmData.setDateLastChange(lastRecord.mDate);
            firmData.setTextLastChange(lastRecord.mText);
        }
        else{
            firmData.setDateLastChange("2000-01-01");
            firmData.setTextLastChange("Записи не найдены");
        }
    }
}
