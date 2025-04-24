package com.amrhefny.jobtracker.users;

import com.amrhefny.jobtracker.roles.Role;
import com.amrhefny.jobtracker.roles.RoleDTO;
import com.amrhefny.jobtracker.roles.RoleRespository;
import com.amrhefny.jobtracker.roles.RoleService;
import com.amrhefny.jobtracker.users.exceptions.UserExistException;
import com.amrhefny.jobtracker.users.exceptions.UserNotFoundException;
import com.amrhefny.jobtracker.util.PatchField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRespository roleRespository;

    @InjectMocks
    UserService userService;

    Role savedRole;
    RoleDTO roleRequest;

    UserDTO userRequest;
    UserPatchDTO userPatchRequestOneField;
    UserPatchDTO getUserPatchRequestRoleField;
    User savedUser;

    @BeforeEach
    public void beforeEachTest(){
        this.roleRequest = new RoleDTO("test role");
        this.savedRole = new Role(1L, "test role");
        this.userRequest = new UserDTO(
                "test userName",
                "test firstName",
                "test lastName",
                "test jobTitle",
                "test email",
                "test password",
                savedRole.getId()
        );
        this.savedUser = new User(
                1L,
                "test userName",
                "test firstName",
                "test lastName",
                "test jobTitle",
                "test email",
                "test password",
                savedRole
        );
    }

    @Test
    void testAUserCanBeCreated(){
        Mockito.when(roleRespository.findById(savedRole.getId())).thenReturn(Optional.of(savedRole));

        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User userResponse = userService.createUser(userRequest);

        assertEquals(savedUser, userResponse);
        verify(userRepository).save(any(User.class));
    }
    
    @Test
    void testAUserCanNotBeCreatedWithExistingEmail(){
        Mockito.when(userRepository.existsByEmail(savedUser.getEmail())).thenReturn(true);
        
        Exception exception = assertThrows(UserExistException.class, ()-> userService.createUser(userRequest));
        
        assertEquals("User already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAUserCanBeRetrievedByItsId(){
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        User userResponse = userService.getAUser(1L);

        assertEquals(savedUser, userResponse);
    }

    @Test
    void testAnExceptionThrownWhenUserNotFound(){
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, ()-> userService.getAUser(2L));

        assertEquals("User with id 2 not found", exception.getMessage());

        verify(userRepository, Mockito.times(1)).findById(any(Long.class));
    }

    @Test
    void testTheListOfUsersCanBeRetrieved(){
        List<User> usersList = List.of(savedUser);

        Mockito.when(userRepository.findAll()).thenReturn(usersList);

        List<User> userResponse = userService.getAllUsers();

        assertEquals(usersList,userResponse);
    }

    @Test
    void testAllUserFieldsCanBeUpdated(){
        Role updateRole = new Role(2L,"update role");

        Mockito.when(roleRespository.findById(2L)).thenReturn(Optional.of(updateRole));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserPatchDTO userPatchRequestAllFields = new UserPatchDTO(
                PatchField.of("update userName"),
                PatchField.of("update firstName"),
                PatchField.of("update lastName"),
                PatchField.of("update jobTitle"),
                PatchField.of("update email"),
                PatchField.of("update password"),
                PatchField.of(2L)
        );

        User userResponse = userService.updateAUser(userPatchRequestAllFields, 1L);

        assertEquals("update userName", userResponse.getUserName());
        assertEquals("update firstName", userResponse.getFirstName());
        assertEquals("update lastName", userResponse.getLastName());
        assertEquals("update jobTitle", userResponse.getJobTitle());
        assertEquals("update email", userResponse.getEmail());
        assertEquals("update password", userResponse.getPassword());
        assertEquals(updateRole, userResponse.getRole());
    }

    @Test
    void testOneUserFieldCanBeUpdated(){
        Role updateRole = new Role(2L,"update role");

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserPatchDTO userPatchRequestOneField = new UserPatchDTO();
        userPatchRequestOneField.setUserName(PatchField.of("update userName"));

        User updatedUser = new User(
                1L,
                "update userName",
                "test firstName",
                "test lastName",
                "test jobTitle",
                "test email",
                "test password",
                savedRole
        );

        User userResponse = userService.updateAUser(userPatchRequestOneField, 1L);

        assertEquals("update userName", userResponse.getUserName());
        assertEquals("test firstName", userResponse.getFirstName());
        assertEquals("test lastName", userResponse.getLastName());
        assertEquals("test jobTitle", userResponse.getJobTitle());
        assertEquals("test email", userResponse.getEmail());
        assertEquals("test password", userResponse.getPassword());
        assertEquals(savedRole, userResponse.getRole());
    }

    @Test
    void testOnlyRoleFieldCanBeUpdated(){
        Role updateRole = new Role(2L,"update role");

        Mockito.when(roleRespository.findById(2L)).thenReturn(Optional.of(updateRole));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserPatchDTO userPatchRequestRoleField = new UserPatchDTO();
        userPatchRequestRoleField.setRole(PatchField.of(2L));


        User userResponse = userService.updateAUser(userPatchRequestRoleField, 1L);

        assertEquals("test userName", userResponse.getUserName());
        assertEquals("test firstName", userResponse.getFirstName());
        assertEquals("test lastName", userResponse.getLastName());
        assertEquals("test jobTitle", userResponse.getJobTitle());
        assertEquals("test email", userResponse.getEmail());
        assertEquals("test password", userResponse.getPassword());
        assertEquals(updateRole, userResponse.getRole());
    }

    @Test
    void testAUserCanBeDeletedByItsId(){
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        userService.deleteAUser(1L);

        verify(userRepository, Mockito.times(1)).deleteById(1L);
    }
}