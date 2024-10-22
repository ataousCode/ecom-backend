package com.almousleck.user;

public record UserRegistrationRequest(
        String firstname,
        String lastname,
        String email,
        Gender gender,
        String password
) {
}
