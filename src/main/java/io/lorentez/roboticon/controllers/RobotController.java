package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.commands.RobotTransferCommand;
import io.lorentez.roboticon.services.RobotService;
import io.lorentez.roboticon.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/robots")
public class RobotController {

    private final RobotService robotService;
    private final TeamService teamService;

    @PreAuthorize("hasAuthority('admin.robot.edit') OR " +
            "hasAuthority('user.robot.edit') AND " +
                        "@robotsAuthenticationManager.canUserEditRobot(authentication, #robotId)")
    @PutMapping("{robotId}")
    public ResponseEntity<?> updateRobot(@RequestBody @Valid RobotCommand command, @PathVariable @NotNull Long robotId){
        try {
            RobotCommand robotCommand = robotService.update(command, robotId);
            return ResponseEntity.ok(robotCommand);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
        catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('admin.robot.view') OR " +
            "hasAuthority('user.robot.view') AND " +
            "@robotsAuthenticationManager.canUserViewRobot(authentication, #robotId)")
    @GetMapping("{robotId}")
    public ResponseEntity<?> getById(@PathVariable Long robotId){
        try {
            RobotCommand robotCommand = robotService.findById(robotId);
            return ResponseEntity.ok(robotCommand);
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('admin.robot.list')")
    @GetMapping
    public List<RobotCommand> list(){
        return robotService.list();
    }

    @PreAuthorize("hasAuthority('admin.robot.transfer') OR " +
            "hasAuthority('user.robot.transfer') AND @robotsAuthenticationManager.canUserTransferRobot(authentication, #robotId)")
    @PostMapping("{robotId}/transfer")
    public ResponseEntity<?> transfer(@PathVariable @NotNull Long robotId,
                                      @RequestBody @Valid RobotTransferCommand robotTransferCommand){
        if (robotTransferCommand == null || robotTransferCommand.getTeamId() == null){
            return ResponseEntity.badRequest().build();
        }
        else if (teamService.existByTeamId(robotTransferCommand.getTeamId())){
            robotService.transferToTeam(robotId, robotTransferCommand.getTeamId());
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAuthority('admin.robot.transfer.accept') OR " +
            "hasAuthority('user.robot.transfer.accept') AND @robotsAuthenticationManager.canUserAcceptTransfer(authentication, #robotId)")
    @PostMapping("{robotId}/accept")
    public ResponseEntity<?> acceptTransfer(@PathVariable @NotNull Long robotId) {
        try{
            robotService.transferAcceptRobot(robotId);
            return ResponseEntity.noContent().build();
        }
        catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('admin.robot.add') OR " +
            "hasAuthority('user.robot.add') AND @robotsAuthenticationManager.canUserAddRobot(authentication, #robot)")
    @PostMapping
    public ResponseEntity<?> addRobot(@RequestBody @Valid RobotCommand robot){
        try {
            RobotCommand savedRobot = robotService.addRobot(robot);
            return ResponseEntity.created(URI.create("/api/v1/robots/" + savedRobot.getId().toString())).build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
