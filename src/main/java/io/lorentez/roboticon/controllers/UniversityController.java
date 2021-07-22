package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.UniversityCommand;
import io.lorentez.roboticon.services.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/universities")
public class UniversityController {

    private final UniversityService universityService;

    @GetMapping
    public List<UniversityCommand> listAllUniversities(){
        return universityService.findAll();
    }

    @GetMapping("{universityId}")
    public UniversityCommand getById(@PathVariable @NotNull Long universityId) {
        return universityService.findById(universityId);
    }

    @PreAuthorize("hasAuthority('admin.university.manage')")
    @PutMapping("{universityId}")
    public ResponseEntity<?> updateById(@PathVariable @NotNull Long universityId,
                                        @RequestBody @Valid UniversityCommand universityCommand){
        try{
            universityService.update(universityId, universityCommand);
            return ResponseEntity.noContent().build();
        }
        catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAuthority('admin.university.manage')")
    @PostMapping
    public ResponseEntity<?> addNewUniversity(@RequestBody @Valid UniversityCommand university){
        UniversityCommand returnedCommand = universityService.addUniversity(university);
        return ResponseEntity.created(URI.create("/api/v1/universities/" + returnedCommand.getId())).build();
    }

}
