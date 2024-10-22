package com.almousleck.cart;

import com.almousleck.exception.ResourceNotFound;
import com.almousleck.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/cartItems")
@RequiredArgsConstructor
public class CartItemController {
    private final CartItemService cartItemService;
    private final CartService cartService;

    @PostMapping("/addToCart")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId,
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {
            if (cartId == null)
                cartId = cartService.initializeNewCart();
            cartItemService.addItemToCart(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("New item added success", null));
        } catch (ResourceNotFound ex) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{cartId}/{itemId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(
            @PathVariable Long cartId, @PathVariable Long itemId) {
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Item deleted success", null));
        }catch (ResourceNotFound ex) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage(), null));
        }
    }

    @PutMapping("/update/{cartId}/{itemId}")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                          @PathVariable Long itemId,
                                                          @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Item updated success", null));
        } catch (ResourceNotFound ex) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(ex.getMessage(), null));
        }
    }
















}
