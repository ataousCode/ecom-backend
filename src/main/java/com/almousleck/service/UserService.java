package com.almousleck.service;

import com.almousleck.domain.User;

public interface UserService {
    User getUserById(Long userId);
    User register();
    User update();
    void delete(Long userId);
}
