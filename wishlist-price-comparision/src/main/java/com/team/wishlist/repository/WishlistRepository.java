package com.team.wishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.wishlist.entity.WishlistItem;

public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {

}
