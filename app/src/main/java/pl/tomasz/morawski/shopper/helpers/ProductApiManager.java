package pl.tomasz.morawski.shopper.helpers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.client.entity.UrlEncodedFormEntityHC4;
import org.apache.http.client.methods.HttpPostHC4;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
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

    public static void saveProductInformation(ProductInformation product, Integer marketId) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(URL + "save").openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            Map<String, String> request = new HashMap<>();
            request.put("name", product.getName());
            request.put("ean", product.getEan());
            request.put("price", product.getPrice().toString());
            request.put("marketId", marketId.toString());
            OutputStream os = connection.getOutputStream();
            String jsonToSend = new Gson().toJson(request);
            os.write(jsonToSend.getBytes());
            os.flush();
            os.close();
            connection.connect();
            int response = connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
