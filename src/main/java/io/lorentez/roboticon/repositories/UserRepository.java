package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.security.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query(value = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userRoles ur WHERE u.email = :email")
    Optional<User> findByEmailWithAuthorities(String email);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    List<User> findAll();
}
