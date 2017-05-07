package pl.tomasz.morawski.shopper.helpers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by tomek on 25.04.17.
 */

public class ProductApiManager {

    public static String URL = "https://kerrex.pl/product-api/";

    public static ProductInformation getProductInformation(String ean, Integer shopId) {
        try {
            HttpsURLConnection urlConnection = (HttpsURLConnection) new URL(URL + ean + "/" + shopId).openConnection();
            String jsonInString = getJsonInString(urlConnection.getInputStream());
            Map<String, String> jsonMap =
                    new Gson().fromJson(jsonInString, new TypeToken<HashMap<String, String>>(){}.getType());
            return new ProductInformation(Integer.valueOf(jsonMap.get("id")),
                                          jsonMap.get("name"),
                                          jsonMap.get("ean"),
                                          Double.valueOf(jsonMap.get("price")), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getJsonInString(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();

    }
}
