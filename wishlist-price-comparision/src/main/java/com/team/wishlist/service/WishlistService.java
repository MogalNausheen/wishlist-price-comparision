package com.team.wishlist.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.team.wishlist.entity.WishlistItem;
import com.team.wishlist.repository.WishlistRepository;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(WishlistRepository wishlistRepository) {
        this.wishlistRepository = wishlistRepository;
    }

    public WishlistItem addToWishlist(WishlistItem item) {
        return wishlistRepository.save(item);
    }

    public List<WishlistItem> getWishlist() {
        return wishlistRepository.findAll();
    }

    public void removeFromWishlist(Long id) {
        wishlistRepository.deleteById(id);
    }
}
