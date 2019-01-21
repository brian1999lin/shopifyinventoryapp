package com.brianlin.shopifyinventory.ShopifyInventoryApp.repositories;

import com.brianlin.shopifyinventory.ShopifyInventoryApp.models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ProductRepository extends CrudRepository<Product, Long> {
}
