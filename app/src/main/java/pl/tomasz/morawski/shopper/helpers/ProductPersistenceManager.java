package pl.tomasz.morawski.shopper.helpers;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tomek on 25.04.17.
 */

public class ProductPersistenceManager {

    public static final String TEMP_CSV = "temp.csv";
    private Context ctx;

    public ProductPersistenceManager(Context ctx) {

        this.ctx = ctx;
    }
    public void saveToTemporaryStorage(ProductInformation product) throws IOException {
        FileOutputStream outputStream = ctx.openFileOutput(TEMP_CSV, Context.MODE_APPEND);
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
        FileInputStream fileInputStream = ctx.openFileInput(TEMP_CSV);
        BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));

        List<ProductInformation> productList = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] lineArr = line.split(";");
            if (lineArr.length != 5) continue;

            ProductInformation info = getProductInfoWithSameId(productList,
                                                               Integer.valueOf(lineArr[0]));
            if (info != null) {
                info.setQuantity(info.getQuantity() + Integer.valueOf(lineArr[4]));
            } else {
                productList.add(new ProductInformation(Integer.valueOf(lineArr[0]), lineArr[1],
                        lineArr[2], Double.valueOf(lineArr[3]), Integer.valueOf(lineArr[4])));
            }

        }

        return productList;
    }

    public void saveToHistoryFolder(List<ProductInformation> products) throws IOException {
        Date now = new Date(System.currentTimeMillis());
        String currentDateInString = new SimpleDateFormat("dd-MM-yyyy-HH-mm").format(now);
        File rootDir = new File(ctx.getFilesDir() + "/history/" + currentDateInString);
        rootDir.mkdirs();
        FileOutputStream outputStream = ctx.openFileOutput("history/" + currentDateInString, Context.MODE_PRIVATE);
        for (ProductInformation product : products) {
            outputStream.write(createStringToWrite(product).getBytes());
        }
        outputStream.close();
    }

    private ProductInformation getProductInfoWithSameId(List<ProductInformation> productInformations,
                                             Integer id) {
        for (ProductInformation info : productInformations) {
            if (info.getId().equals(id)) {
                return info;
            }
        }
        return null;
    }

    public void removeProduct(ProductInformation productToRemove) throws IOException {
        List<ProductInformation> productInformations = loadFromTemporaryStorage();

        FileOutputStream outputStream = ctx.openFileOutput(TEMP_CSV, Context.MODE_PRIVATE);
        for (ProductInformation info : productInformations) {
            if (info.getId().equals(productToRemove.getId())) continue;

            String stringToWrite = createStringToWrite(info);
            outputStream.write(stringToWrite.getBytes());
        }
        outputStream.close();
    }

    public void clearTemporaryStorage() throws IOException {
        FileOutputStream fileOutputStream = ctx.openFileOutput(TEMP_CSV, Context.MODE_PRIVATE);
        fileOutputStream.write("".getBytes());
        fileOutputStream.close();
    }
}
