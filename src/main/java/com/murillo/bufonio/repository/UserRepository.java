package com.murillo.bufonio.repository;

import java.util.Optional;

import com.murillo.bufonio.dto.summary.UserSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.murillo.bufonio.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmailUser(String emailUser);

    Optional<User> findUserByIdUser(Long idUser);
    Optional<User> findUserByEmailUser(String emailUser);

    Optional<UserSummary> findUserSummaryByIdUser(Long idUser);
}