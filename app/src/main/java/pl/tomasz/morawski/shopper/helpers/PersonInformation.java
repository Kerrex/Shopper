package pl.tomasz.morawski.shopper.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomek on 01.05.17.
 */

public class PersonInformation implements Serializable {
    private String name;
    private List<ProductInformation> productInformationList;

    public PersonInformation(String name) {
        this.name = name;
        productInformationList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
