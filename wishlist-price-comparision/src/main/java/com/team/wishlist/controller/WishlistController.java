package com.team.wishlist.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.team.wishlist.entity.WishlistItem;
import com.team.wishlist.service.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public WishlistItem addToWishlist(@RequestBody WishlistItem item) {
        return wishlistService.addToWishlist(item);
    }

    @GetMapping
    public List<WishlistItem> getWishlist() {
        return wishlistService.getWishlist();
    }

    @DeleteMapping("/{id}")
    public String removeFromWishlist(@PathVariable Long id) {
        wishlistService.removeFromWishlist(id);
        return "Product removed from wishlist";
    }
}
