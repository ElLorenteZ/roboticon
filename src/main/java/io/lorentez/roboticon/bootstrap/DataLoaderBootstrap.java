package io.lorentez.roboticon.bootstrap;

import io.lorentez.roboticon.model.Team;
import io.lorentez.roboticon.model.University;
import io.lorentez.roboticon.repositories.TeamRepository;
import io.lorentez.roboticon.repositories.UniversityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class DataLoaderBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UniversityRepository universityRepository;
    private final TeamRepository teamRepository;

    public DataLoaderBootstrap(UniversityRepository universityRepository, TeamRepository teamRepository) {
        this.universityRepository = universityRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadStartupData();
    }

    private void loadStartupData(){
        Optional<University> aghUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("akademia górniczo-hutnicza");
        Optional<University> pkUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("politechnika krakowska");
        Optional<University> pwUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("politechnika wrocławska");
        Optional<University> psUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("politechnika śląska");
        Optional<University> watUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("wojskowa akacdemia techniczna");

        Team nephthysTeam = Team.builder()
                .name("Nephthys")
                .build();
        Team chronosTeam = Team.builder()
                .name("Chronos")
                .build();
        Team marshalTeam = Team.builder()
                .name("Marshal")
                .build();

        if (aghUniversityOptional.isPresent()){
            University aghUniversity = aghUniversityOptional.get();
            nephthysTeam.setUniversity(aghUniversity);
            chronosTeam.setUniversity(aghUniversity);
            marshalTeam.setUniversity(aghUniversity);

        }
        else {
            log.warn("Unable to find \'Akademia Górniczo-Hutnicza\' in database..");
        }


        Team berserkerTeam = Team.builder()
                .name("Berserker")
                .build();

        Team trailblazerTeam = Team.builder()
                .name("Trailblazer")
                .build();

        if (pkUniversityOptional.isPresent()) {
            University pkUniversity = pkUniversityOptional.get();
            berserkerTeam.setUniversity(pkUniversity);
            trailblazerTeam.setUniversity(pkUniversity);
        }
        else {
            log.warn("Unable to find \'Politechnika Krakowska\' in database..");
        }

        Team kantoTeam = Team.builder()
                .name("Kanto")
                .build();

        Team thaumanovaTeam = Team.builder()
                .name("Thaumanova")
                .build();

        Team sunquaTeam = Team.builder()
                .name("Sunqua")
                .build();

        if (pwUniversityOptional.isPresent()){
            University pwUniversity = pwUniversityOptional.get();
            kantoTeam.setUniversity(pwUniversity);
            thaumanovaTeam.setUniversity(pwUniversity);
            sunquaTeam.setUniversity(pwUniversity);
        }
        else {
            log.warn("Unable to find \'Politechnika Wrocławska\' in database..");
        }

        Team knrTeam = Team.builder()
                .name("Koło Naukowe Robotyków")
                .build();

        Team knsikTeam = Team.builder()
                .name("Koło Naukowe Sensoryki i Kognitywistyki")
                .build();

        if (psUniversityOptional.isPresent()){
            University psUniversity = psUniversityOptional.get();
            knrTeam.setUniversity(psUniversity);
            knsikTeam.setUniversity(psUniversity);
        }
        else {
            log.warn("Unable to find \'Politechnika Śląska\' in database..");
        }

        Team sunriseTeam = Team.builder()
                .name("Sunrise")
                .build();

        Team valhalaTeam = Team.builder()
                .name("Valhala")
                .build();

        Team shashlykTeam = Team.builder()
                .name("Sha-Shlyk Team")
                .build();

        if (watUniversityOptional.isPresent()) {
            University watUniversity = watUniversityOptional.get();
            sunriseTeam.setUniversity(watUniversity);
            valhalaTeam.setUniversity(watUniversity);
            shashlykTeam.setUniversity(watUniversity);
        }
        else {
            log.warn("Unable to find \'Wojskowa Akademia Techniczna\' in database..");
        }

        Team goquoTeam = Team.builder()
                .name("GoQuo")
                .build();

        nephthysTeam = teamRepository.save(nephthysTeam);
        chronosTeam = teamRepository.save(chronosTeam);
        marshalTeam = teamRepository.save(marshalTeam);
        berserkerTeam = teamRepository.save(berserkerTeam);
        trailblazerTeam = teamRepository.save(trailblazerTeam);
        kantoTeam = teamRepository.save(kantoTeam);
        thaumanovaTeam = teamRepository.save(thaumanovaTeam);
        sunquaTeam = teamRepository.save(sunquaTeam);
        knrTeam = teamRepository.save(knrTeam);
        knsikTeam = teamRepository.save(knsikTeam);
        sunriseTeam = teamRepository.save(sunriseTeam);
        valhalaTeam = teamRepository.save(valhalaTeam);
        shashlykTeam = teamRepository.save(shashlykTeam);
        goquoTeam = teamRepository.save(goquoTeam);
    }

}
