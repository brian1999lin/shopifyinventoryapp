package com.brianlin.shopifyinventory.ShopifyInventoryApp;

import com.brianlin.shopifyinventory.ShopifyInventoryApp.services.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.json.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)	// clear DB before each test method
@AutoConfigureMockMvc
public class ShopifyInventoryAppApplicationTests {

	@Autowired
	private MockMvc mockMvc;	// autowire mockMVC to simulate endpoint calls

	@Autowired
	private ProductService productService;

	/**
	 * Test whether a product can be successfully created.
	 * @throws Exception
	 */
	@Test
	public void testCreateNewProduct() throws Exception {
		// perform mock REST call to create product
		this.mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON)
				.content("{\"productName\":\"Shampoo\",\"productPrice\":49.99,\"inventoryCount\":50}"))
				.andExpect(status().isCreated());
		// ensure a product is actually created with the given information
		if(productService.getAllProducts().size() != 1){ fail(); }
	}

	/**
	 * Test whether all products can be retrieved properly.
	 * @throws Exception
	 */
	@Test
	public void testGetAllProducts() throws Exception {
		// perform mock REST call to create products
		this.mockMvc.perform(post("/products/").contentType(MediaType.APPLICATION_JSON)
				.content("{\"productName\":\"Shampoo\",\"productPrice\":49.99,\"inventoryCount\":50}"))
				.andExpect(status().is2xxSuccessful());

		this.mockMvc.perform(post("/products/").contentType(MediaType.APPLICATION_JSON)
				.content("{\"productName\":\"Conditioner\",\"productPrice\":42.99,\"inventoryCount\":5}"))
				.andExpect(status().is2xxSuccessful());

		// fetch all products to ensure products can be retrieved and saved successfully
		MvcResult result = this.mockMvc.perform(get("/products/")).andExpect(status().isOk())
				.andReturn();

		// transform string into JSON
		JSONArray resultArray = new JSONArray(result.getResponse().getContentAsString());

		// check both products to ensure information was saved correctly
		JSONObject resultObject = (JSONObject) resultArray.get(0);

		// ensure JSON array length is indeed 2 and the first object is correct
		if(resultArray.length() != 2 || !resultObject.get("productName").equals("Shampoo") ||
				(double) resultObject.get("productPrice") != 49.99 ||
				(int) resultObject.get("inventoryCount") != 50) { fail(); }

		// update result object to check second object
		resultObject = (JSONObject) resultArray.get(1);

		// ensure JSON array length is indeed 1 and the second object is correct
		if(!resultObject.get("productName").equals("Conditioner") ||
				(double) resultObject.get("productPrice") != 42.99 ||
				(int) resultObject.get("inventoryCount") != 5) { fail(); }
	}

	/**
	 * Test whether only available products are shown.
	 * @throws Exception
	 */
	@Test
	public void testGetAvailableProducts() throws Exception {
		// perform mock REST call to create products
		this.mockMvc.perform(post("/products/").contentType(MediaType.APPLICATION_JSON)
				.content("{\"productName\":\"Shampoo\",\"productPrice\":49.99,\"inventoryCount\":50}"))
				.andExpect(status().is2xxSuccessful());

		this.mockMvc.perform(post("/products/").contentType(MediaType.APPLICATION_JSON)
				.content("{\"productName\":\"Conditioner\",\"productPrice\":42.99,\"inventoryCount\":0}"))
				.andExpect(status().is2xxSuccessful());

		// ensure products can be retrieved and saved successfully
		MvcResult result = this.mockMvc.perform(get("/products/?available=true")).andExpect(status().isOk())
				.andReturn();

		// transform string into JSON
		JSONArray resultArray = new JSONArray(result.getResponse().getContentAsString());
		JSONObject resultObject = (JSONObject) resultArray.get(0);

		// ensure JSON array length is indeed 1 and the object is Shampoo, the item with inventory
		if(resultArray.length() != 1 || !resultObject.get("productName").equals("Shampoo")) { fail(); }
	}

	/**
	 * Test whether user can fetch a single product successfully.
	 * @throws Exception
	 */
	@Test
	public void testGetSingleProductById() throws Exception {
		// perform mock REST call to create product
		this.mockMvc.perform(post("/products/").contentType(MediaType.APPLICATION_JSON)
				.content("{\"productName\":\"Shampoo\",\"productPrice\":49.99,\"inventoryCount\":50}"))
				.andExpect(status().is2xxSuccessful());

		// ensure products can be retrieved and saved successfully
		MvcResult result = this.mockMvc.perform(get("/products/1")).andExpect(status().isOk())
				.andReturn();

		// transform string into JSON object
		JSONObject resultObject = new JSONObject(result.getResponse().getContentAsString());

		// ensure JSON array length is indeed 1 and the product is correct
		if(!resultObject.get("productName").equals("Shampoo") ||
				(double) resultObject.get("productPrice") != 49.99 ||
				(int) resultObject.get("inventoryCount") != 50) { fail(); }
	}

	/**
	 * Test whether user can fetch a single product successfully.
	 * @throws Exception
	 */
	@Test
	public void testDeleteProduct() throws Exception {
		// perform mock REST call to create product
		this.mockMvc.perform(post("/products/").contentType(MediaType.APPLICATION_JSON)
				.content("{\"productName\":\"Shampoo\",\"productPrice\":49.99,\"inventoryCount\":50}"))
				.andExpect(status().is2xxSuccessful());

		// ensure product can be deleted successfully
		this.mockMvc.perform(delete("/products/1")).andExpect(status().is2xxSuccessful());

		// retrieve all products; should be empty
		MvcResult result = this.mockMvc.perform(get("/products/")).andExpect(status().is2xxSuccessful())
				.andReturn();

		// transform string into JSON
		JSONArray resultArray = new JSONArray(result.getResponse().getContentAsString());

		// ensure JSON array length is 0 since there are no products
		if(resultArray.length() != 0) { fail(); }
	}

	/**
	 * Test updating of a product.
	 * @throws Exception
	 */
	@Test
	public void testUpdateProduct() throws Exception {
		// perform mock REST call to create product
		this.mockMvc.perform(post("/products/").contentType(MediaType.APPLICATION_JSON)
				.content("{\"productName\":\"Shampoo\",\"productPrice\":49.99,\"inventoryCount\":50}"))
				.andExpect(status().is2xxSuccessful());

		// update product with test information
		this.mockMvc.perform(patch("/products/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"productName\":\"Conditioner\",\"productPrice\":59.99,\"inventoryCount\":5}"))
				.andExpect(status().is2xxSuccessful());

		// retrieve updated product
		MvcResult result = this.mockMvc.perform(get("/products/1")).andExpect(status().isOk())
				.andReturn();

		// transform string into JSON object
		JSONObject resultObject = new JSONObject(result.getResponse().getContentAsString());

		// ensure JSON array length is indeed 1 and the product is correct
		if(!resultObject.get("productName").equals("Conditioner") ||
				(double) resultObject.get("productPrice") != 59.99 ||
				(int) resultObject.get("inventoryCount") != 5) { fail(); }
	}

	/**
	 * Test purchasing a product.
	 * @throws Exception
	 */
	@Test
	public void testPurchaseProduct() throws Exception {
		// perform mock REST call to create product
		this.mockMvc.perform(post("/products/").contentType(MediaType.APPLICATION_JSON)
				.content("{\"productName\":\"Shampoo\",\"productPrice\":49.99,\"inventoryCount\":50}"))
				.andExpect(status().is2xxSuccessful());

		// update product with test information
		this.mockMvc.perform(patch("/products/purchase/1"));

		// retrieve updated product
		MvcResult result = this.mockMvc.perform(get("/products/1")).andExpect(status().isOk())
				.andReturn();

		// transform string into JSON object
		JSONObject resultObject = new JSONObject(result.getResponse().getContentAsString());

		// ensure that inventory has been updated accordingly
		if(!resultObject.get("productName").equals("Shampoo") ||
				(double) resultObject.get("productPrice") != 49.99 ||
				(int) resultObject.get("inventoryCount") != 49) { fail(); }
	}
}

