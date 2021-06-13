package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.services.TournamentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping(value = {"", "/"})
    public List<TournamentCommand> findAll(){
        return tournamentService.findAll();
    }

}
