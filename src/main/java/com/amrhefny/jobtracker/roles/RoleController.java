package com.amrhefny.jobtracker.roles;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/roles")
@CrossOrigin("*")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody RoleDTO roleRequest){
        return ResponseEntity.status(201).body(roleService.createRole(roleRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> findARole(@PathVariable Long id){
        return ResponseEntity.status(200).body(roleService.getARole(id));
    }

    @GetMapping
    public ResponseEntity<List<Role>> findAllRoles(){
        return ResponseEntity.status(200).body(roleService.getAllRoles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<Role>> updateARole(@RequestBody RoleDTO roleRequest, @PathVariable Long id){
        Optional<Role> updatedRole = roleService.updateARole(roleRequest, id);
        return ResponseEntity.status(200).body(updatedRole);
    }
}
