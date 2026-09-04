package com.team.wishlist.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.team.wishlist.entity.ExternalProduct;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExternalProductService {

    @Value("${serpapi.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();


    // ==========================================
    // SEARCH PRODUCTS
    // ==========================================

    public List<ExternalProduct> searchProducts(String productName) {

        List<ExternalProduct> products = new ArrayList<>();

        try {

            String url = "https://serpapi.com/search.json"
                    + "?engine=google_shopping"
                    + "&q=" + java.net.URLEncoder.encode(
                            productName,
                            java.nio.charset.StandardCharsets.UTF_8)
                    + "&api_key=" + apiKey;


            String response =
                    restTemplate.getForObject(url, String.class);


            JsonNode root =
                    objectMapper.readTree(response);


            JsonNode shoppingResults =
                    root.get("shopping_results");


            if (shoppingResults != null) {

                String searchName =
                        productName.toLowerCase().trim();


                for (JsonNode item : shoppingResults) {

                    String name =
                            item.path("title").asText();


                    String price =
                            item.path("price").asText();


                    String store =
                            item.path("source").asText();


                    String imageUrl =
                            item.path("thumbnail").asText();


                    // ==========================================
                    // GET PRODUCT LINK
                    // ==========================================

                    String productUrl =
                            item.path("product_link").asText("");


                    // Fallback product link

                    if (productUrl == null
                            || productUrl.isBlank()) {

                        String searchQuery =
                                name + " " + store;


                        productUrl =
                                "https://www.google.com/search?tbm=shop&q="
                                + java.net.URLEncoder.encode(
                                        searchQuery,
                                        java.nio.charset.StandardCharsets.UTF_8);
                    }


                    // ==========================================
                    // IGNORE MONTHLY INSTALLMENT PRICES
                    // ==========================================

                    if (price.contains("/mo")) {
                        continue;
                    }


                    String productTitle =
                            name.toLowerCase();


                    // ==========================================
                    // MATCH PRODUCT
                    // ==========================================

                    if (!isMatchingProduct(
                            productTitle,
                            searchName)) {

                        continue;
                    }


                    // ==========================================
                    // IGNORE SUSPICIOUS IPHONE 16 PRICES
                    // ==========================================

                    JsonNode extractedPriceNode =
                            item.get("extracted_price");


                    if (extractedPriceNode != null
                            && extractedPriceNode.isNumber()) {

                        double extractedPrice =
                                extractedPriceNode.asDouble();


                        if (searchName.contains("iphone 16")
                                && extractedPrice < 100) {

                            continue;
                        }
                    }


                    // ==========================================
                    // CHECK REFURBISHED
                    // ==========================================

                    boolean refurbished =
                            isRefurbished(
                                    item,
                                    productTitle);


                    // ==========================================
                    // ADD REFURBISHED LABEL
                    // ==========================================

                    if (refurbished
                            && !name.toLowerCase()
                                    .contains("refurbished")
                            && !name.toLowerCase()
                                    .contains("restored")
                            && !name.toLowerCase()
                                    .contains("used")) {

                        name =
                                name + " (Refurbished)";
                    }


                    // ==========================================
                    // CREATE PRODUCT
                    // ==========================================

                    ExternalProduct product =
                            new ExternalProduct(
                                    name,
                                    price,
                                    store,
                                    imageUrl,
                                    productUrl,
                                    refurbished
                            );


                    products.add(product);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }


        return products;
    }


    // ==========================================
    // CHECK REFURBISHED PRODUCT
    // ==========================================

    private boolean isRefurbished(
            JsonNode item,
            String productTitle) {


        // Check SerpApi second-hand condition

        String condition =
                item.path("second_hand_condition")
                        .asText("")
                        .toLowerCase();


        if (condition.contains("refurbished")
                || condition.contains("used")
                || condition.contains("pre-owned")
                || condition.contains("second")) {

            return true;
        }


        // Check product title

        if (productTitle.contains("refurbished")
                || productTitle.contains("restored")
                || productTitle.contains("renewed")
                || productTitle.contains("pre-owned")
                || productTitle.contains("used")) {

            return true;
        }


        return false;
    }


    // ==========================================
    // MATCH SEARCHED PRODUCT
    // ==========================================

    private boolean isMatchingProduct(
            String productTitle,
            String searchName) {


        // ==========================================
        // IGNORE ACCESSORIES
        // ==========================================

        String[] unwantedWords = {

                "case",
                "cover",
                "charger",
                "cable",
                "screen protector",
                "tempered glass",
                "adapter",
                "holder",
                "stand",
                "replacement",
                "battery",
                "protector"
        };


        for (String word : unwantedWords) {

            if (productTitle.contains(word)) {

                return false;
            }
        }


        // ==========================================
        // IPHONE 16
        // ==========================================

        if (searchName.equals("iphone 16")) {


            // Exclude different iPhone 16 models

            if (productTitle.contains("iphone 16 pro")
                    || productTitle.contains("iphone 16 plus")
                    || productTitle.contains("iphone 16e")) {

                return false;
            }


            // Must contain iPhone 16

            if (!productTitle.contains("iphone 16")) {

                return false;
            }


            return true;
        }


        // ==========================================
        // SAMSUNG GALAXY S25
        // ==========================================

        if (searchName.equals("samsung galaxy s25")) {


            // Exclude different S25 models

            if (productTitle.contains("s25 fe")
                    || productTitle.contains("s25 plus")
                    || productTitle.contains("s25 ultra")
                    || productTitle.contains("s25 edge")) {

                return false;
            }


            // Must contain Samsung Galaxy S25

            if (!productTitle.contains("samsung galaxy s25")) {

                return false;
            }


            return true;
        }


        // ==========================================
        // GENERAL PRODUCT MATCHING
        // ==========================================

        String[] searchWords =
                searchName.split("\\s+");


        for (String word : searchWords) {

            if (!productTitle.contains(word)) {

                return false;
            }
        }


        return true;
    }
}