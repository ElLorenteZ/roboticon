package io.lorentez.roboticon.repositories;

import io.lorentez.roboticon.model.University;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
class UniversityRepositoryTest {

    @Autowired
    UniversityRepository repository;

    @Test
    void findByName() {
        //given

        //when
        List<University> universities = repository.findByNameContainingIgnoreCase("politechnika");

        //then
        assertNotNull(universities);
        assertThat(universities).isNotEmpty();
    }

    @Test
    void findByCity() {
        //given

        //when
        List<University> universities = repository.findByCityIgnoreCase("kraków");

        //then
        assertNotNull(universities);
        assertThat(universities).isNotEmpty();
    }

    @Test
    void findByCountry() {
        //given

        //when
        List<University> universities = repository.findByCountryIgnoreCase("polska");

        //then
        assertNotNull(universities);
        assertThat(universities).isNotEmpty();
    }
}