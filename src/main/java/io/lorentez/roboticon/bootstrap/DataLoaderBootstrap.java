package io.lorentez.roboticon.bootstrap;

import io.lorentez.roboticon.model.*;
import io.lorentez.roboticon.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

/*
    This class is used only to read mock data.
 */

@Profile("default")
@Slf4j
@Component
public class DataLoaderBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UniversityRepository universityRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final CompetitionTypeRepository competitionTypeRepository;
    private final RobotRepository robotRepository;

    public DataLoaderBootstrap(UniversityRepository universityRepository,
                               TeamRepository teamRepository,
                               UserRepository userRepository,
                               CompetitionTypeRepository competitionTypeRepository,
                               RobotRepository robotRepository) {
        this.universityRepository = universityRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.competitionTypeRepository = competitionTypeRepository;
        this.robotRepository = robotRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Loading mock data on application startup..");
        loadStartupUserAndTeamData();
        loadRobotsAndCompetitionTypes();
        log.info("Loading mock data finished..");
        //loadCompetitions();
    }

    private void loadStartupUserAndTeamData(){
        Optional<University> aghUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("akademia górniczo-hutnicza");
        Optional<University> pkUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("politechnika krakowska");
        Optional<University> pwUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("politechnika wrocławska");
        Optional<University> psUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("politechnika śląska");
        Optional<University> watUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("wojskowa akademia techniczna");

        User user1 = User.builder()
                .name("Janusz")
                .surname("Iksiński")
                .email("janusz.iksinski@test.pl")
                .build();

        User user2 = User.builder()
                .name("Anna")
                .surname("Kowal")
                .email("anna.kowal@test.pl")
                .build();

        User user3 = User.builder()
                .name("Tomasz")
                .surname("Chomik")
                .email("tomasz.chomik@test.pl")
                .build();

        User user4 = User.builder()
                .name("Karol")
                .surname("Żurawski")
                .email("karol.zurawski@test.pl")
                .build();

        User user5 = User.builder()
                .name("Magdalena")
                .surname("Kowalska")
                .email("magdalena.kowalska@test.pl")
                .build();

        User user6 = User.builder()
                .name("Kazimierz")
                .surname("Motyka")
                .email("kazimierz.motyka@test.pl")
                .build();

        User user7 = User.builder()
                .name("Radosław")
                .surname("Gola")
                .email("radoslaw.gola@test.pl")
                .build();

        User user8 = User.builder()
                .name("Maria")
                .surname("Nuta")
                .email("maria.nuta@test.pl")
                .build();

        User user9 = User.builder()
                .name("Katarzyna")
                .surname("Groteska")
                .email("katarzyna.groteska@test.pl")
                .build();

        User user10 = User.builder()
                .name("Natalia")
                .surname("Koryto")
                .email("natalia.koryto@test.pl")
                .build();

        User user11 = User.builder()
                .name("Lucjan")
                .surname("Anatoliński")
                .email("lucjan.anatolinski@test.pl")
                .build();

        User user12 = User.builder()
                .name("Stanisław")
                .surname("Bonkiewicz")
                .email("stanislaw.bonkiewicz@test.pl")
                .build();

        User user13 = User.builder()
                .name("Michał")
                .surname("Kuczma")
                .email("michal.kuczma@test.pl")
                .build();

        User user14 = User.builder()
                .name("Mateusz")
                .surname("Juczkiewicz")
                .email("mateusz.juczkiewicz@test.pl")
                .build();

        User user15 = User.builder()
                .name("Urszula")
                .surname("Potra")
                .email("urszula.potra@test.pl")
                .build();

        User user16 = User.builder()
                .name("Bartosz")
                .surname("Czarny")
                .email("bartosz.czarny@test.pl")
                .build();

        User user17 = User.builder()
                .name("Bartłomiej")
                .surname("Wertowski")
                .email("bartlomiej.wertowski@test.pl")
                .build();

        User user18 = User.builder()
                .name("Paweł")
                .surname("Kuwert")
                .email("pawel.kuwert@test.pl")
                .build();

        User user19 = User.builder()
                .name("Piotr")
                .surname("Intel")
                .email("piotr.intel@test.pl")
                .build();

        User user20 = User.builder()
                .name("Klaudia")
                .surname("Fasada")
                .email("klaudia.fasada@test.pl")
                .build();

        User user21 = User.builder()
                .name("Aneta")
                .surname("Adapter")
                .email("aneta.adapter@test.pl")
                .build();

        User user22 = User.builder()
                .name("Elżbieta")
                .surname("Gromada")
                .email("elżbieta.gromada@test.pl")
                .build();

        User user23 = User.builder()
                .name("Wojciech")
                .surname("Chyży")
                .email("wojciech.chyzy@test.pl")
                .build();

        User user24 = User.builder()
                .name("Wiktoria")
                .surname("Tygrys")
                .email("wiktoria.tygrys@test.pl")
                .build();

        User user25 = User.builder()
                .name("Weronika")
                .surname("Kot")
                .email("weronika.kot@test.pl")
                .build();

        User user26 = User.builder()
                .name("Małgorzata")
                .surname("Pies")
                .email("malgorzata.pies@test.pl")
                .build();

        User user27 = User.builder()
                .name("Tadeusz")
                .surname("Lew")
                .email("tadeusz.lew@test.pl")
                .build();

        User user28 = User.builder()
                .name("Iga")
                .surname("Niedźwiedź")
                .email("iga.niedzwiedz@test.pl")
                .build();

        User user29 = User.builder()
                .name("Oliwia")
                .surname("Mamba")
                .email("oliwia.mamba@test.pl")
                .build();

        User user30 = User.builder()
                .name("Justyna")
                .surname("Urbańska")
                .email("justyna.urbanska@test.pl")
                .build();

        User user31 = User.builder()
                .name("Cecylia")
                .surname("Loki")
                .email("cecylia.loki@test.pl")
                .build();

        User user32 = User.builder()
                .name("Celina")
                .surname("Zeus")
                .email("celina.zeus@test.pl")
                .build();

        User user33 = User.builder()
                .name("Lucyna")
                .surname("Hermes")
                .email("lucyna.hermes@test.pl")
                .build();

        User user34 = User.builder()
                .name("Filip")
                .surname("Buk")
                .email("filip.buk@test.pl")
                .build();

        User user35 = User.builder()
                .name("Amelia")
                .surname("Dąb")
                .email("amelia.dab@test.pl")
                .build();

        User user36 = User.builder()
                .name("Hubert")
                .surname("Szyszka")
                .email("hubert.szyszka@test.pl")
                .build();

        User user37 = User.builder()
                .name("Robert")
                .surname("Sosna")
                .email("robert.sosna@test.pl")
                .build();

        User user38 = User.builder()
                .name("Tomasz")
                .surname("Jodła")
                .email("tomasz.jodla@test.pl")
                .build();

        User user39 = User.builder()
                .name("Tadeusz")
                .surname("Stalowy")
                .email("tadeusz.stalowy@test.pl")
                .build();

        User user40 = User.builder()
                .name("Jacek")
                .surname("Banderas")
                .email("jacek.banderas@test.pl")
                .build();

        User user41 = User.builder()
                .name("Eryk")
                .surname("Browar")
                .email("eryk.browar@test.pl")
                .build();

        User user42 = User.builder()
                .name("Kacper")
                .surname("Listkiewicz")
                .email("kacper.listkiewicz@test.pl")
                .build();

        User user43 = User.builder()
                .name("Krystian")
                .surname("Barka")
                .email("krystian.barka@test.pl")
                .build();

        User user44 = User.builder()
                .name("Adam")
                .surname("Spychacz")
                .email("adam.spychacz@test.pl")
                .build();

        User user45 = User.builder()
                .name("Antoni")
                .surname("Spawacz")
                .email("antoni.spawacz@test.pl")
                .build();

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);
        user4 = userRepository.save(user4);
        user5 = userRepository.save(user5);
        user6 = userRepository.save(user6);
        user7 = userRepository.save(user7);
        user8 = userRepository.save(user8);
        user9 = userRepository.save(user9);
        user10 = userRepository.save(user10);
        user11 = userRepository.save(user11);
        user12 = userRepository.save(user12);
        user13 = userRepository.save(user13);
        user14 = userRepository.save(user14);
        user15 = userRepository.save(user15);
        user16 = userRepository.save(user16);
        user17 = userRepository.save(user17);
        user18 = userRepository.save(user18);
        user19 = userRepository.save(user19);
        user20 = userRepository.save(user20);
        user21 = userRepository.save(user21);
        user22 = userRepository.save(user22);
        user23 = userRepository.save(user23);
        user24 = userRepository.save(user24);
        user25 = userRepository.save(user25);
        user26 = userRepository.save(user26);
        user27 = userRepository.save(user27);
        user28 = userRepository.save(user28);
        user29 = userRepository.save(user29);
        user30 = userRepository.save(user30);
        user31 = userRepository.save(user31);
        user32 = userRepository.save(user32);
        user33 = userRepository.save(user33);
        user34 = userRepository.save(user34);
        user35 = userRepository.save(user35);
        user36 = userRepository.save(user36);
        user37 = userRepository.save(user37);
        user38 = userRepository.save(user38);
        user39 = userRepository.save(user39);
        user40 = userRepository.save(user40);
        user41 = userRepository.save(user41);
        user42 = userRepository.save(user42);
        user43 = userRepository.save(user43);
        user44 = userRepository.save(user44);
        user45 = userRepository.save(user45);

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


        UserTeam userTeam1 = UserTeam.builder()
                .status(UserTeamStatus.OWNER)
                .user(user1)
                .team(nephthysTeam)
                .timeAdded(LocalDateTime.now().minusDays(1))
                .build();

        UserTeam userTeam2 = UserTeam.builder()
                .status(UserTeamStatus.MEMBER)
                .user(user2)
                .team(nephthysTeam)
                .timeAdded(LocalDateTime.now().minusDays(1))
                .timeRemoved(LocalDateTime.now())
                .build();

        UserTeam userTeam3 = UserTeam.builder()
                .status(UserTeamStatus.MEMBER)
                .user(user3)
                .team(nephthysTeam)
                .timeAdded(LocalDateTime.now().minusDays(1))
                .build();

        UserTeam userTeam43 = UserTeam.builder()
                .status(UserTeamStatus.MEMBER)
                .user(user43)
                .team(nephthysTeam)
                .timeAdded(LocalDateTime.now().minusDays(3))
                .build();

        UserTeam userTeam44 = UserTeam.builder()
                .status(UserTeamStatus.MEMBER)
                .user(user44)
                .team(nephthysTeam)
                .timeAdded(LocalDateTime.now().minusDays(3))
                .build();

        UserTeam userTeam45 = UserTeam.builder()
                .status(UserTeamStatus.MEMBER)
                .user(user45)
                .team(nephthysTeam)
                .timeAdded(LocalDateTime.now().minusDays(3))
                .build();

        nephthysTeam.getUserTeams().add(userTeam45);
        nephthysTeam.getUserTeams().add(userTeam44);
        nephthysTeam.getUserTeams().add(userTeam43);
        nephthysTeam.getUserTeams().add(userTeam1);
        nephthysTeam.getUserTeams().add(userTeam2);
        nephthysTeam.getUserTeams().add(userTeam3);

        nephthysTeam = teamRepository.save(nephthysTeam);

        UserTeam userTeam4 = UserTeam.builder()
                .status(UserTeamStatus.OWNER)
                .user(user4)
                .team(chronosTeam)
                .timeAdded(LocalDateTime.now().minusDays(4))
                .build();

        UserTeam userTeam5 = UserTeam.builder()
                .status(UserTeamStatus.ADMIN)
                .user(user5)
                .team(chronosTeam)
                .timeAdded(LocalDateTime.now().minusDays(4))
                .build();

        UserTeam userTeam6 = UserTeam.builder()
                .status(UserTeamStatus.MEMBER)
                .user(user6)
                .team(chronosTeam)
                .timeAdded(LocalDateTime.now().minusDays(3))
                .build();

        chronosTeam.getUserTeams().add(userTeam4);
        chronosTeam.getUserTeams().add(userTeam5);
        chronosTeam.getUserTeams().add(userTeam6);

        chronosTeam = teamRepository.save(chronosTeam);

        addSampleUsersToTeam(user7, user8, user9, marshalTeam);
        marshalTeam = teamRepository.save(marshalTeam);

        addSampleUsersToTeam(user10, user11, user12, berserkerTeam);
        berserkerTeam = teamRepository.save(berserkerTeam);

        addSampleUsersToTeam(user13, user14, user15, trailblazerTeam);
        trailblazerTeam = teamRepository.save(trailblazerTeam);

        addSampleUsersToTeam(user16, user17, user18, kantoTeam);
        kantoTeam = teamRepository.save(kantoTeam);

        addSampleUsersToTeam(user19, user20, user21, thaumanovaTeam);
        thaumanovaTeam = teamRepository.save(thaumanovaTeam);

        addSampleUsersToTeam(user22, user23, user24, sunquaTeam);
        sunquaTeam = teamRepository.save(sunquaTeam);

        addSampleUsersToTeam(user25, user26, user27, knrTeam);
        knrTeam = teamRepository.save(knrTeam);

        addSampleUsersToTeam(user28, user29, user30, knsikTeam);
        knsikTeam = teamRepository.save(knsikTeam);

        addSampleUsersToTeam(user31, user32, user33, sunriseTeam);
        sunriseTeam = teamRepository.save(sunriseTeam);

        addSampleUsersToTeam(user34, user35, user36, valhalaTeam);
        valhalaTeam = teamRepository.save(valhalaTeam);

        addSampleUsersToTeam(user37, user38, user39, shashlykTeam);
        shashlykTeam = teamRepository.save(shashlykTeam);

        addSampleUsersToTeam(user40, user41, user42, goquoTeam);
        goquoTeam = teamRepository.save(goquoTeam);
    }

    private void addSampleUsersToTeam(User user1, User user2, User user3, Team team) {
        UserTeam userTeam1 = UserTeam.builder()
                .status(UserTeamStatus.OWNER)
                .user(user1)
                .team(team)
                .timeAdded(LocalDateTime.now().minusDays(10))
                .build();

        UserTeam userTeam2 = UserTeam.builder()
                .status(UserTeamStatus.MEMBER)
                .user(user2)
                .team(team)
                .timeAdded(LocalDateTime.now().minusDays(10))
                .build();

        UserTeam userTeam3 = UserTeam.builder()
                .status(UserTeamStatus.MEMBER)
                .user(user3)
                .team(team)
                .timeAdded(LocalDateTime.now().minusDays(10))
                .build();

        team.getUserTeams().add(userTeam1);
        team.getUserTeams().add(userTeam2);
        team.getUserTeams().add(userTeam3);

    }

    private void loadRobotsAndCompetitionTypes() {
        CompetitionType lineFollowerType = CompetitionType.builder()
                .type("Line Follower".toUpperCase(Locale.ROOT))
                .scoreType(ScoreType.MIN_TIME)
                .build();
        lineFollowerType = competitionTypeRepository.save(lineFollowerType);

        CompetitionType sumoCompetitionType = CompetitionType.builder()
                .type("Sumo".toUpperCase(Locale.ROOT))
                .scoreType(ScoreType.MAX_POINTS)
                .build();
        sumoCompetitionType = competitionTypeRepository.save(sumoCompetitionType);

        CompetitionType micromouseCompetitionType = CompetitionType.builder()
                .type("Micromouse".toUpperCase(Locale.ROOT))
                .scoreType(ScoreType.MIN_TIME)
                .build();
        micromouseCompetitionType = competitionTypeRepository.save(micromouseCompetitionType);

        CompetitionType roboSprintType = CompetitionType.builder()
                .type("Robo Sprint".toUpperCase(Locale.ROOT))
                .scoreType(ScoreType.MIN_TIME)
                .build();
        roboSprintType = competitionTypeRepository.save(roboSprintType);

        CompetitionType freestyleType = CompetitionType.builder()
                .type("Freestyle".toUpperCase(Locale.ROOT))
                .scoreType(ScoreType.MAX_POINTS)
                .build();
        freestyleType = competitionTypeRepository.save(freestyleType);

        CompetitionType raceType = CompetitionType.builder()
                .type("Robo Race".toUpperCase(Locale.ROOT))
                .scoreType(ScoreType.MIN_TIME)
                .build();
        raceType = competitionTypeRepository.save(raceType);

        CompetitionType puckCollectType = CompetitionType.builder()
                .type("Puck Collect".toUpperCase(Locale.ROOT))
                .scoreType(ScoreType.MAX_POINTS)
                .build();
        puckCollectType = competitionTypeRepository.save(puckCollectType);

        Optional<Team> nephthysTeamOptional = teamRepository.findByNameWithRobotTeams("Nephthys".toLowerCase(Locale.ROOT));
        Optional<Team> chronosTeamOptional = teamRepository.findByNameWithRobotTeams("Chronos".toLowerCase(Locale.ROOT));
        Optional<Team> marshalTeamOptional = teamRepository.findByNameWithRobotTeams("Marshal".toLowerCase(Locale.ROOT));
        Optional<Team> berserkerTeamOptional = teamRepository.findByNameWithRobotTeams("Berserker".toLowerCase(Locale.ROOT));
        Optional<Team> trailblazerTeamOptional = teamRepository.findByNameWithRobotTeams("Trailblazer".toLowerCase(Locale.ROOT));
        Optional<Team> kantoTeamOptional = teamRepository.findByNameWithRobotTeams("Kanto".toLowerCase(Locale.ROOT));
        Optional<Team> thaumanovaTeamOptional = teamRepository.findByNameWithRobotTeams("Thaumanova".toLowerCase(Locale.ROOT));
        Optional<Team> sunquaTeamOptional = teamRepository.findByNameWithRobotTeams("Sunqua".toLowerCase(Locale.ROOT));
        Optional<Team> knrTeamOptional = teamRepository.findByNameWithRobotTeams("Koło Naukowe Robotyków".toLowerCase(Locale.ROOT));
        Optional<Team> knsikTeamOptional = teamRepository.findByNameWithRobotTeams("Koło Naukowe Sensoryki".toLowerCase(Locale.ROOT));
        Optional<Team> sunriseTeamOptional = teamRepository.findByNameWithRobotTeams("Sunrise".toLowerCase(Locale.ROOT));
        Optional<Team> valhalaTeamOptional = teamRepository.findByNameWithRobotTeams("Valhala".toLowerCase(Locale.ROOT));
        Optional<Team> shashlykTeamOptional = teamRepository.findByNameWithRobotTeams("Sha-Shlyk".toLowerCase(Locale.ROOT));
        Optional<Team> goquoTeamOptional = teamRepository.findByNameWithRobotTeams("GoQuo".toLowerCase(Locale.ROOT));

        //LINE FOLLOWER
        Robot robot1 = Robot.builder()
                .name("L1n3r")
                .build();

        Robot robot2 = Robot.builder()
                .name("Niuchacz")
                .build();

        Robot robot3 = Robot.builder()
                .name("Gorky")
                .build();

        Robot robot4 = Robot.builder()
                .name("Valter")
                .build();

        Robot robot5 = Robot.builder()
                .name("Aberiusz")
                .build();

        //SUMO
        Robot robot6 = Robot.builder()
                .name("Dzik")
                .build();

        Robot robot7 = Robot.builder()
                .name("Kubik")
                .build();

        Robot robot8 = Robot.builder()
                .name("Scrappy")
                .build();

        Robot robot9 = Robot.builder()
                .name("Omtron")
                .build();

        Robot robot10 = Robot.builder()
                .name("Ezroid")
                .build();

        //MICROMOUSE
        Robot robot11 = Robot.builder()
                .name("Dustie")
                .build();

        Robot robot12 = Robot.builder()
                .name("Alpha")
                .build();

        Robot robot13 = Robot.builder()
                .name("Azerty")
                .build();

        Robot robot14 = Robot.builder()
                .name("Esupx")
                .build();

        Robot robot15 = Robot.builder()
                .name("Abonator")
                .build();

        //PUCK COLLECT
        Robot robot16 = Robot.builder()
                .name("Garbage Disposal Bot")
                .build();

        Robot robot17 = Robot.builder()
                .name("Eha")
                .build();

        Robot robot18 = Robot.builder()
                .name("Shrimp")
                .build();

        Robot robot19 = Robot.builder()
                .name("Talus")
                .build();

        Robot robot20 = Robot.builder()
                .name("Sterling")
                .build();

        //ROBO SPRINT
        Robot robot21 = Robot.builder()
                .name("Ulofroid")
                .build();

        Robot robot22 = Robot.builder()
                .name("01010010")
                .build();

        Robot robot23 = Robot.builder()
                .name("Mr Quickfoot")
                .build();

        Robot robot24 = Robot.builder()
                .name("(F)Robo Baggins")
                .build();

        Robot robot25 = Robot.builder()
                .name("Sp33d0")
                .build();

        //FREESTYLE
        Robot robot26 = Robot.builder()
                .name("TaZ")
                .build();

        Robot robot27 = Robot.builder()
                .name("Bucky")
                .build();

        Robot robot28 = Robot.builder()
                .name("Charr Lee")
                .build();

        Robot robot29 = Robot.builder()
                .name("B33p")
                .build();

        Robot robot30 = Robot.builder()
                .name("Nax")
                .build();

        //ROBO RACE
        Robot robot31 = Robot.builder()
                .name("Inferius")
                .build();

        Robot robot32 = Robot.builder()
                .name("Ingot")
                .build();

        Robot robot33 = Robot.builder()
                .name("Wheely Mk1")
                .build();

        Robot robot34 = Robot.builder()
                .name("Garbi D")
                .build();

        Robot robot35 = Robot.builder()
                .name("Zeer")
                .build();

        if (nephthysTeamOptional.isPresent()){
            Team nephthysTeam = nephthysTeamOptional.get();
            addMockRobots(nephthysTeam, robot1, robot15, robot29);
        }
        else{
            log.warn("Couldn\'t find \'Nephthys\' team in database..");
        }
        if (chronosTeamOptional.isPresent()){
            Team chronosTeam = chronosTeamOptional.get();
            addMockRobots(chronosTeam, robot2, robot16, robot30);

        }
        else {
            log.warn("Couldn't find \'Chronos\' team in database..");
        }

        if(marshalTeamOptional.isPresent()){
            Team marshalTeam = marshalTeamOptional.get();
            addMockRobots(marshalTeam, robot3, robot17, robot31);
        }
        else{
            log.warn("Couldn't find \'Marshal\' team in database..");
        }

        if (berserkerTeamOptional.isPresent()){
            Team berserkerTeam = berserkerTeamOptional.get();
            addMockRobots(berserkerTeam, robot4, robot18, robot32);
        }
        else {
            log.warn("Couldn't find \'Berserker\' team in database..");
        }

        if(trailblazerTeamOptional.isPresent()){
            Team trailblazerTeam = trailblazerTeamOptional.get();
            addMockRobots(trailblazerTeam, robot5, robot19, robot33);
        }
        else{
            log.warn("Couldn't find \'Trailblazer\' team in database..");
        }

        if(kantoTeamOptional.isPresent()){
            Team kantoTeam = kantoTeamOptional.get();
            addMockRobots(kantoTeam, robot6, robot20, robot34);
        }
        else{
            log.warn("Couldn't find \'Kanto\' team in database..");
        }

        if(thaumanovaTeamOptional.isPresent()){
            Team thaumanovaTeam = thaumanovaTeamOptional.get();
            addMockRobots(thaumanovaTeam, robot7, robot21, robot35);
        }
        else{
            log.warn("Couldn't find \'Thaumanova\' team in database..");
        }

        if(sunquaTeamOptional.isPresent()){
            Team sunquaTeam = sunquaTeamOptional.get();
            addMockRobots(sunquaTeam, robot8, robot22);
        }
        else{
            log.warn("Couldn't find \'Sunqua\' team in database..");
        }

        if(knrTeamOptional.isPresent()){
            Team knrTeam = knrTeamOptional.get();
            addMockRobots(knrTeam, robot9, robot23);
        }
        else{
            log.warn("Couldn't find \'Koło Naukowe Robotyków\' team in database..");
        }

        if(knsikTeamOptional.isPresent()){
            Team knsikTeam = knsikTeamOptional.get();
            addMockRobots(knsikTeam, robot10, robot24);
        }
        else{
            log.warn("Couldn't find \'Koło Naukowe Sensoryki\' team in database..");
        }

        if (sunriseTeamOptional.isPresent()){
            Team sunriseTeam = sunriseTeamOptional.get();
            addMockRobots(sunriseTeam, robot11, robot25);
        }
        else {
            log.warn("Couldn't find \'Sunrise\' team in database..");
        }

        if (valhalaTeamOptional.isPresent()){
            Team valhalaTeam = valhalaTeamOptional.get();
            addMockRobots(valhalaTeam, robot12, robot26);
        }
        else {
            log.warn("Couldn't find \'Valhala\' team in database..");
        }

        if (shashlykTeamOptional.isPresent()){
            Team shashlykTeam = shashlykTeamOptional.get();
            addMockRobots(shashlykTeam, robot13, robot27);
        }
        else {
            log.warn("Couldn't find \'Sha-Shlyk\' team in database..");
        }

        if (goquoTeamOptional.isPresent()){
            Team goquoTeam = goquoTeamOptional.get();
            addMockRobots(goquoTeam, robot14, robot28);
        }
        else {
            log.warn("Couldn't find \'GoQuo\' team in database..");
        }


    }
    private void addMockRobots(Team team, Robot robot1, Robot robot2) {
        addMockRobots(team, robot1, robot2, null);
    }

    private void addMockRobots(Team team, Robot robot1, Robot robot2, Robot robot3) {
        if(robot1 != null){
            robot1 = robotRepository.save(robot1);
            RobotTeam robotTeam1 = RobotTeam.builder()
                    .robot(robot1)
                    .team(team)
                    .build();
            team.getRobotTeams().add(robotTeam1);
        }

        if(robot2 != null) {
            robot2 = robotRepository.save(robot2);
            RobotTeam robotTeam2 = RobotTeam.builder()
                    .robot(robot2)
                    .team(team)
                    .build();
            team.getRobotTeams().add(robotTeam2);
        }

        if (robot3 != null){
            robot3 = robotRepository.save(robot3);
            RobotTeam robotTeam3 = RobotTeam.builder()
                    .robot(robot3)
                    .team(team)
                    .build();

            team.getRobotTeams().add(robotTeam3);
        }

        team = teamRepository.save(team);
    }


}
