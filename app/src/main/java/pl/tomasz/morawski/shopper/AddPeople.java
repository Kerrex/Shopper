package pl.tomasz.morawski.shopper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import pl.tomasz.morawski.shopper.helpers.PersonInformation;
import pl.tomasz.morawski.shopper.helpers.PersonPersistenceManager;

public class AddPeople extends AppCompatActivity {

    private TextView mTextMessage;

    public List<PersonInformation> personList = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.add_new_person:
                    showAddPersonDialog();
                    return true;
                case R.id.next:
                    goToNextStage();
                    return true;
            }
            return false;
        }
    };

    private void goToNextStage() {
    }

    private void showAddPersonDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText text = new EditText(this);
        final PersonPersistenceManager persistenceManager =
                new PersonPersistenceManager(this);
        builder.setTitle("Podaj imię osoby");
        builder.setView(text);

        builder.setPositiveButton("Akceptuj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String textFromInput = text.getText().toString();
                PersonInformation person = new PersonInformation(textFromInput);
                addPersonToList(person);
                savePersonToTemporaryStorage(persistenceManager, person);

            }
        });

        builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }

    private void savePersonToTemporaryStorage(PersonPersistenceManager persistenceManager, PersonInformation person) {
        try {
            persistenceManager.saveToTemporaryStorage(person);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_people);
        //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        final PersonPersistenceManager persistenceManager = new PersonPersistenceManager(this);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.people_main_layout);
        layout.removeAllViews();

        try {
            List<PersonInformation> people = persistenceManager.loadFromTemporaryStorage();
            for (PersonInformation p : people) {
                addPersonToList(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void addPersonToList(PersonInformation person) {
        final LinearLayout layout = (LinearLayout) findViewById(R.id.people_main_layout);


        TableRow row = new TableRow(this);
        row.setPadding(0, 24, 0, 24);
        TextView personName = new TextView(this);
        personList.add(person);

        personName.setText(person.getName());
        row.addView(personName);
        layout.addView(row);
    }
}
