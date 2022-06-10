package com.example.boardv.config.auth.domain.user.General;

import com.example.boardv.config.auth.domain.user.General.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}
