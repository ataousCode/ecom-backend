package com.almousleck.service;

import com.almousleck.domain.Cart;

import java.math.BigDecimal;

public interface CartService {
    Cart getCart(Long cartId);
    void clearCart(Long cartId);
    BigDecimal getTotalPrice(Long cartId);

    Long initializeNewCart();
}
