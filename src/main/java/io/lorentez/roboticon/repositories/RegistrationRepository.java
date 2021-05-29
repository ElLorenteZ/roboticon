package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.Registration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends CrudRepository<Registration, Long> {
}
