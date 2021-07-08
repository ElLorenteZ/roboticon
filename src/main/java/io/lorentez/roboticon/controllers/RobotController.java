package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.RobotCommand;
import io.lorentez.roboticon.services.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/robots")
public class RobotController {

    private final RobotService robotService;

    @PreAuthorize("hasAuthority('admin.robot.edit') OR " +
            "hasAuthority('user.robot.edit') AND " +
                        "@robotsAuthenticationManager.canUserEditRobot(authentication, #robotId)")
    @PutMapping("{robotId}")
    public ResponseEntity<?> updateRobot(@RequestBody RobotCommand command, @PathVariable Long robotId){
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

}
