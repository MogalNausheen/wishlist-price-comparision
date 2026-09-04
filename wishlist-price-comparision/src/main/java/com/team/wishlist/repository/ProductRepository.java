package com.team.wishlist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.wishlist.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByNameContainingIgnoreCase(String name);
	
}