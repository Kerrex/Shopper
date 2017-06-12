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

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.IOException;
import java.util.List;

import pl.tomasz.morawski.shopper.helpers.ProductApiManager;
import pl.tomasz.morawski.shopper.helpers.ProductInformation;
import pl.tomasz.morawski.shopper.helpers.ProductPersistenceManager;
import pl.tomasz.morawski.shopper.helpers.ShopInformation;

public class AddToCart extends AppCompatActivity {

    private ProductPersistenceManager persistenceManager = new ProductPersistenceManager(this);
    private BarcodeCallback callback = new BarcodeCallback() {
        private String alreadyScannedEan = "";

        @Override
        public void barcodeResult(BarcodeResult result) {
            final String eanCode = result.getText();
            final EditText quantityText = (EditText) findViewById(R.id.quantity);
            final ShopInformation info = getIntent().getParcelableExtra("ShopInformation");

            if (eanCode != null && !eanCode.equals(alreadyScannedEan)) {
                alreadyScannedEan = eanCode;

                Snackbar.make(getWindow().getDecorView().getRootView(),
                        "Dodano!", Snackbar.LENGTH_LONG)
                        .show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        addProductToCart(eanCode, info.getId(),
                                         Integer.valueOf(quantityText.getText().toString()));
                    }
                }).start();
            }

            return;
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    private void addProductToCart(String eanCode, Integer shopId, Integer quantity) {
        ProductInformation product =
                ProductApiManager.getProductInformation(
                        eanCode,
                        shopId);
        if (product == null) {
            showAddNewProductDialog(eanCode);
        } else {
            product.setQuantity(quantity);
            try {
                persistenceManager.saveToTemporaryStorage(product);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private DecoratedBarcodeView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        makeInputFieldZero();
        bindManualInputButton();
        bindPlusMinusButtons();
        showBarcodeScannner();
    }


    private void showBarcodeScannner() {
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(callback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
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
                inputField.setText(Integer.valueOf(currentValue - 1).toString());
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer currentValue = Integer.valueOf(inputField.getText().toString());
                inputField.setText(Integer.valueOf(currentValue + 1).toString());
            }
        });

    }

    private void showAddNewProductDialog(final String ean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final EditText editText = new EditText(AddToCart.this);
                final ShopInformation info = getIntent().getParcelableExtra("ShopInformation");
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddToCart.this);
                final EditText quantityText = (EditText) findViewById(R.id.quantity);
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setTitle("Nie znaleziono! Podaj nazwę produktu");
                builder.setView(editText);
                builder.setPositiveButton("Dalej", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String name = editText.getText().toString();
                        builder.setTitle("Podaj cenę");
                        final EditText priceEditText = new EditText(AddToCart.this);
                        priceEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(priceEditText);
                        builder.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String price = priceEditText.getText().toString();
                                Double priceInDouble = 0.0;
                                try {
                                    priceInDouble = Double.parseDouble(price);
                                    Integer quantity = Integer.valueOf(quantityText.getText().toString());
                                    final ProductInformation productInformation =
                                            new ProductInformation(null, name, ean, priceInDouble, quantity);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Looper.prepare();
                                            ProductApiManager.saveProductInformation(productInformation, info.getId());
                                            Integer quantity = Integer.valueOf(quantityText.getText().toString());
                                            addProductToCart(ean, info.getId(), quantity);
                                        }
                                    }).start();
                                    finish();
                                    startActivity(getIntent());
                                } catch (NumberFormatException ex) {
                                    builder.setTitle("Nieprawidłowa cena!");
                                    builder.show();
                                }

                            }
                        });
                        builder.show();
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

    private void bindManualInputButton() {
        Button manualInputButton = (Button) findViewById(R.id.button_manual_ean);
        manualInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddToCart.this);
                final EditText quantityText = (EditText) findViewById(R.id.quantity);
                final EditText editText = new EditText(AddToCart.this);
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
