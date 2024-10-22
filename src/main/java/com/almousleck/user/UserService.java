package com.almousleck.user;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User getUserById(Long userId);
    User getUserByEmail(String email);
    User register(UserRegistrationRequest request);
    User updateUser(Long userId, UserUpdateRequest request);
    void deleteUser(Long userId);
    UserDto convertUserToDto(User user);
    String uploadPhoto(String email, MultipartFile file);

}
