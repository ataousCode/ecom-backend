package com.almousleck.user;

import com.almousleck.exception.RequestValidationException;
import com.almousleck.exception.ResourceNotFound;
import com.almousleck.exception.UserAlreadyExistException;
import com.almousleck.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.almousleck.constants.Constant.PHOTO_DIRECTORY;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Fetching user", userDto));
        } catch (ResourceNotFound ex) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(ex.getMessage(), null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserRegistrationRequest request) {
        log.info("Register request received: {}", request);
        try {
            User user = userService.register(request);
            UserDto dto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("User created success", dto));
        }catch (UserAlreadyExistException ex) {
            return ResponseEntity.status(CONFLICT)
                    .body(new ApiResponse(ex.getMessage(), null));
        }
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        log.info("Update request received: {}", request);
        try {
            User user = userService.updateUser(userId, request);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Update User Success!", userDto));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (RequestValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            log.error("An unexpected error occurred: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("An unexpected error occurred", null));
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long userId) {
        log.warn("Delete request receive: {}", userId);
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success!", null));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }


    @PutMapping("/photo")
    public ResponseEntity<String> uploadProfile(@RequestParam("email") String email,
                                                @RequestParam("file")MultipartFile file) {
        return ResponseEntity.ok().body(userService.uploadPhoto(email, file));
    }

    @GetMapping(path = "/image/{filename}", produces = { IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(PHOTO_DIRECTORY + filename));
    }
}
