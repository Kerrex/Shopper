package pl.tomasz.morawski.shopper.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.tomasz.morawski.shopper.helpers.JsonMappers.ResultMapper;
import pl.tomasz.morawski.shopper.helpers.JsonMappers.RootMapper;

/**
 * Created by tomek on 23.04.17.
 */

public class ShopFinder {

    private static final String GOOGLE_API_KEY = Constants.GOOGLE_API_KEY;
    private static final String GOOGLE_PLACES_API_URL =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String LOCATION_PARAM = "location";
    private static final String RADIUS_PARAM = "radius";
    private static final String MARKET_NAME_PARAM = "keyword";
    private static final String API_KEY_PARAM = "key";
    private static final Integer RADIUS_IN_METERS = 50;
    private List<String> shopNameList;
    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public ShopFinder(List<String> shopNameList) {
        this.shopNameList = shopNameList;
    }

    public ShopInformation findShop(double latitude, double longitude) {
        for (String shopName : shopNameList) {
            String url = buildJsonUrl(shopName.toLowerCase(), latitude, longitude);
            try {
                URLConnection urlConnection = new URL(url).openConnection();
                String jsonInString = getJsonInString(urlConnection.getInputStream());
                RootMapper json = gson.fromJson(jsonInString, RootMapper.class);
                if (json.getStatus().equals("OK")) {
                    String name = json.getResults().get(0).getName();
                    String address = json.getResults().get(0).getVicinity();
                    Integer id = Constants.valueOf(shopName).getIndex();
                    return new ShopInformation(name, address, id);
                }

            } catch (IOException ignored) {}

        }
        return null;
    }

    private String getJsonInString(InputStream inputStream) {
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

    private String buildJsonUrl(String shopName, double latitude, double longitude) {
        return GOOGLE_PLACES_API_URL +
                LOCATION_PARAM + "=" + latitude + "," + longitude + "&" +
                RADIUS_PARAM + "=" + RADIUS_IN_METERS + "&" +
                MARKET_NAME_PARAM + "=" + shopName + "&" +
                API_KEY_PARAM + "=" + GOOGLE_API_KEY;
    }

}
