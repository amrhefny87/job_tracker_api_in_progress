package com.amrhefny.jobtracker.roles;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RoleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoleRespository roleRespository;

    RoleDTO roleRequest;
    Role savedRole;
    Role roleToSave;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEachTest(){
        this.roleRequest = new RoleDTO("test role");
        this.savedRole = new Role(1L, "test role");
        this.roleToSave = new Role("test role");
    }

    @Test
    void canCreateARole() throws Exception {
        String requestJson = objectMapper.writeValueAsString(roleRequest);

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(savedRole.getId()))
                .andExpect(jsonPath("role").value(savedRole.getRole()));

        assertEquals(1, roleRespository.count());
    }

    @Test
    void canFindARoleById() throws Exception {
        roleRespository.save(roleToSave);
        mockMvc.perform(get("/api/roles/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("role").value("test role"));
    }

    @Test
    void canGetAllRoles() throws Exception {
        roleRespository.save(roleToSave);
        mockMvc.perform(get("/api/roles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].role").value("test role"));
    }

    @Test
    void canUpdateARole() throws Exception {
        roleRespository.save(roleToSave);
        RoleDTO roleUpdateRequest = new RoleDTO("update role");
        String roleUpdateRequestJson = objectMapper.writeValueAsString(roleUpdateRequest);
        mockMvc.perform(put("/api/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(roleUpdateRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.role").value("update role"));
    }

}