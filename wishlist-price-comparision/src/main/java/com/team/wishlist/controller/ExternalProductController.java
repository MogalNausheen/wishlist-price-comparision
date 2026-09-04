package com.team.wishlist.controller;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.team.wishlist.entity.ExternalProduct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.wishlist.service.ExternalProductService;

@RestController
@RequestMapping("/api/external-products")
@CrossOrigin(origins = "*")
public class ExternalProductController {

    private final ExternalProductService externalProductService;

    public ExternalProductController(ExternalProductService externalProductService) {
        this.externalProductService = externalProductService;
    }

    @GetMapping("/search")
    public List<ExternalProduct> searchProducts(@RequestParam("name") String name) {
        return externalProductService.searchProducts(name);
    }
}
