package pl.tomasz.morawski.shopper;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import pl.tomasz.morawski.shopper.helpers.ProductInformation;
import pl.tomasz.morawski.shopper.helpers.ProductPersistenceManager;
import pl.tomasz.morawski.shopper.helpers.ShopInformation;

public class DoShopping extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String SHOP_NAME = "shopName";
    private ProductPersistenceManager persistenceManager = new ProductPersistenceManager(this);
    private List<ProductInformation> products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shopping);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShopInformation info = getIntent().getParcelableExtra("ShopInformation");
                Intent intent = new Intent(getBaseContext(), AddToCart.class);
                intent.putExtra("ShopInformation", info);
                startActivity(intent);
            /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();*/
            }
        });

        FloatingActionButton nextFab = (FloatingActionButton) findViewById(R.id.fab_next);
        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddPeople.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onResume() {
        try {
            products = loadProductsFromTemporaryStorage();
            populateProducts(products);
        } catch (Exception ex) {
            new AlertDialog.Builder(this).setMessage(ex.getMessage()).show();
        }
        super.onResume();
    }

    private void populateProducts(List<ProductInformation> products) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.shopping_main_layout);
        layout.removeAllViews();
        for(ProductInformation product : products) {
            TableRow row = new TableRow(this);
            row.setPadding(0, 24, 0, 24);
            TextView productName = new TextView(this);
            productName.setText(product.getName());
            row.addView(productName);
            layout.addView(row);
        }
    }

    private List<ProductInformation> loadProductsFromTemporaryStorage() {
        try {
            return persistenceManager.loadFromTemporaryStorage();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ShopInformation info = getIntent().getParcelableExtra("ShopInformation");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.do_shopping, menu);
        TextView shopNameTextView = (TextView) findViewById(R.id.shop_name);
        shopNameTextView.setText(info.getName());

        TextView shopAddressTextView = (TextView) findViewById(R.id.shop_address);
        shopAddressTextView.setText(info.getAddress());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
