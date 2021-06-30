package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.services.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    @GetMapping(value = {"", "/"})
    public List<TournamentCommand> list(){
        return tournamentService.findAll();
    }

    @GetMapping(value = "{id}")
    public TournamentCommand getById(@PathVariable Long id){
        return tournamentService.findById(id);
    }



}
