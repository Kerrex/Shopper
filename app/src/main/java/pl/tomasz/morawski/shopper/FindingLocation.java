package pl.tomasz.morawski.shopper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Debug;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import pl.tomasz.morawski.shopper.helpers.Constants;
import pl.tomasz.morawski.shopper.helpers.DialogUtil;
import pl.tomasz.morawski.shopper.helpers.GPSTracker;
import pl.tomasz.morawski.shopper.helpers.ShopFinder;
import pl.tomasz.morawski.shopper.helpers.ShopInformation;

public class FindingLocation extends AppCompatActivity {

    public static final String SHOP_INDEX = "shopIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_location);
        bindShowNotificaitonOnClick();


    }

    @Override
    protected void onResume() {
        super.onResume();
        findShop();
    }

    private void findShop() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Looper.prepare();
                        List<String> shopNames = new ArrayList<>();
                        for (Constants shop : Constants.values()) {
                            shopNames.add(shop.name());
                        }

                        GPSTracker tracker = new GPSTracker(FindingLocation.this);
                        double lat = tracker.getLatitude();
                        double lng = tracker.getLongitude();
                        ShopFinder shopFinder = new ShopFinder(shopNames);
                        ShopInformation info = shopFinder.findShop(lat, lng);

                        if (info == null) {
                            //spawnChooseShopDialog();
                            return;
                        }

                        Intent intent = new Intent(getBaseContext(), DoShopping.class);
                        intent.putExtra("ShopInformation", info);
                        startActivity(intent);
                    } catch (Exception ex) {

                    }
                }
            }).start();
        } catch (Exception ex) {
            spawnChooseShopDialog();
        }
    }

    private void bindShowNotificaitonOnClick() {
        Button chooseShopButton = (Button) findViewById(R.id.button);
        assert chooseShopButton != null;
        chooseShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spawnChooseShopDialog();
            }
        });
    }

    private void spawnChooseShopDialog() {
        DialogUtil.spawnChooseShopDialog(this);
    }
}
