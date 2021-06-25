package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.services.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    @GetMapping(value = {"", "/"})
    public List<TournamentCommand> findAll(){
        return tournamentService.findAll();
    }

}
