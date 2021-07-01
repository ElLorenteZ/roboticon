package io.lorentez.roboticon.controllers;

import io.lorentez.roboticon.commands.TournamentCommand;
import io.lorentez.roboticon.services.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    @GetMapping(value = {"", "/"})
    public List<TournamentCommand> list(@RequestParam(required = false) String keyword){
        if(keyword == null || keyword.isBlank()){
            return tournamentService.findAll();
        }
        else {
            return tournamentService.findSearchedTournaments(keyword);
        }
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        TournamentCommand command = tournamentService.findById(id);
        if (command == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(command);

    }


}
