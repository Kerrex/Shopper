package pl.tomasz.morawski.shopper;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.util.Arrays;

import pl.tomasz.morawski.shopper.helpers.PersonPersistenceManager;
import pl.tomasz.morawski.shopper.helpers.PersonWithProducts;
import pl.tomasz.morawski.shopper.helpers.ProductPersistenceManager;

public class Result extends AppCompatActivity {

    private PersonWithProducts[] peopleWithProducts;
    private ProductPersistenceManager persistenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        persistenceManager = new ProductPersistenceManager(this);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton save = (FloatingActionButton) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    persistenceManager.savePeopleWithProducts(Arrays.asList(peopleWithProducts));
                    Snackbar.make(getWindow().getDecorView().getRootView(),
                            "Zapisano!", Snackbar.LENGTH_LONG)
                            .show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Parcelable[] parcels = getIntent().getParcelableArrayExtra("PeopleWithProducts");
        peopleWithProducts = new PersonWithProducts[parcels.length];
        System.arraycopy(parcels, 0, peopleWithProducts, 0, parcels.length);
        fillResultScreen();
        clearTemporaryStorages();
    }

    private void clearTemporaryStorages() {
        try {
            new PersonPersistenceManager(this).clearTemporaryStorage();
            new ProductPersistenceManager(this).clearTemporaryStorage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillResultScreen() {
        TableLayout resultLayout = (TableLayout) findViewById(R.id.result_table_layout);
        resultLayout.removeAllViews();
        for (PersonWithProducts person : peopleWithProducts) {
            TableRow row = new TableRow(this);

            TextView name = new TextView(this);
            name.setText(person.getName());
            name.setTextSize(16);
            name.setWidth(260);
            row.addView(name);

            TextView toPay = new TextView(this);
            toPay.setText(person.getTotalMoneyToPay() + " zł");
            toPay.setTextSize(16);
            row.addView(toPay);

            resultLayout.addView(row);
        }

    }

}
