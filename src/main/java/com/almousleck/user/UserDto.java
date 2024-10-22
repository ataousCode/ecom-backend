package com.almousleck.user;

import com.almousleck.cart.CartDto;
import com.almousleck.order.OrderDto;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String firstname;
    private String lastname;
    private String email;
    private Gender gender;
    private String profile;
    private List<OrderDto> orders;
    private CartDto cart;
}
//private Long id;
//private String firstName;
//private String lastName;
//private String email;
//private List<OrderDto> orders;
//private CartDto cart;