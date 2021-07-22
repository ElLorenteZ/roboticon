package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.BasicUserCommand;
import io.lorentez.roboticon.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAuthority('admin.user.edit') OR " +
            "hasAuthority('user.user.edit') AND @userAuthenticationManager.isUserSelf(authentication, #userId)")
    @PostMapping("{userId}/update")
    public ResponseEntity<?> updateDetails(@PathVariable @NotNull Long userId,
                                        @RequestBody @Valid BasicUserCommand userDetails){
        try {
            BasicUserCommand returnedCommand = userService.changeUserDetails(userId, userDetails);
            return ResponseEntity.ok(returnedCommand);
        }
        catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }



}
