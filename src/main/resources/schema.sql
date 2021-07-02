CREATE TABLE University(
                           id BIGSERIAL NOT NULL PRIMARY KEY,
                           name VARCHAR(300) NOT NULL,
                           address_line1 VARCHAR(200) NOT NULL,
                           address_line2 VARCHAR(200) NOT NULL,
                           zipcode VARCHAR(10) NOT NULL,
                           city VARCHAR(100) NOT NULL,
                           province VARCHAR(100) NOT NULL,
                           country VARCHAR(100) NOT NULL
);

CREATE TABLE Team(
                     id BIGSERIAL NOT NULL PRIMARY KEY,
                     name VARCHAR(150) NOT NULL,
                     university_id BIGINT,
                     time_created TIMESTAMP NOT NULL
);

CREATE TABLE RobotTeam(
                          id BIGSERIAL NOT NULL PRIMARY KEY,
                          team_id BIGINT NOT NULL,
                          robot_id BIGINT NOT NULL,
                          time_added DATE NOT NULL,
                          time_removed DATE
);

CREATE TABLE Robot(
                      id BIGSERIAL NOT NULL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      time_added TIMESTAMP NOT NULL
);

CREATE TABLE UserTeam(
                         id BIGSERIAL NOT NULL PRIMARY KEY,
                         team_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         time_added TIMESTAMP NOT NULL,
                         time_removed TIMESTAMP
);

CREATE TABLE User(
                     id BIGSERIAL NOT NULL PRIMARY KEY,
                     name VARCHAR (100),
                     surname VARCHAR (200),
                     email VARCHAR (200) NOT NULL,
                     password VARCHAR (200) NOT NULL,
                     enabled BOOLEAN NOT NULL,
                     account_non_expired BOOLEAN NOT NULL,
                     credentials_non_expired BOOLEAN NOT NULL,
                     account_non_locked BOOLEAN NOT NULL
);

CREATE TABLE UserRole(
                    id BIGSERIAL NOT NULL PRIMARY KEY,
                    user_id BIGINT NOT NULL,
                    role_id BIGINT NOT NULL,
                    time_granted TIMESTAMP NOT NULL,
                    time_removed TIMESTAMP
);

CREATE TABLE Role(
                    id BIGSERIAL NOT NULL PRIMARY KEY,
                    name VARCHAR (200) NOT NULL
);

CREATE TABLE RoleAuthority(
                    id BIGSERIAL NOT NULL PRIMARY KEY,
                    role_id BIGINT NOT NULL,
                    authority_id BIGINT NOT NULL,
                    time_granted TIMESTAMP NOT NULL,
                    time_removed TIMESTAMP
);

CREATE TABLE Authority(
                    id BIGSERIAL NOT NULL PRIMARY KEY,
                    permission VARCHAR (200) NOT NULL,
                    description VARCHAR (500) NOT NULL
);

CREATE TABLE UserRegistration(
                                 user_id BIGINT NOT NULL,
                                 registration_id BIGINT NOT NULL,
                                 PRIMARY KEY (user_id, registration_id)
);

CREATE TABLE Registration(
                             id BIGSERIAL NOT NULL PRIMARY KEY,
                             robot_id BIGINT NOT NULL,
                             competition_id BIGINT NOT NULL
);

CREATE TABLE RegistrationStatus(
                                   id BIGSERIAL NOT NULL PRIMARY KEY,
                                   registration_id BIGINT NOT NULL,
                                   status VARCHAR(80) NOT NULL,
                                   time_from TIMESTAMP NOT NULL,
                                   time_to TIMESTAMP
);

CREATE TABLE Competition(
                            id BIGSERIAL NOT NULL PRIMARY KEY,
                            name VARCHAR(200) NOT NULL,
                            competition_type_id  BIGINT NOT NULL,
                            tournament_id BIGINT,
                            description TEXT
);

CREATE TABLE CompetitionType(
                                id BIGSERIAL NOT NULL PRIMARY KEY,
                                type VARCHAR(80) NOT NULL,
                                score_type VARCHAR(80) NOT NULL
);

CREATE TABLE Tournament(
                           id BIGSERIAL NOT NULL PRIMARY KEY,
                           name VARCHAR(200) NOT NULL,
                           date_start TIMESTAMP NOT NULL,
                           date_end TIMESTAMP NOT NULL
);

CREATE TABLE PasswordResetToken(
    id BIGSERIAL NOT NULL PRIMARY KEY,
    token VARCHAR(200) NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    user_id BIGINT NOT NULL
);

ALTER TABLE Team ADD CONSTRAINT team_university_id FOREIGN KEY (university_id) REFERENCES University(id);
ALTER TABLE RobotTeam ADD CONSTRAINT robotteam_team_id FOREIGN KEY (team_id) REFERENCES Team(id);
ALTER TABLE RobotTeam ADD CONSTRAINT robotteam_robot_id FOREIGN KEY (robot_id) REFERENCES Robot(id);
ALTER TABLE UserTeam ADD CONSTRAINT userteam_team_id FOREIGN KEY (team_id) REFERENCES Team(id);
ALTER TABLE UserTeam ADD CONSTRAINT userteam_user_id FOREIGN KEY (user_id) REFERENCES User(id);
ALTER TABLE UserRegistration ADD CONSTRAINT userregistration_user_id FOREIGN KEY (user_id) REFERENCES User(id);
ALTER TABLE UserRegistration ADD CONSTRAINT userregistration_registration_id FOREIGN KEY (registration_id) REFERENCES Registration(id);
ALTER TABLE RegistrationStatus ADD CONSTRAINT registrationstatus_registration_id FOREIGN KEY (registration_id) REFERENCES Registration(id);
ALTER TABLE Registration ADD CONSTRAINT registration_competition_id FOREIGN KEY (competition_id) REFERENCES Competition(id);
ALTER TABLE Competition ADD CONSTRAINT competition_competitiontype_id FOREIGN KEY (competition_type_id) REFERENCES CompetitionType(id);
ALTER TABLE Competition ADD CONSTRAINT competition_tournament_id FOREIGN KEY (tournament_id) REFERENCES Tournament(id);
ALTER TABLE RoleAuthority ADD CONSTRAINT roleauthority_role_id FOREIGN KEY (role_id) REFERENCES Role(id);
ALTER TABLE RoleAuthority ADD CONSTRAINT roleauthority_authority_id FOREIGN KEY (authority_id) REFERENCES Authority(id);
ALTER TABLE UserRole ADD CONSTRAINT userrole_role_id FOREIGN KEY (role_id) REFERENCES Role(id);
ALTER TABLE UserRole ADD CONSTRAINT userrole_user_id FOREIGN KEY (user_id) REFERENCES User(id);
ALTER TABLE PasswordResetToken ADD CONSTRAINT passwordresettoken_user_id FOREIGN KEY (user_id) REFERENCES User(id);