package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.security.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
