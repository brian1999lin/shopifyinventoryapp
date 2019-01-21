package com.brianlin.shopifyinventory.ShopifyInventoryApp.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String productName;
    private double productPrice;
    private int inventoryCount;

    public Product() { }
    public Product(String productName, double productPrice, int inventoryCount) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.inventoryCount = inventoryCount;
    }

    /**
     * Gets a product ID.
     * @return The ID of the product.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the name of the product.
     * @return The name of the product.
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the name of the product.
     * @param productName The new name of the product.
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the price of the product.
     * @return The price of the product.
     */
    public double getProductPrice() {
        return productPrice;
    }

    /**
     * Sets the price of the product.
     * @param productPrice The new price of the product.
     */
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * Gets the amount of the product currently available in inventory.
     * @return The amount of the product currently available in inventory.
     */
    public int getInventoryCount() {
        return inventoryCount;
    }

    /**
     * Sets the amount of the product currently available in inventory.
     * @param inventoryCount The amount of the product currently available in inventory.
     */
    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount = inventoryCount;
    }
}
