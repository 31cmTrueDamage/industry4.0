package org.industry40.controllers;

import org.industry40.models.Machine;
import org.industry40.services.MachinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/machines")
public class MachinesController {

    @Autowired
    private MachinesService machinesService;

    @GetMapping
    public ResponseEntity<List<Machine>> getAllMachines() {
        return new ResponseEntity<>(machinesService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/{id}/allocate")
    public ResponseEntity<Void> allocate(
            @PathVariable Integer id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime until) {
        machinesService.allocateMachine(id, until);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/release")
    public ResponseEntity<Void> release(@PathVariable Integer id) {
        machinesService.releaseMachine(id);
        return ResponseEntity.ok().build();
    }
}
