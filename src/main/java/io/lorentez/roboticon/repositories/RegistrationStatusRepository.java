package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.RegistrationStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationStatusRepository extends CrudRepository<RegistrationStatus, Long> {
}
