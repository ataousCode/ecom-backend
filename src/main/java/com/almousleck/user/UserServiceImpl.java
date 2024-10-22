package com.almousleck.user;

import com.almousleck.exception.RequestValidationException;
import com.almousleck.exception.ResourceNotFound;
import com.almousleck.exception.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.almousleck.constants.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new ResourceNotFound("User not found"));
    }

    @Override
    public User register(UserRegistrationRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository
                        .existsUserByEmail(request.email()))
                .map(req -> {
                    User user = User.builder()
                            .firstname(request.firstname())
                            .lastname(request.lastname())
                            .email(request.email())
                            .gender(request.gender())
                            .password(request.password())
                            .build();
                    return userRepository.save(user);
                }).orElseThrow(
                        () -> new UserAlreadyExistException("Ops! user with the given email: [%s] taken"
                                .formatted(request.email()))
                );
    }

    @Override
    public User updateUser(Long userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("Ops! user with the given id: [%s] not found"
                        .formatted(userId)
                ));
        boolean changes = false;

        if (request.firstname() != null && !request.firstname().equals(user.getFirstname())) {
            user.setFirstname(request.firstname());
            changes = true;
        }

        if (request.lastname() != null && !request.lastname().equals(user.getLastname())) {
            user.setLastname(request.lastname());
            changes = true;
        }

        if (request.email() != null && !request.email().equals(user.getEmail())) {
            if (userRepository.existsUserByEmail(request.email()))
                throw new UserAlreadyExistException("Email already taken");
            user.setEmail(request.email());
            changes = true;
        }

        if (!changes)
            throw new RequestValidationException("No data changes found");
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete, () -> {
                    throw new ResourceNotFound("Ops! user with the given id: [%s] not found"
                            .formatted(userId));
                });
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public String uploadPhoto(String userEmail, MultipartFile file) {
        log.info("Saving picture for user with id: {}", userEmail);
        User user = getUserByEmail(userEmail);
        String photoUri = photoFunction.apply(userEmail, file);
        user.setProfile(photoUri);
        return photoUri;
    }

    private final Function<String, String> fileExtension = filename ->
            Optional.of(filename)
                    .filter(name -> name.contains("."))
                    .map(name -> "." + name
                            .substring(filename.lastIndexOf(".") + 1))
                    .orElse(".npg");

    private final BiFunction<String, MultipartFile, String> photoFunction = (id, image) -> {
        String filename = id + fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorageLocation = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation))
                Files.createDirectories(fileStorageLocation);
            Files.copy(image.getInputStream(), fileStorageLocation.resolve(filename), REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/users/image/" + filename).toUriString();
        } catch (Exception ex) {
            throw new RuntimeException("Unable to save image");
        }
    };
}
