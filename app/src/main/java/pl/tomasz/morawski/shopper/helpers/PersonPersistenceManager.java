package pl.tomasz.morawski.shopper.helpers;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomek on 01.05.17.
 */

public class PersonPersistenceManager {
    public static final String TEMP_PEOPLE_CSV = "tempPeople.csv";
    private Context ctx;

    public PersonPersistenceManager(Context ctx) {

        this.ctx = ctx;
    }

    public void saveToTemporaryStorage(PersonInformation person) throws IOException {
        FileOutputStream outputStream = ctx.openFileOutput(TEMP_PEOPLE_CSV, Context.MODE_APPEND);
        String stringToWrite = createStringToWrite(person);
        outputStream.write(stringToWrite.getBytes());
        outputStream.close();
    }

    private String createStringToWrite(PersonInformation person) {
        StringBuilder sb = new StringBuilder();
        sb.append(person.getName()).append("\n");
        return sb.toString();
    }

    public List<PersonInformation> loadFromTemporaryStorage() throws IOException {
        FileInputStream fileInputStream = ctx.openFileInput(TEMP_PEOPLE_CSV);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));

        List<PersonInformation> personList = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            personList.add(new PersonInformation(line));
        }
        return personList;
    }


    public void clearTemporaryStorage() throws IOException {
        FileOutputStream fileOutputStream = ctx.openFileOutput(TEMP_PEOPLE_CSV, Context.MODE_PRIVATE);
        fileOutputStream.write("".getBytes());
        fileOutputStream.close();
    }
}
