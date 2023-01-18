package com.way2mars.ij.java.checkfirms;

import android.util.Log;

import androidx.annotation.Nullable;

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

public final class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


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

        if( jsonResponse == null) return null;

        return new FirmData(jsonResponse);
    }
}
