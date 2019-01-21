package com.brianlin.shopifyinventory.ShopifyInventoryApp.exceptions;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String exception) {
        super("The product was not found in the database.");
    }
}