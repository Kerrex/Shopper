package pl.tomasz.morawski.shopper.helpers;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomek on 08.05.17.
 */

public class PersonWithProducts implements Parcelable {

    private List<ProductInformation> products;
    private PersonInformation person;

    public PersonWithProducts(PersonInformation person) {
        this.person = person;
        products = new ArrayList<>();
    }

    public void addProduct(ProductInformation productInformation, int quantity) {
        products.add(new ProductInformation(productInformation.getId(),
                productInformation.getName(),
                productInformation.getEan(),
                productInformation.getPrice(),
                quantity));
    }

    public double getTotalMoneyToPay() {
        double sum = 0;
        for (ProductInformation productInformation : products) {
            sum += productInformation.getTotalPrice();
        }
        return sum;
    }

    public String getName() {
        return person.getName();
    }
    public List<ProductInformation> getProducts() {
        return products;
    }

    protected PersonWithProducts(Parcel in) {
        if (in.readByte() == 0x01) {
            products = new ArrayList<ProductInformation>();
            in.readList(products, ProductInformation.class.getClassLoader());
        } else {
            products = null;
        }
        person = (PersonInformation) in.readValue(PersonInformation.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (products == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(products);
        }
        dest.writeValue(person);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PersonWithProducts> CREATOR = new Parcelable.Creator<PersonWithProducts>() {
        @Override
        public PersonWithProducts createFromParcel(Parcel in) {
            return new PersonWithProducts(in);
        }

        @Override
        public PersonWithProducts[] newArray(int size) {
            return new PersonWithProducts[size];
        }
    };
}
