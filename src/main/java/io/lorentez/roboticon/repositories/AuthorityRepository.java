package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.security.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, Long> {
}
