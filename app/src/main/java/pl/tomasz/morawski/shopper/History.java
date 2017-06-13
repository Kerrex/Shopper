package pl.tomasz.morawski.shopper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import pl.tomasz.morawski.shopper.helpers.PersonWithProducts;
import pl.tomasz.morawski.shopper.helpers.ProductInformation;
import pl.tomasz.morawski.shopper.helpers.ProductPersistenceManager;

public class History extends AppCompatActivity {

    private ProductPersistenceManager persistenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        persistenceManager = new ProductPersistenceManager(this);
        populateHistory();
    }

    private void populateHistory() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.history_main_layout);
        layout.removeAllViews();
        List<String> dates = loadAvailableHistoryEntries();
        for (final String date : dates) {
            TableRow row = new TableRow(this);
            row.setPadding(0, 24, 0, 24);
            addTextViewToRow(row, date);
            layout.addView(row);
        }
    }

    private void addTextViewToRow(TableRow row, final String date) {
        TextView text = new TextView(this);
        text.setText(date);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    List<PersonWithProducts> people = persistenceManager.getHistory(date);
                    Intent intent = new Intent(getBaseContext(), Result.class);
                    intent.putExtra("PeopleWithProducts", people.toArray(new PersonWithProducts[0]));
                    intent.putExtra("isHistory", true);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        row.addView(text);
    }

    private List<String> loadAvailableHistoryEntries() {
        try {
            return persistenceManager.getHistoryEntries();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
