package com.nphase.service;

import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ShoppingCartService {

    public BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
        var products = shoppingCart.getProducts();
        if (products == null) {
            return BigDecimal.ZERO;
        }
        return products
                .stream()
                .map(product ->
                        {
                            var priceWithDiscount = product.getQuantity() > 3 ? 0.9 : 1;
                            return Optional.ofNullable(product.getPricePerUnit())
                                    .map(calculateProductsPrice(product, priceWithDiscount))
                                    .orElse(BigDecimal.ZERO);
                        }
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateTotalPriceByCategories(ShoppingCart shoppingCart) {
        var products = shoppingCart.getProducts();
        if (products == null) {
            return BigDecimal.ZERO;
        }
        var groupedProducts = products
                .stream()
                .collect(Collectors.groupingBy(Product::getCategory));

        return groupedProducts
                .values()
                .stream()
                .map(productList -> {
                    var size = productList.size();
                    var countOfProductsInCategory = productList.stream()
                            .map(Product::getQuantity)
                            .reduce(0, Integer::sum);
                    var priceWithDiscount = size + countOfProductsInCategory > 3 ? 0.9 : 1;
                    return calculateTotal(productList, priceWithDiscount);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateTotalPriceByCategoriesWithConfig(ShoppingCart shoppingCart, int amountOfItems, double discount) {
        var products = shoppingCart.getProducts();
        if (products == null) {
            return BigDecimal.ZERO;
        }
        var groupedProducts = products
                .stream()
                .collect(Collectors.groupingBy(Product::getCategory));

        return groupedProducts
                .values()
                .stream()
                .map(productList -> {
                    var size = productList.size();
                    var countOfProductsInCategory = productList.stream()
                            .map(Product::getQuantity)
                            .reduce(0, Integer::sum);
                    var priceWithDiscount = size + countOfProductsInCategory > amountOfItems ? 1 - discount : 1;
                    return calculateTotal(productList, priceWithDiscount);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }


    private BigDecimal calculateTotal(List<Product> productList, double priceWithDiscount) {
        return productList
                .stream()
                .map(product -> Optional.ofNullable(product.getPricePerUnit())
                        .map(calculateProductsPrice(product, priceWithDiscount))
                        .orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Function<BigDecimal, BigDecimal> calculateProductsPrice(com.nphase.entity.Product product, double priceWithDiscount) {
        return pricePerUnit -> pricePerUnit
                .multiply(BigDecimal.valueOf(priceWithDiscount))
                .multiply(BigDecimal.valueOf(product.getQuantity()));
    }
}
