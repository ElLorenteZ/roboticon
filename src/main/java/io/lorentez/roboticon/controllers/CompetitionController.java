package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.CompetitionCommand;
import io.lorentez.roboticon.services.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/competitions")
public class CompetitionController {

    private final CompetitionService service;

    @GetMapping("{id}")
    public CompetitionCommand findById(@PathVariable @NotNull Long id){
        return service.findById(id);
    }

}
