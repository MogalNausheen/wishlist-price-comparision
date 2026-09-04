package com.team.wishlist.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.team.wishlist.entity.Product;
import com.team.wishlist.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    

public List<Product> searchProducts(String name) {
	return productRepository.findByNameContainingIgnoreCase(name);
}

public Product getLowestPriceProduct(String name) {

    List<Product> products = productRepository.findByNameContainingIgnoreCase(name);

    if (products.isEmpty()) {
        return null;
    }
    

    Product lowestPriceProduct = products.get(0);

    for (Product product : products) {

        if (product.getPrice() < lowestPriceProduct.getPrice()) {
            lowestPriceProduct = product;
        }
    }

    return lowestPriceProduct;
}

public List<Product> comparePrices(String name) {
    List<Product> products = productRepository.findByNameContainingIgnoreCase(name);

    products.sort((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));

    return products;
}
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
