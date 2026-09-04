package com.team.wishlist.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.team.wishlist.entity.Product;
import com.team.wishlist.service.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam("name") String name) {

        return productService.searchProducts(name);

    }
    
    @GetMapping("/lowest-price")
    public Product getLowestPrice(@RequestParam("name") String name) {

        return productService.getLowestPriceProduct(name);

    }
    
    @GetMapping("/compare")
    public List<Product> comparePrices(@RequestParam("name") String name) {

        return productService.comparePrices(name);

    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }
    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product existingProduct = productService.getProductById(id);

        if (existingProduct == null) {
            return null;
        }

        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStore(product.getStore());
        existingProduct.setProductUrl(product.getProductUrl());

        return productService.addProduct(existingProduct);
    }
}
