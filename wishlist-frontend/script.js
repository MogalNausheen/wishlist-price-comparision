function searchProduct() {

    const productName = document.getElementById("searchInput").value;

    if (productName.trim() === "") {

        alert("Please enter a product name");
        return;
    }


    const results =
        document.getElementById("productResults");


    results.innerHTML = `
        <div class="col-12 text-center">
            <h4>🔍 Searching for products...</h4>
        </div>
    `;


    fetch(
        `http://localhost:8080/api/external-products/search?name=${encodeURIComponent(productName)}`
    )

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Failed to fetch products"
                );
            }

            return response.json();
        })


        .then(products => {

            results.innerHTML = "";


            // ==========================================
            // NO PRODUCTS
            // ==========================================

            if (products.length === 0) {

                results.innerHTML = `
                    <div class="col-12 text-center">

                        <h4>No products found</h4>

                        <p>
                            Try searching for another product.
                        </p>

                    </div>
                `;

                return;
            }


            // ==========================================
            // FIND PRODUCTS THAT MATCH THE SEARCH
            // ==========================================

            const searchWords =
                productName
                    .toLowerCase()
                    .trim()
                    .split(/\s+/);


            const matchingProducts =
                products.filter(product => {

                    const productTitle =
                        product.name.toLowerCase();


                    return searchWords.every(word =>
                        productTitle.includes(word)
                    );

                });


            // Use matching products if available

            const productsForPriceComparison =
                matchingProducts.length > 0
                    ? matchingProducts
                    : products;


            // ==========================================
            // SEPARATE NEW AND REFURBISHED PRODUCTS
            // ==========================================

            const newProducts =
                productsForPriceComparison.filter(product =>
                    !product.refurbished
                );


            const refurbishedProducts =
                productsForPriceComparison.filter(product =>
                    product.refurbished
                );


            // ==========================================
            // GET NEW PRODUCT PRICES
            // ==========================================

            const newPrices =
                newProducts
                    .map(product => {

                        return parseFloat(
                            product.price.replace(
                                /[^0-9.]/g,
                                ""
                            )
                        );

                    })
                    .filter(price => !isNaN(price));


            // ==========================================
            // FIND BEST NEW PRICE
            // ==========================================

            const bestNewPrice =
                newPrices.length > 0
                    ? Math.min(...newPrices)
                    : null;


            // ==========================================
            // GET REFURBISHED PRODUCT PRICES
            // ==========================================

            const refurbishedPrices =
                refurbishedProducts
                    .map(product => {

                        return parseFloat(
                            product.price.replace(
                                /[^0-9.]/g,
                                ""
                            )
                        );

                    })
                    .filter(price => !isNaN(price));


            // ==========================================
            // FIND BEST REFURBISHED PRICE
            // ==========================================

            const bestRefurbishedPrice =
                refurbishedPrices.length > 0
                    ? Math.min(...refurbishedPrices)
                    : null;


            // ==========================================
            // SORT PRODUCTS
            // LOWEST PRICE → HIGHEST PRICE
            // ==========================================

            products.sort((a, b) => {

                const priceA =
                    parseFloat(
                        a.price.replace(
                            /[^0-9.]/g,
                            ""
                        )
                    );


                const priceB =
                    parseFloat(
                        b.price.replace(
                            /[^0-9.]/g,
                            ""
                        )
                    );


                return priceA - priceB;

            });


            // ==========================================
            // DISPLAY PRODUCTS
            // ==========================================

            products.forEach((product, index) => {

                const numericPrice =
                    parseFloat(
                        product.price.replace(
                            /[^0-9.]/g,
                            ""
                        )
                    );


                // ==========================================
                // CHECK BEST NEW PRICE
                // ==========================================

                const isBestNewPrice =
                    !product.refurbished &&
                    numericPrice === bestNewPrice &&
                    productsForPriceComparison.includes(product);


                // ==========================================
                // CHECK BEST REFURBISHED PRICE
                // ==========================================

                const isBestRefurbishedPrice =
                    product.refurbished &&
                    numericPrice === bestRefurbishedPrice &&
                    productsForPriceComparison.includes(product);


                // ==========================================
                // PRODUCT CARD
                // ==========================================

                results.innerHTML += `

                    <div class="col-md-4 mb-4">

                        <div class="card shadow product-card">


                            <!-- PRODUCT IMAGE -->

                            <img
                                src="${product.imageUrl}"
                                class="card-img-top"
                                alt="${product.name}"
                            >


                            <div class="card-body">


                                <!-- PRODUCT NAME -->

                                <h5 class="card-title">

                                    ${product.name}

                                </h5>


                                <!-- PRICE -->

                                <h4 class="product-price">

                                    ${product.price}

                                </h4>


                                <!-- STORE -->

                                <p>

                                    Store:

                                    <strong>
                                        ${product.store}
                                    </strong>

                                </p>


                                <!-- BEST NEW PRICE -->

                                ${
                                    isBestNewPrice
                                        ? `
                                            <span class="badge bg-success">

                                                🏆 Best New Price

                                            </span>
                                          `
                                        : ""
                                }


                                <!-- BEST REFURBISHED PRICE -->

                                ${
                                    isBestRefurbishedPrice
                                        ? `
                                            <span class="badge bg-warning text-dark">

                                                🔄 Best Refurbished Price

                                            </span>
                                          `
                                        : ""
                                }


                                <br>
                                <br>


                                <!-- VIEW PRODUCT -->

                                <a
                                    href="${product.productUrl}"
                                    target="_blank"
                                    class="btn btn-primary"
                                >

                                    View Product

                                </a>


                                <!-- ADD TO WISHLIST -->

                                <button
                                    class="btn btn-outline-danger mt-2 wishlist-btn"
                                    data-product-index="${index}"
                                >

                                    ❤️ Add to Wishlist

                                </button>


                            </div>

                        </div>

                    </div>

                `;

            });


            // ==========================================
            // WISHLIST BUTTON EVENT LISTENERS
            // ==========================================

            document
                .querySelectorAll(".wishlist-btn")
                .forEach(button => {

                    button.addEventListener(
                        "click",
                        function () {

                            const index =
                                this.getAttribute(
                                    "data-product-index"
                                );


                            const product =
                                products[index];


                            addToWishlist(product);

                        }
                    );

                });

        })


        // ==========================================
        // ERROR HANDLING
        // ==========================================

        .catch(error => {

            console.error(
                "Error:",
                error
            );


            results.innerHTML = `

                <div class="col-12 text-center">

                    <h4>
                        ❌ Unable to load products
                    </h4>

                    <p>
                        Please make sure the Spring Boot server is running.
                    </p>

                </div>

            `;

        });

}



