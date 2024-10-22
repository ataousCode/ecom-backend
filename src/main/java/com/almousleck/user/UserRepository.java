package com.almousleck.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
    boolean existsUserById(Long id);
    Optional<User> findUserByEmail(String email);
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.profile = ?1 WHERE u.id = ?2")
    int updateProfileImage(String profile, Long userId);
}
