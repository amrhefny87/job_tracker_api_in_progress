package com.amrhefny.jobtracker.roles;

import com.amrhefny.jobtracker.roles.exceptions.RoleExistException;
import com.amrhefny.jobtracker.roles.exceptions.RoleNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRespository roleRespository;

    public RoleService(RoleRespository roleRespository) {
        this.roleRespository = roleRespository;
    }

    public Role createRole(RoleDTO roleRequest){
        if(roleRespository.existsByRole(roleRequest.role())){
            throw new RoleExistException("Role already exists");
        }
        Role roleToBeSaved = new Role(roleRequest.role());

        return roleRespository.save(roleToBeSaved);
    }

    public Role getARole(Long id){
        Optional<Role> returnedRole = roleRespository.findById(id);
        if(returnedRole.isEmpty()){
            throw new RoleNotFoundException("The role with id " + id + " not found");
        }

        return returnedRole.get();
    }

    public List<Role> getAllRoles(){
        return roleRespository.findAll();
    }

    public Optional<Role> updateARole(RoleDTO roleRequest, Long id){
        Optional<Role> returnedRole = roleRespository.findById(id);
        if(returnedRole.isEmpty()){
            throw new RoleNotFoundException("The role with id " + id + " not found");
        }

        Role roleToBeUdpated = returnedRole.get();
        roleToBeUdpated.setRole(roleRequest.role());
        return Optional.of(roleRespository.save(roleToBeUdpated));
    }
}
