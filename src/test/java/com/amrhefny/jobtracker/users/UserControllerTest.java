package com.amrhefny.jobtracker.users;

import com.amrhefny.jobtracker.roles.Role;
import com.amrhefny.jobtracker.roles.RoleRespository;
import com.amrhefny.jobtracker.util.PatchField;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
            private RoleRespository roleRespository;

    Role savedRole;
    UserDTO userRequest;
    User savedUser;
    User userToSave;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEachTest(){
        this.savedRole = roleRespository.save(new Role(null, "test role"));
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
        this.userToSave = new User(
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
    void canCreateAUser() throws Exception {
        String requestJson = objectMapper.writeValueAsString(userRequest);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(savedUser.getId()))
                .andExpect(jsonPath("userName").value(savedUser.getUserName()))
                .andExpect(jsonPath("firstName").value(savedUser.getFirstName()))
                .andExpect(jsonPath("lastName").value(savedUser.getLastName()))
                .andExpect(jsonPath("jobTitle").value(savedUser.getJobTitle()))
                .andExpect(jsonPath("email").value(savedUser.getEmail()))
                .andExpect(jsonPath("password").value(savedUser.getPassword()))
                .andExpect(jsonPath("role.id").value(savedUser.getRole().getId()))
                .andExpect(jsonPath("role.role").value(savedUser.getRole().getRole()))
        ;

        assertEquals(1, userRepository.count());
    }

    @Test
    void canFindAUserById() throws Exception {
        userRepository.save(userToSave);
        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("userName").value(savedUser.getUserName()))
                .andExpect(jsonPath("firstName").value(savedUser.getFirstName()))
                .andExpect(jsonPath("lastName").value(savedUser.getLastName()))
                .andExpect(jsonPath("jobTitle").value(savedUser.getJobTitle()))
                .andExpect(jsonPath("email").value(savedUser.getEmail()))
                .andExpect(jsonPath("password").value(savedUser.getPassword()))
                .andExpect(jsonPath("role.id").value(savedUser.getRole().getId()))
                .andExpect(jsonPath("role.role").value(savedUser.getRole().getRole()));
    }

    @Test
    void canGetAllUsers() throws Exception {
        userRepository.save(userToSave);
        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userName").value(savedUser.getUserName()))
                .andExpect(jsonPath("$[0].firstName").value(savedUser.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(savedUser.getLastName()))
                .andExpect(jsonPath("$[0].jobTitle").value(savedUser.getJobTitle()))
                .andExpect(jsonPath("$[0].email").value(savedUser.getEmail()))
                .andExpect(jsonPath("$[0].password").value(savedUser.getPassword()))
                .andExpect(jsonPath("$[0].role.id").value(savedUser.getRole().getId()))
                .andExpect(jsonPath("$[0].role.role").value(savedUser.getRole().getRole()));
    }

    @Test
    void canUpdateAllFieldsForAUser() throws Exception {
        userRepository.save(userToSave);
        Role updateRole = roleRespository.save(new Role("update role"));
        String userUpdateRequestJson = """
                {
                    "userName": "update userName",
                    "firstName": "update firstName",
                    "lastName": "update lastName",
                    "jobTitle": "update jobTitle",
                    "email": "update email",
                    "password": "update password",
                    "role": 2
                }
        """;

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.userName").value("update userName"))
                .andExpect(jsonPath("$.firstName").value("update firstName"))
                .andExpect(jsonPath("$.lastName").value("update lastName"))
                .andExpect(jsonPath("$.jobTitle").value("update jobTitle"))
                .andExpect(jsonPath("$.email").value("update email"))
                .andExpect(jsonPath("$.password").value("update password"))
                .andExpect(jsonPath("$.role.id").value(updateRole.getId().intValue()));
    }

    @Test
    void canUpdateOneFieldsForAUser() throws Exception {
        userRepository.save(userToSave);
        String userUpdateRequestJson = """
                {
                    "userName": "update userName"
                }
        """;

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.userName").value("update userName"))
                .andExpect(jsonPath("$.firstName").value(savedUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(savedUser.getLastName()))
                .andExpect(jsonPath("$.jobTitle").value(savedUser.getJobTitle()))
                .andExpect(jsonPath("$.email").value(savedUser.getEmail()))
                .andExpect(jsonPath("$.password").value(savedUser.getPassword()))
                .andExpect(jsonPath("$.role.id").value(savedUser.getRole().getId()));
    }

    @Test
    void canUpdateRoleFieldsForAUser() throws Exception {
        userRepository.save(userToSave);
        Role updateRole = roleRespository.save(new Role("update role"));
        String userUpdateRequestJson = """
                {
                    "role": "2"
                }
        """;

        mockMvc.perform(patch("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.userName").value(savedUser.getUserName()))
                .andExpect(jsonPath("$.firstName").value(savedUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(savedUser.getLastName()))
                .andExpect(jsonPath("$.jobTitle").value(savedUser.getJobTitle()))
                .andExpect(jsonPath("$.email").value(savedUser.getEmail()))
                .andExpect(jsonPath("$.password").value(savedUser.getPassword()))
                .andExpect(jsonPath("$.role.id").value(updateRole.getId()));
    }

    @Test
    void deleteAUser() throws Exception {
        userRepository.save(userToSave);

        assertEquals(1, userRepository.count());

        mockMvc.perform(delete("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(0, userRepository.count());
    }

}