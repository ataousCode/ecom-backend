package com.almousleck.user;

public record UserUpdateRequest(
        String firstname,
        String lastname,
        String email
) {
}
