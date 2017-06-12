package pl.tomasz.morawski.shopper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.tomasz.morawski.shopper.helpers.PersonInformation;
import pl.tomasz.morawski.shopper.helpers.PersonPersistenceManager;
import pl.tomasz.morawski.shopper.helpers.PersonWithProducts;
import pl.tomasz.morawski.shopper.helpers.ProductInformation;
import pl.tomasz.morawski.shopper.helpers.ProductPersistenceManager;

public class Sharing extends AppCompatActivity {

    private TextView mTextMessage;
    private PersonPersistenceManager personPersistenceManager = new PersonPersistenceManager(this);
    private ProductPersistenceManager productPersistenceManager = new ProductPersistenceManager(this);
    private List<ProductInformation> productInformations;
    private List<PersonWithProducts> peopleWithProducts = new ArrayList<>();
    private ProductInformation currentProduct;
    private Integer currentProductId = -1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.previous_item:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.next_item:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        try {
            productInformations = productPersistenceManager.loadFromTemporaryStorage();
            goToNextProduct();
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadPeopleButtons();
        prepareAddSubstractEditTextButtons();
    }

    private void prepareAddSubstractEditTextButtons() {
        ImageButton addButton = (ImageButton) findViewById(R.id.addQ);
        ImageButton substractButton = (ImageButton) findViewById(R.id.substractQ);
        final EditText quantityEditText = (EditText) findViewById(R.id.add_to_cart_quantity);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer currentQuantityText = Integer.valueOf(quantityEditText.getText().toString());
                if (currentQuantityText < currentProduct.getQuantity()) {
                    quantityEditText.setText(String.valueOf(currentQuantityText + 1));
                }
            }
        });
        substractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer currentQuantityText = Integer.valueOf(quantityEditText.getText().toString());
                if (currentQuantityText > 0) {
                    quantityEditText.setText(String.valueOf(currentQuantityText - 1));
                }
            }
        });
    }

    private void loadPeopleButtons() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.peopleButtonsLayout);
        layout.removeAllViews();

        try {
            List<PersonInformation> personInformationList =
                    personPersistenceManager.loadFromTemporaryStorage();
            for (PersonInformation person : personInformationList) {
                final PersonWithProducts personWithProducts = new PersonWithProducts(person);
                peopleWithProducts.add(personWithProducts);
                Button button = new Button(this);
                button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 12);
                button.setLayoutParams(params);
                button.setTextColor(Color.WHITE);
                button.setText(person.getName());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currentProduct == null) return;

                        Integer quantity = Integer.valueOf(((EditText) findViewById(R.id.add_to_cart_quantity)).getText().toString());
                        personWithProducts.addProduct(currentProduct, quantity);
                        if (quantity < currentProduct.getQuantity()) {
                            currentProduct.setQuantity(currentProduct.getQuantity() - quantity);
                        } else {
                            goToNextProduct();
                        }
                        if (currentProduct == null) {
                            transitToResult();
                            return;
                        }
                        setQuantityEditText();
                    }
                });
                layout.addView(button);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToNextProduct() {
        currentProductId++;
        if (currentProductId > productInformations.size() - 1) {
            currentProduct = null;
            return;
        }
        ProductInformation newProduct = productInformations.get(currentProductId);
        TextView productNameText = (TextView) findViewById(R.id.productName);
        productNameText.setText(newProduct.getName());
        currentProduct =
                new ProductInformation(newProduct.getId(),
                                       newProduct.getName(),
                                       newProduct.getEan(),
                                       newProduct.getPrice(),
                                       newProduct.getQuantity());
    }

    private void transitToResult() {
        Intent intent = new Intent(getBaseContext(), Result.class);
        intent.putExtra("PeopleWithProducts", peopleWithProducts.toArray(new PersonWithProducts[0]));
        startActivity(intent);
    }

    private void setQuantityEditText() {
        EditText quantityText = (EditText) findViewById(R.id.add_to_cart_quantity);
        quantityText.setText(String.valueOf(currentProduct.getQuantity()));
    }

}
