package io.lorentez.roboticon.bootstrap;

import io.lorentez.roboticon.model.*;
import io.lorentez.roboticon.model.security.Authority;
import io.lorentez.roboticon.model.security.Role;
import io.lorentez.roboticon.model.security.User;
import io.lorentez.roboticon.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/*
    This class is used only to read mock data.
 */

@Profile("default")
@Slf4j
@RequiredArgsConstructor
@Component
public class DataLoaderBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final RegistrationStatusRepository registrationStatusRepository;
    private final RegistrationRepository registrationRepository;
    private final UniversityRepository universityRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final CompetitionTypeRepository competitionTypeRepository;
    private final TournamentRepository tournamentRepository;
    private final RobotRepository robotRepository;
    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("Loading mock data on application startup..");
        loadStartupUserAndTeamData();
        loadRobots();
        loadTournamentsAndCompetitions();
        loadRegistrations();
        log.info("Loading mock data finished..");
    }

    private void loadStartupUserAndTeamData(){
        Optional<University> aghUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("akademia g??rniczo-hutnicza");
        Optional<University> pkUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("politechnika krakowska");
        Optional<University> pwUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("politechnika wroc??awska");
        Optional<University> psUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("politechnika ??l??ska");
        Optional<University> watUniversityOptional = universityRepository
                .findByNameContainingIgnoreCase("wojskowa akademia techniczna");

        Authority createTournamentAuthority = Authority.builder()
                .permission("tournament.create")
                .description("Permission to create new tournament.")
                .build();
        Authority updateTournamentAuthority = Authority.builder()
                .permission("tournament.update")
                .description("Permission to update tournament data.")
                .build();
        Authority createTeamAuthority = Authority.builder()
                .permission("team.create")
                .description("Permission to create teams.")
                .build();
        Authority adminUpdateTeamAuthority = Authority.builder()
                .permission("admin.team.update")
                .description("Permission to update any team data.")
                .build();
        Authority adminReadTeamAuthority = Authority.builder()
                .permission("admin.team.read")
                .description("Permission to read any team data.")
                .build();
        Authority adminInviteTeamAuthority = Authority.builder()
                .permission("admin.team.invite")
                .description("Permission to invite any user to any team.")
                .build();
        Authority adminChangeUserStatusAuthority = Authority.builder()
                .permission("admin.team.user.status")
                .description("Permission to change internal status of any user in team.")
                .build();
        Authority adminViewTeamAuthority = Authority.builder()
                .permission("admin.team.view")
                .description("Permission to view details of any team.")
                .build();
        Authority adminChangeRobotDetailsAuthority = Authority.builder()
                .permission("admin.robot.edit")
                .description("Permission to change details of any robot.")
                .build();
        Authority adminListRobotsAuthority = Authority.builder()
                .permission("admin.robot.list")
                .description("Permission to list all robots.")
                .build();
        Authority adminRobotTransferAuthority = Authority.builder()
                .permission("admin.robot.transfer")
                .description("Permission to request transfer of any robot.")
                .build();
        Authority adminRobotTransferAcceptAuthority = Authority.builder()
                .permission("admin.robot.transfer.accept")
                .description("Permission to request accept transfer of any robot.")
                .build();
        Authority adminUserEditAuthority = Authority.builder()
                .permission("admin.user.edit")
                .description("Permission to edit any user details.")
                .build();
        Authority adminTeamEditAuthority = Authority.builder()
                .permission("admin.team.edit")
                .description("Permission to update any team's details.")
                .build();
        Authority adminCreateRegistrationAuthority = Authority.builder()
                .permission("admin.registration.create")
                .description("Permission to create registration for any team.")
                .build();
        Authority adminRegistrationStatusUpdateAuthority = Authority.builder()
                .permission("admin.registration.status.update")
                .description("Permission to change registration status.")
                .build();
        Authority adminEditRegistrationAuthority = Authority.builder()
                .permission("admin.registration.edit")
                .description("Permission to edit any registration.")
                .build();
        Authority adminViewTeamRegistrationAuthority = Authority.builder()
                .permission("admin.registration.team.view")
                .description("Permission to preview any team registration.")
                .build();
        Authority adminUniversityManageAuthority = Authority.builder()
                .permission("admin.university.manage")
                .description("Permission to manage universities' data.")
                .build();
        Authority adminUsersListAuthority = Authority.builder()
                .permission("admin.users.view")
                .description("Permission to list users' data.")
                .build();
        Authority adminTournamentCreateAuthority = Authority.builder()
                .permission("admin.tournament.create")
                .description("Permission to create new tournament.")
                .build();
        Authority adminTournamentEditAuthority = Authority.builder()
                .permission("admin.tournament.edit")
                .description("Permission to edit any tournament's data.")
                .build();
        Authority adminRobotViewAuthority = Authority.builder()
                .permission("admin.robot.view")
                .description("Permission to view any robot.")
                .build();
        Authority adminRobotAddAuthority = Authority.builder()
                .permission("admin.robot.add")
                .description("Permission to add robot to any team.")
                .build();
        Authority adminUsersRegistrationViewAuthority = Authority.builder()
                .permission("admin.registration.users")
                .description("Permission to check any user's registrations.")
                .build();
        Authority adminUserChangePasswordAuthority = Authority.builder()
                .permission("admin.user.password.change")
                .description("Permission to change any user's password.")
                .build();
        Authority adminTeamUserViewAuthority = Authority.builder()
                .permission("admin.teams.user.view")
                .description("Permission to view any user's teams.")
                .build();

        Authority userUpdateTeamAuthority = Authority.builder()
                .permission("user.team.update")
                .description("Permission to update team data where user has status of owner or admin.")
                .build();
        Authority userInviteTeamAuthority = Authority.builder()
                .permission("user.team.invite")
                .description("Permission to invite any user to team where user has status owner or admin.")
                .build();
        Authority userReadTeamAuthority = Authority.builder()
                .permission("user.team.read")
                .description("Permission to read team data where user has status of owner or admin.")
                .build();
        Authority userChangeUserStatusAuthority = Authority.builder()
                .permission("user.team.user.status")
                .description("Permission to change status of user in team.")
                .build();
        Authority userViewTeamAuthority = Authority.builder()
                .permission("user.team.view")
                .description("Permission to view details of team in which user has status of \'member\', \'admin\' or \'owner\'.")
                .build();
        Authority userChangeRobotDetails = Authority.builder()
                .permission("user.robot.edit")
                .description("Permission to change details of robot in team if user has Admin or Owner status.")
                .build();
        Authority userRobotTransferAuthority = Authority.builder()
                .permission("user.robot.transfer")
                .description("Permission to transfer robot is user has status OWNER in team.")
                .build();
        Authority userRobotTransferAcceptAuthority = Authority.builder()
                .permission("user.robot.transfer.accept")
                .description("Permission to accept transfer robot to user's team when he has OWNER status.")
                .build();
        Authority userUserEditAuthority = Authority.builder()
                .permission("user.user.edit")
                .description("Permission to edit user self details.")
                .build();
        Authority userUserPasswordChangeAuthority = Authority.builder()
                .permission("user.user.password.change")
                .description("Permission to change self password.")
                .build();
        Authority userTeamEditAuthority = Authority.builder()
                .permission("user.team.edit")
                .description("Permission to edit team's data.")
                .build();
        Authority userRegistrationCreateAuthority = Authority.builder()
                .permission("user.registration.create")
                .description("Permission to create registration for team where user has status of ADMIN or OWNER.")
                .build();
        Authority userRegistrationEditAuthority = Authority.builder()
                .permission("user.registration.edit")
                .description("Permission to edit registration for team where user has status of ADMIN or OWNER.")
                .build();
        Authority userViewTeamRegistrationAuthority = Authority.builder()
                .permission("user.registration.team.view")
                .description("Permission to preview registration of team in which user has status of OWNER, ADMIN or member.")
                .build();
        Authority userRobotViewAuthority = Authority.builder()
                .permission("user.robot.view")
                .description("Permission to view robots in team where user has status of OWNER or ADMIN.")
                .build();
        Authority userRobotAddAuthority = Authority.builder()
                .permission("user.robot.add")
                .description("Permission to add user to team where user has status of OWNER or ADMIN.")
                .build();
        Authority userUsersRegistrationViewAuthority = Authority.builder()
                .permission("user.registration.users")
                .description("Permission to check user's own registrations.")
                .build();
        Authority userTeamsUserViewAuthority = Authority.builder()
                .permission("user.teams.user.view")
                .description("Permission to check user's teams.")
                .build();

        createTournamentAuthority = authorityRepository.save(createTournamentAuthority);
        updateTournamentAuthority = authorityRepository.save(updateTournamentAuthority);
        createTeamAuthority = authorityRepository.save(createTeamAuthority);
        adminUpdateTeamAuthority = authorityRepository.save(adminUpdateTeamAuthority);
        adminReadTeamAuthority = authorityRepository.save(adminReadTeamAuthority);
        adminInviteTeamAuthority = authorityRepository.save(adminInviteTeamAuthority);
        adminChangeUserStatusAuthority = authorityRepository.save(adminChangeUserStatusAuthority);
        adminViewTeamAuthority = authorityRepository.save(adminViewTeamAuthority);
        adminChangeRobotDetailsAuthority = authorityRepository.save(adminChangeRobotDetailsAuthority);
        adminListRobotsAuthority = authorityRepository.save(adminListRobotsAuthority);
        adminRobotTransferAuthority = authorityRepository.save(adminRobotTransferAuthority);
        adminRobotTransferAcceptAuthority = authorityRepository.save(adminRobotTransferAcceptAuthority);
        adminUserEditAuthority = authorityRepository.save(adminUserEditAuthority);
        adminTeamEditAuthority = authorityRepository.save(adminTeamEditAuthority);
        adminViewTeamRegistrationAuthority = authorityRepository.save(adminViewTeamRegistrationAuthority);
        adminCreateRegistrationAuthority = authorityRepository.save(adminCreateRegistrationAuthority);
        adminEditRegistrationAuthority = authorityRepository.save(adminEditRegistrationAuthority);
        adminRegistrationStatusUpdateAuthority = authorityRepository.save(adminRegistrationStatusUpdateAuthority);
        adminUniversityManageAuthority = authorityRepository.save(adminUniversityManageAuthority);
        adminUsersListAuthority = authorityRepository.save(adminUsersListAuthority);
        adminTournamentCreateAuthority = authorityRepository.save(adminTournamentCreateAuthority);
        adminTournamentEditAuthority = authorityRepository.save(adminTournamentEditAuthority);
        adminRobotAddAuthority = authorityRepository.save(adminRobotAddAuthority);
        adminRobotViewAuthority = authorityRepository.save(adminRobotViewAuthority);
        adminUsersRegistrationViewAuthority = authorityRepository.save(adminUsersRegistrationViewAuthority);
        adminUserChangePasswordAuthority = authorityRepository.save(adminUserChangePasswordAuthority);
        adminTeamUserViewAuthority = authorityRepository.save(adminTeamUserViewAuthority);

        userReadTeamAuthority = authorityRepository.save(userReadTeamAuthority);
        userInviteTeamAuthority = authorityRepository.save(userInviteTeamAuthority);
        userUpdateTeamAuthority = authorityRepository.save(userUpdateTeamAuthority);
        userChangeUserStatusAuthority = authorityRepository.save(userChangeUserStatusAuthority);
        userViewTeamAuthority = authorityRepository.save(userViewTeamAuthority);
        userChangeRobotDetails = authorityRepository.save(userChangeRobotDetails);
        userRobotTransferAuthority = authorityRepository.save(userRobotTransferAuthority);
        userRobotTransferAcceptAuthority = authorityRepository.save(userRobotTransferAcceptAuthority);
        userUserEditAuthority = authorityRepository.save(userUserEditAuthority);
        userUserPasswordChangeAuthority = authorityRepository.save(userUserPasswordChangeAuthority);
        userTeamEditAuthority = authorityRepository.save(userTeamEditAuthority);
        userViewTeamRegistrationAuthority = authorityRepository.save(userViewTeamRegistrationAuthority);
        userRegistrationCreateAuthority = authorityRepository.save(userRegistrationCreateAuthority);
        userRegistrationEditAuthority = authorityRepository.save(userRegistrationEditAuthority);
        userRobotAddAuthority = authorityRepository.save(userRobotAddAuthority);
        userRobotViewAuthority = authorityRepository.save(userRobotViewAuthority);
        userUsersRegistrationViewAuthority = authorityRepository.save(userUsersRegistrationViewAuthority);
        userTeamsUserViewAuthority = authorityRepository.save(userTeamsUserViewAuthority);

        Role adminRole = Role.builder()
                .name("ADMIN")
                .build();

        adminRole.grantAuthority(createTournamentAuthority);
        adminRole.grantAuthority(updateTournamentAuthority);
        adminRole.grantAuthority(createTeamAuthority);
        adminRole.grantAuthority(adminUpdateTeamAuthority);
        adminRole.grantAuthority(adminReadTeamAuthority);
        adminRole.grantAuthority(adminInviteTeamAuthority);
        adminRole.grantAuthority(adminChangeUserStatusAuthority);
        adminRole.grantAuthority(adminViewTeamAuthority);
        adminRole.grantAuthority(adminChangeRobotDetailsAuthority);
        adminRole.grantAuthority(adminListRobotsAuthority);
        adminRole.grantAuthority(adminRobotTransferAuthority);
        adminRole.grantAuthority(adminRobotTransferAcceptAuthority);
        adminRole.grantAuthority(adminUserEditAuthority);
        adminRole.grantAuthority(adminTeamEditAuthority);
        adminRole.grantAuthority(adminViewTeamRegistrationAuthority);
        adminRole.grantAuthority(adminCreateRegistrationAuthority);
        adminRole.grantAuthority(adminEditRegistrationAuthority);
        adminRole.grantAuthority(adminRegistrationStatusUpdateAuthority);
        adminRole.grantAuthority(adminUniversityManageAuthority);
        adminRole.grantAuthority(adminUsersListAuthority);
        adminRole.grantAuthority(adminTournamentCreateAuthority);
        adminRole.grantAuthority(adminTournamentEditAuthority);
        adminRole.grantAuthority(adminRobotAddAuthority);
        adminRole.grantAuthority(adminRobotViewAuthority);
        adminRole.grantAuthority(adminUsersRegistrationViewAuthority);
        adminRole.grantAuthority(adminUserChangePasswordAuthority);
        adminRole.grantAuthority(adminTeamUserViewAuthority);
        adminRole = roleRepository.save(adminRole);


        Role userRole = Role.builder()
                .name("USER")
                .build();

        userRole.grantAuthority(createTeamAuthority);
        userRole.grantAuthority(userUpdateTeamAuthority);
        userRole.grantAuthority(userReadTeamAuthority);
        userRole.grantAuthority(userInviteTeamAuthority);
        userRole.grantAuthority(userChangeUserStatusAuthority);
        userRole.grantAuthority(userViewTeamAuthority);
        userRole.grantAuthority(userChangeRobotDetails);
        userRole.grantAuthority(userRobotTransferAuthority);
        userRole.grantAuthority(userRobotTransferAcceptAuthority);
        userRole.grantAuthority(userUserEditAuthority);
        userRole.grantAuthority(userUserPasswordChangeAuthority);
        userRole.grantAuthority(userTeamEditAuthority);
        userRole.grantAuthority(userViewTeamRegistrationAuthority);
        userRole.grantAuthority(userRegistrationCreateAuthority);
        userRole.grantAuthority(userRegistrationEditAuthority);
        userRole.grantAuthority(userRobotAddAuthority);
        userRole.grantAuthority(userRobotViewAuthority);
        userRole.grantAuthority(userUsersRegistrationViewAuthority);
        userRole.grantAuthority(userTeamsUserViewAuthority);
        userRole = roleRepository.save(userRole);

        User user1 = User.builder()
                .name("Janusz")
                .surname("Iksi??ski")
                .email("janusz.iksinski@test.pl")
                .password("{noop}testtest")
                .build();
        user1.grantRole(userRole);

        User user2 = User.builder()
                .name("Anna")
                .surname("Kowal")
                .email("anna.kowal@test.pl")
                .password("{noop}testtest")
                .accountNonExpired(Boolean.FALSE)
                .build();
        user2.grantRole(userRole);

        User user3 = User.builder()
                .name("Tomasz")
                .surname("Chomik")
                .email("tomasz.chomik@test.pl")
                .password("{noop}testtest")
                .build();
        user3.grantRole(userRole);

        User user4 = User.builder()
                .name("Karol")
                .surname("??urawski")
                .email("karol.zurawski@test.pl")
                .password("{noop}testtest")
                .build();
        user4.grantRole(userRole);

        User user5 = User.builder()
                .name("Magdalena")
                .surname("Kowalska")
                .email("magdalena.kowalska@test.pl")
                .password("{noop}testtest")
                .build();
        user5.grantRole(userRole);

        User user6 = User.builder()
                .name("Kazimierz")
                .surname("Motyka")
                .email("kazimierz.motyka@test.pl")
                .password("{noop}testtest")
                .build();
        user6.grantRole(userRole);

        User user7 = User.builder()
                .name("Rados??aw")
                .surname("Gola")
                .email("radoslaw.gola@test.pl")
                .password("{noop}testtest")
                .build();
        user7.grantRole(userRole);

        User user8 = User.builder()
                .name("Maria")
                .surname("Nuta")
                .email("maria.nuta@test.pl")
                .password("{noop}testtest")
                .build();
        user8.grantRole(userRole);

        User user9 = User.builder()
                .name("Katarzyna")
                .surname("Groteska")
                .email("katarzyna.groteska@test.pl")
                .password("{noop}testtest")
                .build();
        user9.grantRole(userRole);

        User user10 = User.builder()
                .name("Natalia")
                .surname("Koryto")
                .email("natalia.koryto@test.pl")
                .password("{noop}testtest")
                .build();
        user10.grantRole(userRole);

        User user11 = User.builder()
                .name("Lucjan")
                .surname("Anatoli??ski")
                .email("lucjan.anatolinski@test.pl")
                .password("{noop}testtest")
                .build();
        user11.grantRole(userRole);

        User user12 = User.builder()
                .name("Stanis??aw")
                .surname("Bonkiewicz")
                .email("stanislaw.bonkiewicz@test.pl")
                .password("{noop}testtest")
                .build();
        user12.grantRole(userRole);

        User user13 = User.builder()
                .name("Micha??")
                .surname("Kuczma")
                .email("michal.kuczma@test.pl")
                .password("{noop}testtest")
                .build();
        user13.grantRole(userRole);

        User user14 = User.builder()
                .name("Mateusz")
                .surname("Juczkiewicz")
                .email("mateusz.juczkiewicz@test.pl")
                .password("{noop}testtest")
                .build();
        user14.grantRole(userRole);

        User user15 = User.builder()
                .name("Urszula")
                .surname("Potra")
                .email("urszula.potra@test.pl")
                .password("{noop}testtest")
                .build();
        user15.grantRole(userRole);

        User user16 = User.builder()
                .name("Bartosz")
                .surname("Czarny")
                .email("bartosz.czarny@test.pl")
                .password("{noop}testtest")
                .build();
        user16.grantRole(userRole);

        User user17 = User.builder()
                .name("Bart??omiej")
                .surname("Wertowski")
                .email("bartlomiej.wertowski@test.pl")
                .password("{noop}testtest")
                .build();
        user17.grantRole(userRole);

        User user18 = User.builder()
                .name("Pawe??")
                .surname("Kuwert")
                .email("pawel.kuwert@test.pl")
                .password("{noop}testtest")
                .build();
        user18.grantRole(userRole);

        User user19 = User.builder()
                .name("Piotr")
                .surname("Intel")
                .email("piotr.intel@test.pl")
                .password("{noop}testtest")
                .build();
        user19.grantRole(userRole);

        User user20 = User.builder()
                .name("Klaudia")
                .surname("Fasada")
                .email("klaudia.fasada@test.pl")
                .password("{noop}testtest")
                .build();
        user20.grantRole(userRole);

        User user21 = User.builder()
                .name("Aneta")
                .surname("Adapter")
                .email("aneta.adapter@test.pl")
                .password("{noop}testtest")
                .build();
        user21.grantRole(userRole);

        User user22 = User.builder()
                .name("El??bieta")
                .surname("Gromada")
                .email("el??bieta.gromada@test.pl")
                .password("{noop}testtest")
                .build();
        user22.grantRole(userRole);

        User user23 = User.builder()
                .name("Wojciech")
                .surname("Chy??y")
                .email("wojciech.chyzy@test.pl")
                .password("{noop}testtest")
                .build();
        user23.grantRole(userRole);

        User user24 = User.builder()
                .name("Wiktoria")
                .surname("Tygrys")
                .email("wiktoria.tygrys@test.pl")
                .password("{noop}testtest")
                .build();
        user24.grantRole(userRole);

        User user25 = User.builder()
                .name("Weronika")
                .surname("Kot")
                .email("weronika.kot@test.pl")
                .password("{noop}testtest")
                .build();
        user25.grantRole(userRole);

        User user26 = User.builder()
                .name("Ma??gorzata")
                .surname("Pies")
                .email("malgorzata.pies@test.pl")
                .password("{noop}testtest")
                .build();
        user26.grantRole(userRole);

        User user27 = User.builder()
                .name("Tadeusz")
                .surname("Lew")
                .email("tadeusz.lew@test.pl")
                .password("{noop}testtest")
                .build();
        user27.grantRole(userRole);

        User user28 = User.builder()
                .name("Iga")
                .surname("Nied??wied??")
                .email("iga.niedzwiedz@test.pl")
                .password("{noop}testtest")
                .build();
        user28.grantRole(userRole);

        User user29 = User.builder()
                .name("Oliwia")
                .surname("Mamba")
                .email("oliwia.mamba@test.pl")
                .password("{noop}testtest")
                .build();
        user29.grantRole(userRole);

        User user30 = User.builder()
                .name("Justyna")
                .surname("Urba??ska")
                .email("justyna.urbanska@test.pl")
                .password("{noop}testtest")
                .build();
        user30.grantRole(userRole);

        User user31 = User.builder()
                .name("Cecylia")
                .surname("Loki")
                .email("cecylia.loki@test.pl")
                .password("{noop}testtest")
                .build();
        user31.grantRole(userRole);

        User user32 = User.builder()
                .name("Celina")
                .surname("Zeus")
                .email("celina.zeus@test.pl")
                .password("{noop}testtest")
                .build();
        user32.grantRole(userRole);

        User user33 = User.builder()
                .name("Lucyna")
                .surname("Hermes")
                .email("lucyna.hermes@test.pl")
                .password("{noop}testtest")
                .build();
        user33.grantRole(userRole);

        User user34 = User.builder()
                .name("Filip")
                .surname("Buk")
                .email("filip.buk@test.pl")
                .password("{noop}testtest")
                .build();
        user34.grantRole(userRole);

        User user35 = User.builder()
                .name("Amelia")
                .surname("D??b")
                .email("amelia.dab@test.pl")
                .password("{noop}testtest")
                .build();
        user35.grantRole(userRole);

        User user36 = User.builder()
                .name("Hubert")
                .surname("Szyszka")
                .email("hubert.szyszka@test.pl")
                .password("{noop}testtest")
                .build();
        user36.grantRole(userRole);

        User user37 = User.builder()
                .name("Robert")
                .surname("Sosna")
                .email("robert.sosna@test.pl")
                .password("{noop}testtest")
                .build();
        user37.grantRole(userRole);

        User user38 = User.builder()
                .name("Tomasz")
                .surname("Jod??a")
                .email("tomasz.jodla@test.pl")
                .password("{noop}testtest")
                .build();
        user38.grantRole(userRole);

        User user39 = User.builder()
                .name("Tadeusz")
                .surname("Stalowy")
                .email("tadeusz.stalowy@test.pl")
                .password("{noop}testtest")
                .build();
        user39.grantRole(userRole);

        User user40 = User.builder()
                .name("Jacek")
                .surname("Banderas")
                .email("jacek.banderas@test.pl")
                .password("{noop}testtest")
                .build();
        user40.grantRole(userRole);

        User user41 = User.builder()
                .name("Eryk")
                .surname("Browar")
                .email("eryk.browar@test.pl")
                .password("{noop}testtest")
                .build();
        user41.grantRole(userRole);

        User user42 = User.builder()
                .name("Kacper")
                .surname("Listkiewicz")
                .email("kacper.listkiewicz@test.pl")
                .password("{noop}testtest")
                .build();
        user42.grantRole(userRole);

        User user43 = User.builder()
                .name("Krystian")
                .surname("Barka")
                .email("krystian.barka@test.pl")
                .password("{noop}testtest")
                .build();
        user43.grantRole(userRole);

        User user44 = User.builder()
                .name("Adam")
                .surname("Spychacz")
                .email("adam.spychacz@test.pl")
                .password("{noop}testtest")
                .build();
        user44.grantRole(userRole);

        User user45 = User.builder()
                .name("Antoni")
                .surname("Spawacz")
                .email("antoni.spawacz@test.pl")
                .password("{noop}testtest")
                .build();
        user45.grantRole(userRole);


        User adminUser = User.builder()
                .name("Mr")
                .surname("Administrator")
                .email("admin@test.pl")
                .password("{noop}testtest")
                .build();
        adminUser.grantRole(adminRole);

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

        adminUser = userRepository.save(adminUser);

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
            log.warn("Unable to find \'Akademia G??rniczo-Hutnicza\' in database..");
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
            log.warn("Unable to find \'Politechnika Wroc??awska\' in database..");
        }

        Team knrTeam = Team.builder()
                .name("Ko??o Naukowe Robotyk??w")
                .build();

        Team knsikTeam = Team.builder()
                .name("Ko??o Naukowe Sensoryki i Kognitywistyki")
                .build();

        if (psUniversityOptional.isPresent()){
            University psUniversity = psUniversityOptional.get();
            knrTeam.setUniversity(psUniversity);
            knsikTeam.setUniversity(psUniversity);
        }
        else {
            log.warn("Unable to find \'Politechnika ??l??ska\' in database..");
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

        UserTeam userTeam1_inv4 = UserTeam.builder()
                .status(UserTeamStatus.INVITED)
                .user(user4)
                .team(nephthysTeam)
                .timeAdded(LocalDateTime.now().minusDays(1))
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
                .status(UserTeamStatus.ADMIN)
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
        nephthysTeam.getUserTeams().add(userTeam1_inv4);

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
                .status(UserTeamStatus.ADMIN)
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

    private void loadRobots() {
        Optional<Team> nephthysTeamOptional = teamRepository.findByNameWithRobotTeams("Nephthys".toLowerCase(Locale.ROOT));
        Optional<Team> chronosTeamOptional = teamRepository.findByNameWithRobotTeams("Chronos".toLowerCase(Locale.ROOT));
        Optional<Team> marshalTeamOptional = teamRepository.findByNameWithRobotTeams("Marshal".toLowerCase(Locale.ROOT));
        Optional<Team> berserkerTeamOptional = teamRepository.findByNameWithRobotTeams("Berserker".toLowerCase(Locale.ROOT));
        Optional<Team> trailblazerTeamOptional = teamRepository.findByNameWithRobotTeams("Trailblazer".toLowerCase(Locale.ROOT));
        Optional<Team> kantoTeamOptional = teamRepository.findByNameWithRobotTeams("Kanto".toLowerCase(Locale.ROOT));
        Optional<Team> thaumanovaTeamOptional = teamRepository.findByNameWithRobotTeams("Thaumanova".toLowerCase(Locale.ROOT));
        Optional<Team> sunquaTeamOptional = teamRepository.findByNameWithRobotTeams("Sunqua".toLowerCase(Locale.ROOT));
        Optional<Team> knrTeamOptional = teamRepository.findByNameWithRobotTeams("Ko??o Naukowe Robotyk??w".toLowerCase(Locale.ROOT));
        Optional<Team> knsikTeamOptional = teamRepository.findByNameWithRobotTeams("Ko??o Naukowe Sensoryki".toLowerCase(Locale.ROOT));
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

            robot1 = robotRepository.save(robot1);
            RobotTeam robotTeam1 = RobotTeam.builder()
                    .robot(robot1)
                    .team(nephthysTeam)
                    .build();
            nephthysTeam.getRobotTeams().add(robotTeam1);


            robot15 = robotRepository.save(robot15);
            RobotTeam robotTeam15 = RobotTeam.builder()
                    .robot(robot15)
                    .team(nephthysTeam)
                    .build();
            nephthysTeam.getRobotTeams().add(robotTeam15);


            robot29 = robotRepository.save(robot29);
            RobotTeam robotTeam29 = RobotTeam.builder()
                    .robot(robot29)
                    .team(nephthysTeam)
                    .build();

            nephthysTeam.getRobotTeams().add(robotTeam29);

            robot5 = robotRepository.save(robot5);
            RobotTeam robotTeam5pending = RobotTeam.builder()
                    .robot(robot5)
                    .team(nephthysTeam)
                    .status(RobotTeamStatus.PENDING)
                    .build();
            nephthysTeam.getRobotTeams().add(robotTeam5pending);

            nephthysTeam = teamRepository.save(nephthysTeam);

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
            addMockRobots(trailblazerTeam, robot19, robot33);

            robot5 = robotRepository.save(robot5);
            RobotTeam robotTeam5sent = RobotTeam.builder()
                    .robot(robot5)
                    .team(trailblazerTeam)
                    .status(RobotTeamStatus.SENT)
                    .build();
            trailblazerTeam.getRobotTeams().add(robotTeam5sent);
            trailblazerTeam = teamRepository.save(trailblazerTeam);
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
            log.warn("Couldn't find \'Ko??o Naukowe Robotyk??w\' team in database..");
        }

        if(knsikTeamOptional.isPresent()){
            Team knsikTeam = knsikTeamOptional.get();
            addMockRobots(knsikTeam, robot10, robot24);
        }
        else{
            log.warn("Couldn't find \'Ko??o Naukowe Sensoryki\' team in database..");
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

    private void loadTournamentsAndCompetitions(){
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


        Tournament roboticonTournament = Tournament.builder()
                .name("Roboticon")
                .dateStart(LocalDate.now().plusMonths(1))
                .dateEnd(LocalDate.now().plusMonths(1).plusDays(1))
                .build();

        Competition competitionRoboticonStandardSumo = Competition.builder()
                .name("Sumo Standard")
                .description("Sumo - dwa roboty pr??buj?? zepchn???? si?? nawzajem z ringu (dohyo). " +
                        "Podczas walki nie mo??na sterowa?? robotem (poza w????czeniem i wy????czeniem)." +
                        "Wielko???? robota nie mo??e przekroczy?? 20 x 20cm (wysoko???? bez ogranicze??), " +
                        "waga do 3kg. Ring ma ??rednic?? 149cm.")
                .competitionType(sumoCompetitionType)
                .tournament(roboticonTournament)
                .build();
        roboticonTournament.getCompetitions().add(competitionRoboticonStandardSumo);

        Competition competitionRoboticonLineFollower = Competition.builder()
                .name("Line Follower Enchanced")
                .description("Kategoria Line Follower Enhanced (L.F.E.) to w pe??ni autonomiczne roboty mobilne, " +
                        "zdolne do szybkiego poruszania si?? po kontrastuj??cej z t??em linii oraz omijania " +
                        "znajduj??cych si?? na torze przeszk??d. ")
                .competitionType(lineFollowerType)
                .tournament(roboticonTournament)
                .build();
        roboticonTournament.getCompetitions().add(competitionRoboticonLineFollower);

        Competition competitionRoboticonFreestyle = Competition.builder()
                .name("Robot Freestyle")
                .description("Celem konkurencji jest prezentacja najciekawszych pomys????w. " +
                        "Miejsca zostaj?? przydzielone na podstawie punkt??w przydzielonych przez jury. " +
                        "Opr??cz miejsc przydzielane s?? r??wnie?? nagrody specjalne np. nagroda od publiczon??ci.")
                .competitionType(freestyleType)
                .tournament(roboticonTournament)
                .build();
        roboticonTournament.getCompetitions().add(competitionRoboticonFreestyle);

        Competition competitionRoboticonMicromouse = Competition.builder()
                .name("Micromouse")
                .description("Micromouse ??? rodzaj zawod??w robot??w, w kt??rych robot 'mysz' " +
                        "ma do pokonania labirynt " +
                        "z??o??ony z p??askiej powierzchni i u??o??onych pojedynczych ??cianek. " +
                        "Klasyczny labirynt jest u??o??ony w kwadracie powsta??ym po po????czaniu " +
                        "16 ??cianek na wszystkich jego bokach, " +
                        "gdzie ka??da z nich ma 180 mm d??ugo??ci i 50 mm wysoko??ci.")
                .competitionType(micromouseCompetitionType)
                .tournament(roboticonTournament)
                .build();
        roboticonTournament.getCompetitions().add(competitionRoboticonMicromouse);
        roboticonTournament = tournamentRepository.save(roboticonTournament);

        Tournament roboCupTournament = Tournament.builder()
                .name("Robo Cup")
                .dateStart(LocalDate.now().minusDays(2))
                .dateEnd(LocalDate.now().minusDays(2))
                .build();

        Competition competitionRoboCupSprint = Competition.builder()
                .name("Robo Sprint")
                .description("Specjalne zawody przeznaczone dla wszelkiego rodzaju robot??w krocz??cych. " +
                        "Podczas zawod??w liczy si?? jak najszybsze przebiegni??cie przez robota wskazanego dystansu.")
                .competitionType(roboSprintType)
                .tournament(roboCupTournament)
                .build();
        roboCupTournament.getCompetitions().add(competitionRoboCupSprint);

        Competition competitionRoboCupRaceTime = Competition.builder()
                .name("F1 Tenth")
                .description("Zawody przeznaczone dla modeli pojazd??w samochodowych. " +
                        "Celem jest pokonanie jak najwi??kszej liczby okr????e?? we wskazanym czasie.")
                .competitionType(raceType)
                .tournament(roboCupTournament)
                .build();
        roboCupTournament.getCompetitions().add(competitionRoboCupRaceTime);

        Competition competitionRoboCupRace = Competition.builder()
                .name("F1 Tenth Race")
                .description("Zawody przeznaczone dla modeli pojazd??w samochodowych. " +
                        "Wy??cig, podczas kt??rego na torze ??cigaj?? si?? jednocze??nie autonomiczne roboty. " +
                        "Wygrywa ten kto pierwszy minie lini?? mety.")
                .competitionType(raceType)
                .tournament(roboCupTournament)
                .build();
        roboCupTournament.getCompetitions().add(competitionRoboCupRace);

        Competition competitionRoboCupMicromouse = Competition.builder()
                .name("Micromouse")
                .description("Zawody, podzczas kt??rych liczy si?? najszybsze pokonanie labirytnu " +
                        "przez robo mysz.")
                .competitionType(micromouseCompetitionType)
                .tournament(roboCupTournament)
                .build();
        roboCupTournament.getCompetitions().add(competitionRoboCupMicromouse);
        roboCupTournament = tournamentRepository.save(roboCupTournament);

        Tournament platiniumPantherTournament = Tournament.builder()
                .name("Platinium Pather")
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(1))
                .build();

        Competition competitionPlatiniumPantherSumo = Competition.builder()
                .name("Standard Sumo")
                .description("Pojedynek robot??w niczym japo??skich wojownik??w sumo.")
                .competitionType(sumoCompetitionType)
                .tournament(platiniumPantherTournament)
                .build();
        platiniumPantherTournament.getCompetitions().add(competitionPlatiniumPantherSumo);

        Competition competitionPlatiniumPantherLineFollower = Competition.builder()
                .name("Line Follower Turbo")
                .description("Zawody typu line follower dopuszczaj??ce pojazdy z turbin??.")
                .competitionType(lineFollowerType)
                .tournament(platiniumPantherTournament)
                .build();
        platiniumPantherTournament.getCompetitions().add(competitionPlatiniumPantherLineFollower);

        Competition competitionPlatiniumPantherPuckCollect = Competition.builder()
                .name("Puck collect")
                .description("Zawody robot??w typu puck collect. Celem jest zebranie jak najwi??kszej liczby kr????k??w " +
                        "w okre??lonym kolorze.")
                .competitionType(puckCollectType)
                .tournament(platiniumPantherTournament)
                .build();
        platiniumPantherTournament.getCompetitions().add(competitionPlatiniumPantherPuckCollect);

        platiniumPantherTournament = tournamentRepository.save(platiniumPantherTournament);

    }

    private void loadRegistrations() {
        Tournament roboticonTournament = tournamentRepository.findByIdFetchCompetitions(1L).orElseThrow();

        Competition sampleCompetition =roboticonTournament.getCompetitions().stream().findFirst().orElseThrow();
        Robot robot2 = robotRepository.findById(2L).orElseThrow();
        Set<User> registrationUsers1 = new HashSet<>();
        userRepository.findAllById(Set.of(3L, 43L, 44L)).forEach(user -> {
            registrationUsers1.add(user);
        });
        Registration registration1 = Registration.builder()
                .robot(robot2)
                .competition(sampleCompetition)
                .build();
        registration1.setUsers(registrationUsers1);
        registration1 = registrationRepository.save(registration1);
        RegistrationStatus registrationStatus1 = RegistrationStatus.builder()
                .timeFrom(LocalDateTime.now())
                .status(RegistrationCurrentStatus.APPLIED)
                .registration(registration1)
                .build();
        registrationStatus1 = registrationStatusRepository.save(registrationStatus1);

        Robot robot8 = robotRepository.findById(8L).orElseThrow();
        Set<User> registrationUsers2 = new HashSet<>();
        userRepository.findAllById(Set.of(8L, 9L)).forEach(user -> {
            registrationUsers2.add(user);
        });

        Registration registration2 = Registration.builder()
                .robot(robot8)
                .users(registrationUsers2)
                .competition(sampleCompetition)
                .build();
        registration2 = registrationRepository.save(registration2);
        RegistrationStatus registrationStatus2 = RegistrationStatus.builder()
                .timeFrom(LocalDateTime.now())
                .status(RegistrationCurrentStatus.APPLIED)
                .registration(registration2)
                .build();
        registrationStatus2 = registrationStatusRepository.save(registrationStatus2);

        Robot robot16 = robotRepository.findById(16L).orElseThrow();
        Set<User> registrationUsers3 = new HashSet<>();
        userRepository.findAllById(Set.of(16L, 17L, 18L)).forEach(user -> {
            registrationUsers3.add(user);
        });
        Registration registration3 = Registration.builder()
                .robot(robot16)
                .competition(sampleCompetition)
                .users(registrationUsers3)
                .build();
        registration3 = registrationRepository.save(registration3);
        RegistrationStatus registrationStatus3 = RegistrationStatus.builder()
                .timeFrom(LocalDateTime.now())
                .status(RegistrationCurrentStatus.APPLIED)
                .registration(registration3)
                .build();
        registrationStatus3 = registrationStatusRepository.save(registrationStatus3);

        Robot robot19 = robotRepository.findById(19L).orElseThrow();
        Set<User> registrationUsers4 = new HashSet<>();
        userRepository.findAllById(Set.of(19L, 21L)).forEach(registrationUsers4::add);
        Registration registration4 = Registration.builder()
                .robot(robot19)
                .competition(sampleCompetition)
                .users(registrationUsers4)
                .build();
        registration4 = registrationRepository.save(registration4);
        RegistrationStatus registrationStatus4 = RegistrationStatus.builder()
                .timeFrom(LocalDateTime.now())
                .status(RegistrationCurrentStatus.APPLIED)
                .registration(registration4)
                .build();
        registrationStatus4 = registrationStatusRepository.save(registrationStatus4);

    }

}
