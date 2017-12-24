/**
 * OrderDetailBean.java. Created Tuesday, August 5 2014
 * @author Michael Kazarian
 * Tags:
 */
package com.blogspot.mkazarian;

public class OrderDetailBean{
    /**
     * This class describes detail records for OrderBean.
     */
    private String name;
    private int quantity;
    private Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
//File: OrderDetailBean.java ends here.
