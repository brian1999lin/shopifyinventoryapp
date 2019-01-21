package com.brianlin.shopifyinventory.ShopifyInventoryApp.web.controller;

import com.brianlin.shopifyinventory.ShopifyInventoryApp.models.Product;
import com.brianlin.shopifyinventory.ShopifyInventoryApp.services.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    /**
     * Retrieves all products currently on the marketplace, with the option of only showing those in inventory.
     * @param available Whether to only show products that are currently available.
     * @return  A list of products on the marketplace.
     */
    @GetMapping(value = "/products/", produces="application/json")
    @ApiOperation(value = "View all products, whether with inventory or without inventory.")
    @ResponseBody
    public List<Product> getProducts(@RequestParam(value="available", required=false) Boolean available) {
        // if no parameter was passed or if user wants all products, return all products
        if(available == null || available != null && !available) {
            return productService.getAllProducts();
        } else {
            ArrayList<Product> allProducts = (ArrayList<Product>) productService.getAllProducts(); // fetch all products
            ArrayList<Product> availableProducts = new ArrayList<>();
            // return all products if
            // iterate through all products
            for (int i = 0; i < ((List) allProducts).size(); i++) {
                // if there is inventory, store the item
                if (allProducts.get(i).getInventoryCount() > 0) {
                    availableProducts.add(allProducts.get(i));
                }
            }
            return availableProducts;
        }
    }

    /**
     * Retrieve a product given its ID.
     * @param id    The ID of the product to be retrieved.
     * @return  The product belonging to the ID.
     */
    @GetMapping(value = "/products/{id}", produces="application/json")
    @ApiOperation(value = "Retrieve a product by its ID.")
    @ResponseBody
    public Product getProduct(@PathVariable("id") long id) {
        return productService.getProductById(id);
    }

    /**
     * Creates a new product given its name, price, and inventory count.
     * @param product   The information of the product to be created.
     * @return  A response of whether the product was created successfully or not.
     */
    @PostMapping(value = "/products/", produces="application/json")
    @ApiOperation(value = "Create a new product given its product name, price, and inventory count.")
    @ResponseBody
    public ResponseEntity<Object> createProduct(@RequestBody Product product) {
        productService.saveProduct(product);
        return ResponseEntity.ok("Product has been created successfully.");
    }

    /**
     * Saves a product with its updated information.
     * @param product   The updated information of the product to be saved.
     * @param id    The ID of the product to be updated.
     * @return  A response entity indicating whether the product was successfully saved or not.
     */
    @PatchMapping(value = "/products/{id}")
    @ApiOperation(value = "Saves a product with its updated information.")
    @ResponseBody
    public ResponseEntity<Object> saveProduct(@RequestBody Product product, @PathVariable("id") long id) {
        try {
            Product productToBeUpdated = productService.getProductById(id); // get existing product

            // update all product information
            productToBeUpdated.setInventoryCount(product.getInventoryCount());
            productToBeUpdated.setProductName(product.getProductName());
            productToBeUpdated.setProductPrice(product.getProductPrice());

            productService.saveProduct(productToBeUpdated); // save product
            return ResponseEntity.ok("Product has been updated.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The product was not found in the database.", e);
        }
    }

    /**
     * Deletes a product given its ID.
     * @param id    The ID of the product to be deleted.
     * @return  A response entity indicating whether the product was successfully deleted or not.
     */
    @DeleteMapping(value = "/products/{id}")
    @ApiOperation(value = "Deletes a product given its ID.")
    @ResponseBody
    public ResponseEntity<Object> deleteProduct(@PathVariable("id") long id) {
        try {
            productService.deleteProduct(id);   // delete product
            return ResponseEntity.ok("Product with ID#" + id + " was deleted.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The product was not found in the database.", e);
        }
    }

    /**
     * Purchase a product given its ID, thus lowering its inventory count by one.
     * @param id    The ID of the product to be purchased.
     * @return  A response indicating whether the item was successfully purchased or not.
     */
    @PatchMapping(value = "/products/purchase/{id}")
    @ApiOperation(value = "Purchase a product given its ID, thus lowering its inventory count by one.")
    @ResponseBody
    public ResponseEntity<Object> purchaseProduct(@PathVariable("id") long id) {
        try {
            Product purchasedProduct = productService.getProductById(id);   // fetch purchased product from db
            // remove an item from the inventory
            int inventory = purchasedProduct.getInventoryCount();
            if(inventory >= 1) {
                inventory--;
                purchasedProduct.setInventoryCount(inventory);  // update inventory count
            } else {
                return ResponseEntity.badRequest().body("This product has no inventory!"); // error if no inventory
            }
            productService.saveProduct(purchasedProduct);
            return ResponseEntity.ok("Product has been updated.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The product was not found in the database.", e);
        }
    }
}
