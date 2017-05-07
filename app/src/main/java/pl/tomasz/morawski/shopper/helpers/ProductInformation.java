package pl.tomasz.morawski.shopper.helpers;


/**
 * Created by tomek on 25.04.17.
 */

public class ProductInformation {
    private Integer id;
    private String name;
    private String ean;
    private Double price;
    private Integer quantity;

    public double getTotalPrice() {
        return price * quantity;
    }

    public ProductInformation(Integer id, String name, String ean, Double price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.ean = ean;
        this.price = price;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEan() {
        return ean;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
