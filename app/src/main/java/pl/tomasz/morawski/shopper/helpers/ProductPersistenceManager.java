package pl.tomasz.morawski.shopper.helpers;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomek on 25.04.17.
 */

public class ProductPersistenceManager {

    private Context ctx;

    public ProductPersistenceManager(Context ctx) {

        this.ctx = ctx;
    }
    public void saveToTemporaryStorage(ProductInformation product) throws IOException {
        FileOutputStream outputStream = ctx.openFileOutput("temp.csv", Context.MODE_APPEND);
        String stringToWrite = createStringToWrite(product);
        outputStream.write(stringToWrite.getBytes());
        outputStream.close();
    }

    private String createStringToWrite(ProductInformation product) {
        StringBuilder sb = new StringBuilder();
        sb.append(product.getId()).append(";");
        sb.append(product.getName()).append(";");
        sb.append(product.getEan()).append(";");
        sb.append(product.getPrice()).append(";");
        sb.append(product.getQuantity()).append("\n");
        return sb.toString();
    }

    public List<ProductInformation> loadFromTemporaryStorage() throws IOException {
        FileInputStream fileInputStream = ctx.openFileInput("temp.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));

        List<ProductInformation> productList = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] lineArr = line.split(";");
            if (lineArr.length != 5) continue;
            productList.add(new ProductInformation(Integer.valueOf(lineArr[0]), lineArr[1],
                    lineArr[2], Double.valueOf(lineArr[3]), Integer.valueOf(lineArr[4])));
        }

        return productList;
    }
}
