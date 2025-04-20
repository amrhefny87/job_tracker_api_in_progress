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

    public User saveUser(UserDTO userRequest){
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

    public User updateAUser(UserDTO userRequest, Long id){
        Optional<User> returturnedUser = userRepository.findById(id);

        if(returturnedUser.isEmpty()){
            throw new UserNotFoundException("User with id " + id + " not found");
        }

        Role userRequestRole = roleRespository.findById(userRequest.role())
                .orElseThrow(()-> new RoleNotFoundException("Role with id " + userRequest.role() + " not found"));

        User userToBeUpdated = returturnedUser.get();
        userToBeUpdated.setUserName(userRequest.userName());
        userToBeUpdated.setFirstName(userRequest.firstName());
        userToBeUpdated.setLastName(userRequest.lastName());
        userToBeUpdated.setJobTitle(userRequest.jobTitle());
        userToBeUpdated.setEmail(userRequest.email());
        userToBeUpdated.setPassword(userRequest.password());
        userToBeUpdated.setRole(userRequestRole);

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
