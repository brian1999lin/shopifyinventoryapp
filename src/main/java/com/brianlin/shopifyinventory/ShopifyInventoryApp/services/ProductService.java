package com.brianlin.shopifyinventory.ShopifyInventoryApp.services;

import com.brianlin.shopifyinventory.ShopifyInventoryApp.models.Product;
import com.brianlin.shopifyinventory.ShopifyInventoryApp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    /**
     * Returns a list of all products in inventory.
     * @return A list of all products currently in inventory.
     */
    public List<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        productRepository.findAll().forEach(product -> products.add(product));
        return products;
    }

    /**
     * Returns the product matching a query id.
     * @param id The query id of the product.
     * @return The product matching the query id.
     */
    public Product getProductById(long id) {
        return productRepository.findById(id).get();
    }

    /**
     * Saves a product to the database.
     * @param product The product to be saved.
     */
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    /**
     * Deletes a product given its id.
     * @param id The id of the product to be deleted.
     */
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }
}
