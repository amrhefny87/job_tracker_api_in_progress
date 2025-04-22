package com.amrhefny.jobtracker.users;

import com.amrhefny.jobtracker.roles.Role;
import com.amrhefny.jobtracker.roles.RoleRespository;
import com.amrhefny.jobtracker.roles.exceptions.RoleNotFoundException;
import com.amrhefny.jobtracker.users.exceptions.UserExistException;
import com.amrhefny.jobtracker.users.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRespository roleRespository;

    public UserService(UserRepository userRepository, RoleRespository roleRespository) {
        this.userRepository = userRepository;
        this.roleRespository = roleRespository;
    }

    public User createUser(UserDTO userRequest){
        if(userRepository.existsByEmail(userRequest.email())){
            throw new UserExistException("User already exists");
        }

        Role userRequestRole = roleRespository.findById(userRequest.role())
                .orElseThrow(()-> new RoleNotFoundException("Role with id " + userRequest.role() + " not found"));

        User userToSave = new User(
                userRequest.userName(),
                userRequest.firstName(),
                userRequest.lastName(),
                userRequest.jobTitle(),
                userRequest.email(),
                userRequest.password(),
                userRequestRole
        );

        return userRepository.save(userToSave);
    }

    public User getAUser(Long id){
        Optional<User> returturnedUser = userRepository.findById(id);

        if(returturnedUser.isEmpty()){
            throw new UserNotFoundException("User with id " + id + " not found");
        }

        return returturnedUser.get();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User updateAUser(UserPatchDTO userPatchRequest, Long id){
        Optional<User> returturnedUser = userRepository.findById(id);

        if(returturnedUser.isEmpty()){
            throw new UserNotFoundException("User with id " + id + " not found");
        }

        User userToBeUpdated = returturnedUser.get();

        userPatchRequest.getUserName().ifProvided(userToBeUpdated::setUserName);
        userPatchRequest.getFirstName().ifProvided(userToBeUpdated::setFirstName);
        userPatchRequest.getLastName().ifProvided(userToBeUpdated::setLastName);
        userPatchRequest.getJobTitle().ifProvided(userToBeUpdated::setJobTitle);
        userPatchRequest.getEmail().ifProvided(userToBeUpdated::setEmail);
        userPatchRequest.getPassword().ifProvided(userToBeUpdated::setPassword);
        userPatchRequest.getRole().ifProvided(roleId->{
           Role role = roleRespository.findById(roleId)
                            .orElseThrow(()-> new RoleNotFoundException("Role with id " + userPatchRequest.getRole() + " not found"));
           userToBeUpdated.setRole(role);
        });

        return userRepository.save(userToBeUpdated);
    }

    public void deleteAUser(Long id){
        Optional<User> returturnedUser = userRepository.findById(id);

        if(returturnedUser.isEmpty()){
            throw new UserNotFoundException("User with id " + id + " not found");
        }

        userRepository.deleteById(id);
    }


}
