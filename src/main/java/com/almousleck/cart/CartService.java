package com.almousleck.cart;

import java.math.BigDecimal;

public interface CartService {
    Cart getCart(Long cartId);
    void clearCart(Long cartId);
    BigDecimal getTotalPrice(Long cartId);

    Long initializeNewCart();
}
