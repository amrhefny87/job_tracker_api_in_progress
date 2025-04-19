package com.amrhefny.jobtracker.roles;

import com.amrhefny.jobtracker.roles.exceptions.RoleExistException;
import com.amrhefny.jobtracker.roles.exceptions.RoleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    RoleRespository roleRespository;

    @InjectMocks
    RoleService roleService;

    RoleDTO roleRequest;
    Role savedRole;

    @BeforeEach
    public void beforeEachTest(){
        this.roleRequest = new RoleDTO("test role");
        this.savedRole = new Role(1L, "test role");
    }

    @Test
    void testARoleCanBeCreated(){
        Mockito.when(roleRespository.save(any(Role.class))).thenReturn(savedRole);

        Role roleResponse = roleService.createRole(roleRequest);

        assertEquals(1L, roleResponse.getId());
        assertEquals("test role", roleResponse.getRole());
        verify(roleRespository).save(any(Role.class));
    }

    @Test
    void testARoleCanNotBeCreatedWithAnExistingRole(){
        Mockito.when((roleRespository.existsByRole(savedRole.getRole()))).thenReturn(true);

        Exception exception = assertThrows(RoleExistException.class, ()-> roleService.createRole(roleRequest));

        assertEquals("Role already exists", exception.getMessage());
        verify(roleRespository, never()).save(any(Role.class));
    }

    @Test
    void testARoleCanBeRetrievedByItsId(){
        Mockito.when(roleRespository.findById(1L)).thenReturn(Optional.of(savedRole));

        Role roleResponse = roleService.getARole(1L);

        assertEquals(savedRole, roleResponse);
    }

    @Test
    void testAnExceptionThrownWhenRoleNotFound(){
        Mockito.when((roleRespository.findById(2L))).thenReturn(Optional.empty());
        Exception exception = assertThrows(RoleNotFoundException.class, ()-> roleService.getARole(2L));

        assertEquals("The role with id 2 not found", exception.getMessage());
        verify(roleRespository, Mockito.times(1)).findById(any(Long.class));
    }

    @Test
    void testTheListOfRolesCanBeRetrieved(){
        List<Role> rolesList = List.of(savedRole);

        Mockito.when(roleRespository.findAll()).thenReturn(rolesList);

        List<Role> roleResponse = roleRespository.findAll();

        assertEquals(rolesList, roleResponse);
    }

    @Test
    void testARoleCanBeUpdated(){
        Mockito.when(roleRespository.findById(1L)).thenReturn(Optional.of(savedRole));
        Mockito.when(roleRespository.save(any(Role.class))).thenReturn(savedRole);

        RoleDTO roleRequest = new RoleDTO("update role");

        Optional<Role> roleResponse = roleService.updateARole(roleRequest, 1L);

        assertTrue(roleResponse.isPresent());

        Role updatedRole = roleResponse.get();

        assertEquals("update role", updatedRole.getRole());
    }

    @Test
    void testARoleCanNotBeUpdatedIfDoesNotExist(){
        Mockito.when(roleRespository.findById(1L)).thenReturn(Optional.empty());

        RoleDTO roleRequest = new RoleDTO("update role");

        Exception exception = assertThrows(RoleNotFoundException.class, ()-> roleService.updateARole(roleRequest, 1L));

        assertEquals("The role with id 1 not found", exception.getMessage());
        verify(roleRespository, never()).save(any(Role.class));
    }
}