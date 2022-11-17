package com.nphase.service;


import com.nphase.entity.Product;
import com.nphase.entity.ShoppingCart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.naming.OperationNotSupportedException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class ShoppingCartServiceTest {
    private final ShoppingCartService service = new ShoppingCartService();

    @Test
    public void calculatesPrice()  {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 2,"basic"),
                new Product("Coffee", BigDecimal.valueOf(6.5), 1,"basic")
        ));

        BigDecimal result = service.calculateTotalPrice(cart);

        Assertions.assertEquals(result, BigDecimal.valueOf(16.5));
    }

    @Test
    public void calculatesPriceWithDiscount()  {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.0), 5,"basic"),
                new Product("Coffee", BigDecimal.valueOf(3.5), 3,"basic")
        ));

        BigDecimal result = service.calculateTotalPrice(cart);

        Assertions.assertEquals(result, BigDecimal.valueOf(33).setScale(2, RoundingMode.HALF_UP));
    }

    @Test
    public void calculateTotalPriceByCategories()  {
        ShoppingCart cart = new ShoppingCart(Arrays.asList(
                new Product("Tea", BigDecimal.valueOf(5.3), 2,"drinks"),
                new Product("Coffee", BigDecimal.valueOf(3.5), 2,"drinks"),
        new Product("cheese", BigDecimal.valueOf(8), 2,"food")

        ));

        BigDecimal result = service.calculateTotalPriceByCategories(cart);

        Assertions.assertEquals(result, BigDecimal.valueOf(31.84).setScale(2, RoundingMode.HALF_UP));
    }

}