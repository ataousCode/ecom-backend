package com.almousleck.user;

public interface UserService {
    User getUserById(Long userId);
    User register();
    User update();
    void delete(Long userId);
}
