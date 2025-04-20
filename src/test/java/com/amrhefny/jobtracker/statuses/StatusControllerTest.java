package com.amrhefny.jobtracker.statuses;

import com.amrhefny.jobtracker.roles.Role;
import com.amrhefny.jobtracker.roles.RoleDTO;
import com.amrhefny.jobtracker.roles.RoleRespository;
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
class StatusControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StatusRepository statusRespository;

    StatusDTO statusRequest;
    Status savedStatus;
    Status statusToSave;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEachTest(){
        this.statusRequest = new StatusDTO("test status");
        this.savedStatus = new Status(1L, "test status");
        this.statusToSave = new Status("test status");
    }

    @Test
    void canCreateAStatus() throws Exception {
        String requestJson = objectMapper.writeValueAsString(statusRequest);

        mockMvc.perform(post("/api/statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(savedStatus.getId()))
                .andExpect(jsonPath("status").value(savedStatus.getStatus()));

        assertEquals(1, statusRespository.count());
    }

    @Test
    void canFindAStatusById() throws Exception {
        statusRespository.save(statusToSave);
        mockMvc.perform(get("/api/statuses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("status").value("test status"));
    }

    @Test
    void canGetAllStatuses() throws Exception {
        statusRespository.save(statusToSave);
        mockMvc.perform(get("/api/statuses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("test status"));
    }

    @Test
    void canUpdateAStatus() throws Exception {
        statusRespository.save(statusToSave);
        StatusDTO statusUpdateRequest = new StatusDTO("update status");
        String statusUpdateRequestJson = objectMapper.writeValueAsString(statusUpdateRequest);
        mockMvc.perform(put("/api/statuses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusUpdateRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("update status"));
    }

}