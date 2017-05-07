package pl.tomasz.morawski.shopper;

import android.content.DialogInterface;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import pl.tomasz.morawski.shopper.helpers.ProductApiManager;
import pl.tomasz.morawski.shopper.helpers.ProductInformation;
import pl.tomasz.morawski.shopper.helpers.ProductPersistenceManager;
import pl.tomasz.morawski.shopper.helpers.ShopInformation;

public class AddToCart extends AppCompatActivity {

    ProductPersistenceManager persistenceManager = new ProductPersistenceManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        makeInputFieldZero();
        bindManualInputButton();
        bindPlusMinusButtons();
    }

    private void makeInputFieldZero() {
        final EditText inputField = (EditText) findViewById(R.id.quantity);
        inputField.setText(Integer.valueOf(0).toString());
    }

    private void bindPlusMinusButtons() {
        final EditText inputField = (EditText) findViewById(R.id.quantity);
        Button minusButton = (Button) findViewById(R.id.minusButton);
        Button plusButton = (Button) findViewById(R.id.plusButton);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer currentValue = Integer.valueOf(inputField.getText().toString());
                inputField.setText(Integer.valueOf(currentValue-1).toString());
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer currentValue = Integer.valueOf(inputField.getText().toString());
                inputField.setText(Integer.valueOf(currentValue+1).toString());
            }
        });

    }

    private void bindManualInputButton() {
        Button manualInputButton = (Button) findViewById(R.id.button_manual_ean);
        manualInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddToCart.this);
                final EditText editText = new EditText(AddToCart.this);
                final EditText quantityText = (EditText) findViewById(R.id.quantity);
                final ShopInformation info = getIntent().getParcelableExtra("ShopInformation");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setTitle("Wprowadź kod kreskowy");
                builder.setView(editText);
                builder.setPositiveButton("Akceptuj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(getWindow().getDecorView().getRootView(),
                                "Przeczytano, trwa dodawanie do koszyka!", Snackbar.LENGTH_LONG)
                                .show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                ProductInformation product =
                                        ProductApiManager.getProductInformation(
                                                editText.getText().toString(),
                                                info.getId());
                                product.setQuantity(Integer.valueOf(quantityText.getText().toString()));
                                try {
                                    persistenceManager.saveToTemporaryStorage(product);
                                    Snackbar.make(getWindow().getDecorView().getRootView(),
                                            "Dodano!", Snackbar.LENGTH_LONG)
                                            .show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Nothing
                    }
                });
                builder.show();

            }
        });
    }
}