// ==================================================
// ADD PRODUCT TO WISHLIST
// ==================================================

function addToWishlist(product) {

    const wishlistItem = {

        productName: product.name,

        price: product.price,

        store: product.store,

        imageUrl: product.imageUrl,

        productUrl: product.productUrl

    };


    fetch(
        "http://localhost:8080/api/wishlist",
        {

            method: "POST",

            headers: {

                "Content-Type": "application/json"

            },

            body: JSON.stringify(wishlistItem)

        }
    )

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Failed to add product to wishlist"
                );

            }

            return response.json();

        })


        .then(data => {

            alert(
                "❤️ Product added to wishlist!"
            );


            console.log(
                "Wishlist item:",
                data
            );


            loadWishlist();

        })


        .catch(error => {

            console.error(
                "Error:",
                error
            );


            alert(
                "Unable to add product to wishlist"
            );

        });

}



// ==================================================
// LOAD WISHLIST
// ==================================================

function loadWishlist() {

    fetch(
        "http://localhost:8080/api/wishlist"
    )

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Failed to load wishlist"
                );

            }

            return response.json();

        })


        .then(wishlist => {

            const wishlistResults =
                document.getElementById(
                    "wishlistResults"
                );


            wishlistResults.innerHTML = "";


            // ==========================================
            // EMPTY WISHLIST
            // ==========================================

            if (wishlist.length === 0) {

                wishlistResults.innerHTML = `

                    <div class="col-12 text-center">

                        <div class="card shadow p-5">

                            <h3>
                                ❤️ Your Wishlist is Empty
                            </h3>

                            <p class="text-muted">

                                Add products to your wishlist
                                to compare them later.

                            </p>

                        </div>

                    </div>

                `;

                return;
            }


            // ==========================================
            // WISHLIST TITLE
            // ==========================================

            wishlistResults.innerHTML += `

                <div class="col-12 mb-4">

                    <h3 class="wishlist-title">

                        ❤️ My Wishlist

                    </h3>


                    <p class="wishlist-subtitle">

                        Products you saved for later

                    </p>

                </div>

            `;


            // ==========================================
            // DISPLAY WISHLIST PRODUCTS
            // ==========================================

            wishlist.forEach(item => {

                wishlistResults.innerHTML += `

                    <div class="col-md-4 mb-4">

                        <div class="card shadow product-card">


                            <!-- IMAGE -->

                            <img
                                src="${item.imageUrl}"
                                class="card-img-top"
                                alt="${item.productName}"
                            >


                            <div class="card-body">


                                <!-- PRODUCT NAME -->

                                <h5 class="card-title">

                                    ${item.productName}

                                </h5>


                                <!-- PRICE -->

                                <h4 class="product-price">

                                    ${item.price}

                                </h4>


                                <!-- STORE -->

                                <p>

                                    Store:

                                    <strong>
                                        ${item.store}
                                    </strong>

                                </p>


                                <!-- VIEW PRODUCT -->

                                <a
                                    href="${item.productUrl}"
                                    target="_blank"
                                    class="btn btn-primary"
                                >

                                    🔗 View Product

                                </a>


                                <!-- REMOVE -->

                                <button
                                    class="btn btn-outline-danger remove-btn"
                                    onclick="removeFromWishlist(${item.id})"
                                >

                                    🗑️ Remove from Wishlist

                                </button>


                            </div>

                        </div>

                    </div>

                `;

            });

        })


        // ==========================================
        // ERROR HANDLING
        // ==========================================

        .catch(error => {

            console.error(
                "Error:",
                error
            );


            const wishlistResults =
                document.getElementById(
                    "wishlistResults"
                );


            wishlistResults.innerHTML = `

                <div class="col-12 text-center">

                    <p class="text-danger">

                        ❌ Unable to load wishlist.

                    </p>

                </div>

            `;

        });

}



// ==================================================
// REMOVE PRODUCT FROM WISHLIST
// ==================================================

function removeFromWishlist(id) {

    fetch(
        `http://localhost:8080/api/wishlist/${id}`,
        {

            method: "DELETE"

        }
    )

        .then(response => {

            if (!response.ok) {

                throw new Error(
                    "Failed to remove product"
                );

            }

            return response.text();

        })


        .then(message => {

            alert(
                "🗑️ Product removed from wishlist!"
            );


            loadWishlist();

        })


        .catch(error => {

            console.error(
                "Error:",
                error
            );


            alert(
                "Unable to remove product from wishlist"
            );

        });

}



// ==================================================
// LOAD WISHLIST WHEN PAGE OPENS
// ==================================================

window.onload = function () {

    loadWishlist();

};