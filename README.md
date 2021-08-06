[![codecov](https://codecov.io/gh/ElLorenteZ/roboticon/branch/master/graph/badge.svg?token=BG85PDHLRF)](https://codecov.io/gh/ElLorenteZ/roboticon)
[![CircleCI](https://circleci.com/gh/ElLorenteZ/roboticon.svg?style=svg&circle-token=f4c008d909f5e6af2c105ea4e03ab057425978dd)](https://circleci.com/gh/ElLorenteZ/roboticon)
# Roboticon - Backend Spring Boot REST API 

## The aim of project

The main goal of the project is to create complex platform 
for organizing and present scores of robot competitions. This 
repository is dedicated to backend side of application. Current 
version is monolith application with endpoints used for registering, adding and 
viewing teams, robots, tournaments and users.

It is possible that this platform will share data with other applications in
the future. For this reason I decided to turn off hibernate DDL and write my
own _data.sql_ to have full control of database model.

#### Next steps: 
- create client side application in Angular,
- decomposition for microservices,
- extend platform to provide presenting of live scores functionality.

## Tests
I attempted to follow TDD process. Services and converters are covered by 
unit tests. Controllers and security is covered by integration tests. I used
[CodeCov](https://codecov.io/gh/ElLorenteZ/roboticon) to show what percent of code is covered 
by unit tests. I also used [CircleCi](https://circleci.com/gh/ElLorenteZ/roboticon) as my tool for 
continuous integration. Some http requests for manual testing were also provided in folder
_"src/main/resources/samples"_.

## Security
There are 2 different types of roles in this application - admin and user. Permissions are granted 
by authorities, which are bound with user roles. Each of the endpoints have two versions of 
authorities - for admin and user. If user tries to reach application endpoint there is usually extra 
validation provided by components from "_src/main/java/io/lorentez/roboticon/security/managers_". 

### List of authorities
| ID   |           PERMISSION             |                                          DESCRIPTION                                           |
|:----:|:--------------------------------:|:----------------------------------------------------------------------------------------------:|
| 1    | tournament.create                | Permission to create new tournament.                                                           |
| 2    | tournament.update                | Permission to update tournament data.                                                          |
| 3    | team.create                      | Permission to create teams.                                                                    |
| 4    | admin.team.update                | Permission to update any team data.                                                            |
| 5    | admin.team.read                  | Permission to read any team data.                                                              |
| 6    | admin.team.invite                | Permission to invite any user to any team.                                                     |
| 7    | admin.team.user.status           | Permission to change internal status of any user in team.                                      |
| 8    | admin.team.view                  | Permission to view details of any team.                                                        |
| 9    | admin.robot.edit                 | Permission to change details of any robot.                                                     |
| 10   | admin.robot.list                 | Permission to list all robots.                                                                 |
| 11   | admin.robot.transfer             | Permission to request transfer of any robot.                                                   |
| 12   | admin.robot.transfer.accept      | Permission to request accept transfer of any robot.                                            |
| 13   | admin.user.edit                  | Permission to edit any user details.                                                           |
| 14   | admin.team.edit                  | Permission to update any team's details.                                                       |
| 15   | admin.registration.team.view     | Permission to preview any team registration.                                                   |
| 16   | admin.registration.create        | Permission to create registration for any team.                                                |
| 17   | admin.registration.edit          | Permission to edit any registration.                                                           |
| 18   | admin.registration.status.update | Permission to change registration status.                                                      |
| 19   | admin.university.manage          | Permission to manage universities' data.                                                       |
| 20   | admin.users.view                 | Permission to list users' data.                                                                |
| 21   | admin.tournament.create          | Permission to create new tournament.                                                           |
| 22   | admin.tournament.edit            | Permission to edit any tournament's data.                                                      |
| 23   | admin.robot.add                  | Permission to add robot to any team.                                                           |
| 24   | admin.robot.view                 | Permission to view any robot.                                                                  |
| 25   | admin.registration.users         | Permission to check any user's registrations.                                                  |
| 26   | user.team.read                   | Permission to read team data where user has status of owner or admin.                          |
| 27   | user.team.invite                 | Permission to invite any user to team where user has status owner or admin.                    |
| 28   | user.team.update                 | Permission to update team data where user has status of owner or admin.                        |
| 29   | user.team.user.status            | Permission to change status of user in team.                                                   |
| 30   | user.team.view                   | Permission to view details of team in which user has status of 'member', 'admin' or 'owner'.   |
| 31   | user.robot.edit                  | Permission to change details of robot in team if user has Admin or Owner status.               |
| 32   | user.robot.transfer              | Permission to transfer robot is user has status OWNER in team.                                 |
| 33   | user.robot.transfer.accept       | Permission to accept transfer robot to user's team when he has OWNER status.                   |
| 34   | user.user.edit                   | Permission to edit user self details.                                                          |
| 35   | user.user.password.change        | Permission to change self password.                                                            |
| 36   | user.team.edit                   | Permission to edit team's data.                                                                |
| 37   | user.registration.team.view      | Permission to preview registration of team in which user has status of OWNER, ADMIN or member. |
| 38   | user.registration.create         | Permission to create registration for team where user has status of ADMIN or OWNER.            |
| 39   | user.registration.edit           | Permission to edit registration for team where user has status of ADMIN or OWNER.              |
| 40   | user.robot.add                   | Permission to add user to team where user has status of OWNER or ADMIN.                        |
| 41   | user.robot.view                  | Permission to view robots in team where user has status of OWNER or ADMIN.                     |
| 42   | user.registration.users          | Permission to check user's own registrations.                                                  |

JWT is used as an authentication method. I wrote 2 custom filters. One is to provide authentication token 
from JSON login credentials, second to validate if user is authorized. I wrote custon password encoder. 
Passwords for sample users have unencrypted passwords. New password (changed or new accounts) are encoded 
with bcrypt. 

## Endpoints
Sample requests for each of the endpoints are in "_src/main/resources/samples_". Authorization tokens can be received 
by sending one of login sample requests. Swagger is also enabled, but is not "prettified" yet.

Current endpoints are listed below. Check comments in samples for more details.

### Auth Controller
| Functionality  | HTTP Method | Path                          |
|:--------------:|:-----------:|:-----------------------------:|
|Login           | POST        | _/api/v1/auth/login_          |
|Register        | POST        | _/api/v1/auth/register_       |
|Change Password | POST        | _/api/v1/auth/changePassword_ |
|Reset Password  | POST        | _/api/v1/auth/resetPassword_  |
|Set Password    | POST        | _/api/v1/auth/setPassword_    |

### Competition Controller
| Functionality         | HTTP Method | Path                                    |
|:---------------------:|:-----------:|:---------------------------------------:|
| Get competition by ID | GET         | _/api/v1/competitions/{competition_id}_ |

### Registration Controller
| Functionality         | HTTP Method | Path                                    |
|:---------------------:|:-----------:|:---------------------------------------:|
| Create registration   | POST        | _/api/v1/registrations_ |
| Update registration   | PUT         | _/api/v1/registrations/{registration_id} |_
| Add new status        | POST        | _/api/v1/registrations/{registration_id}/status_|
| Get registrations of team for tournament | GET | _/api/v1/registrations/tournaments/{tournament_id}/team/{team_id} |_
| Get registrations of user | GET | _/api/v1/registrations/users/{user_id}_ |

### Robot Controller
| Functionality         | HTTP Method | Path                                    |
|:---------------------:|:-----------:|:---------------------------------------:|
| List all robots       | GET | _api/v1/robots_ |
| Add robot | POST | _api/v1/robots_ |
| Get robot by ID | GET | _api/v1/robots/{robot_id}_ |
| Update robot | PUT | _api/v1/robots/{robot_id}_ |
| Transfer robot | POST | _api/v1/robots/{robot_id}/transfer_ |
| Accept transfer robot | POST | _api/v1/robots/{robot_id}/accept_ |

### Team Controller
| Functionality         | HTTP Method | Path                                    |
|:---------------------:|:-----------:|:---------------------------------------:|
| Add team | POST | _/api/v1/teams_ | 
| Get team by ID | GET | _/api/v1/teams/{team_id}_ | 
| Update team | PUT | _api/v1/teams/{team_id}_ |
| Invite to a team | POST | _api/v1/teams/{team_id}/invite_ |
| Change status of user in team | POST | _api/v1/teams/{team_id}/status_ |
| Get teams of user | GET | _/api/v1/teams/user_ |

### Tournament controller
| Functionality         | HTTP Method | Path                                    |
|:---------------------:|:-----------:|:---------------------------------------:|
| Create tournament | POST | _/api/v1/tournaments_ |  
| List all tournaments | GET | _/api/v1/tournaments_ |
| Get tournament by ID | GET | _/api/v1/tournaments/{tournament_id}_ |
| Update tournament | PUT | _/api/v1/tournaments/{tournament_id}_ |

### University Controller
| Functionality         | HTTP Method | Path                                    |
|:---------------------:|:-----------:|:---------------------------------------:|
| Add university | POST | _/api/v1/universities_ | 
| List all universities | GET | _/api/v1/universities_ |
| Get university by ID | GET | _/api/v1/universities/{university_id}_ |
| Update university | PUT | _/api/v1/universities/{university_id}_ |

### User Controller
| Functionality         | HTTP Method | Path                                    |
|:---------------------:|:-----------:|:---------------------------------------:|
| List all users | GET | _/api/v1/users_ |
| Update user | PUT | _/api/v1/users/{user_id} |
