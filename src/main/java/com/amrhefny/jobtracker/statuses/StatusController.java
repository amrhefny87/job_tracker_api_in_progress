package com.amrhefny.jobtracker.statuses;

import com.amrhefny.jobtracker.roles.Role;
import com.amrhefny.jobtracker.roles.RoleDTO;
import com.amrhefny.jobtracker.roles.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/statuses")
@CrossOrigin("*")
public class StatusController {
    private final StatusService statusService;

    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @PostMapping
    public ResponseEntity<Status> createStatus(@RequestBody StatusDTO statusRequest){
        return ResponseEntity.status(201).body(statusService.createStatus(statusRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Status> findAStatus(@PathVariable Long id){
        return ResponseEntity.status(200).body(statusService.getAStatus(id));
    }

    @GetMapping
    public ResponseEntity<List<Status>> findAllStatuses(){
        return ResponseEntity.status(200).body(statusService.getAllStatuses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<Status>> updateAStatuses(@RequestBody StatusDTO statusRequest, @PathVariable Long id){
        Optional<Status> updatedStatus = statusService.updateAStatus(statusRequest, id);
        return ResponseEntity.status(200).body(updatedStatus);
    }
}
