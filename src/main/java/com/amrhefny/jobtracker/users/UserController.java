package com.amrhefny.jobtracker.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createAUser(@RequestBody UserDTO userRequest){
        return ResponseEntity.status(201).body(userService.saveUser(userRequest));
    }

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers(){
        return ResponseEntity.status(200).body(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findAUser(@PathVariable Long id){
        return ResponseEntity.status(200).body(userService.getAUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateAUser(@RequestBody UserDTO userRequest, @PathVariable Long id){
        return ResponseEntity.status(200).body(userService.updateAUser(userRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAUser(@PathVariable Long id){
        userService.deleteAUser(id);
        return ResponseEntity.status(200).body("User with id " + id + " is deleted");
    }

}
