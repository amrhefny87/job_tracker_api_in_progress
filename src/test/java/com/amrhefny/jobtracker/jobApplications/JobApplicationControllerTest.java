package com.amrhefny.jobtracker.jobApplications;

import com.amrhefny.jobtracker.roles.Role;
import com.amrhefny.jobtracker.roles.RoleRespository;
import com.amrhefny.jobtracker.statuses.Status;
import com.amrhefny.jobtracker.statuses.StatusRepository;
import com.amrhefny.jobtracker.users.User;
import com.amrhefny.jobtracker.users.UserRepository;
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
class JobApplicationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
            private RoleRespository roleRespository;

    Role savedRole;
    Status savedStatus;
    User savedUser;

    JobApplicationDTO jobApplicationRequest;
    JobApplication savedJobApplication;
    JobApplication jobApplicationToSave;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void beforeEachTest(){
        this.savedRole = roleRespository.save(new Role(null, "test role"));
        this.savedStatus = statusRepository.save(new Status(null, "test status"));
        this.savedUser = userRepository.save(new User(
                null,
                "test userName",
                "test firstName",
                "test lastName",
                "test jobTitle",
                "test email",
                "test password",
                savedRole
        ));

        this.jobApplicationRequest = new JobApplicationDTO(
                "test jobTitle",
                "test companyName",
                1L,
                "test companyLink",
                "test jobLink",
                "test notes",
                1L
        );

        this.savedJobApplication = new JobApplication(
                "test jobTitle",
                "test companyName",
                savedStatus,
                "test companyLink",
                "test jobLink",
                "test notes",
                savedUser
        );

        this.jobApplicationToSave = new JobApplication(
                "test jobTitle",
                "test companyName",
                savedStatus,
                "test companyLink",
                "test jobLink",
                "test notes",
                savedUser
        );
    }

    @Test
    void canCreateAJobApplication() throws Exception {
        String requestJson = objectMapper.writeValueAsString(jobApplicationRequest);

        mockMvc.perform(post("/api/job_applications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("jobTitle").value(savedJobApplication.getJobTitle()))
                .andExpect(jsonPath("companyName").value(savedJobApplication.getCompanyName()))
                .andExpect(jsonPath("status.id").value(savedJobApplication.getStatus().getId()))
                .andExpect(jsonPath("companyLink").value(savedJobApplication.getCompanyLink()))
                .andExpect(jsonPath("jobLink").value(savedJobApplication.getJobLink()))
                .andExpect(jsonPath("notes").value(savedJobApplication.getNotes()))
                .andExpect(jsonPath("user.id").value(savedJobApplication.getUser().getId()))
                ;

        assertEquals(1, jobApplicationRepository.count());
    }

    @Test
    void canFindAJobApplicationById() throws Exception {
        jobApplicationRepository.save(jobApplicationToSave);
        mockMvc.perform(get("/api/job_applications/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("jobTitle").value(savedJobApplication.getJobTitle()))
                .andExpect(jsonPath("companyName").value(savedJobApplication.getCompanyName()))
                .andExpect(jsonPath("status.id").value(savedJobApplication.getStatus().getId()))
                .andExpect(jsonPath("companyLink").value(savedJobApplication.getCompanyLink()))
                .andExpect(jsonPath("jobLink").value(savedJobApplication.getJobLink()))
                .andExpect(jsonPath("notes").value(savedJobApplication.getNotes()))
                .andExpect(jsonPath("user.id").value(savedJobApplication.getUser().getId()))
        ;
    }

    @Test
    void canGetAllJobApplications() throws Exception {
        jobApplicationRepository.save(jobApplicationToSave);
        mockMvc.perform(get("/api/job_applications")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].jobTitle").value(savedJobApplication.getJobTitle()))
                .andExpect(jsonPath("$[0].companyName").value(savedJobApplication.getCompanyName()))
                .andExpect(jsonPath("$[0].status.id").value(savedJobApplication.getStatus().getId()))
                .andExpect(jsonPath("$[0].companyLink").value(savedJobApplication.getCompanyLink()))
                .andExpect(jsonPath("$[0].jobLink").value(savedJobApplication.getJobLink()))
                .andExpect(jsonPath("$[0].notes").value(savedJobApplication.getNotes()))
                .andExpect(jsonPath("$[0].user.id").value(savedJobApplication.getUser().getId()))
        ;
    }

    @Test
    void canUpdateAllFieldsForAJobApplication() throws Exception {
        jobApplicationRepository.save(jobApplicationToSave);

        Status updateStatus =  statusRepository.save(new Status("update status"));
        User updateUser = userRepository.save(new User(
                null,
                "update userName",
                "update firstName",
                "update lastName",
                "update jobTitle",
                "update email",
                "update password",
                savedRole
        ));
        String jobApplicationUpdateRequestJson = """
                {
                    "jobTitle": "update jobTitle",
                    "companyName": "update companyName",
                    "companyLink": "update companyLink",
                    "jobLink": "update jobLink",
                    "notes": "update notes",
                    "status": 2,
                    "app_user_id": 2
                }
        """;

        mockMvc.perform(patch("/api/job_applications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jobApplicationUpdateRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.jobTitle").value("update jobTitle"))
                .andExpect(jsonPath("$.companyName").value("update companyName"))
                .andExpect(jsonPath("$.status.id").value(updateStatus.getId()))
                .andExpect(jsonPath("$.companyLink").value("update companyLink"))
                .andExpect(jsonPath("$.jobLink").value("update jobLink"))
                .andExpect(jsonPath("$.notes").value("update notes"))
                .andExpect(jsonPath("$.user.id").value(updateUser.getId()))
        ;
    }

    @Test
    void canUpdateOneFieldForAJobApplication() throws Exception {
        jobApplicationRepository.save(jobApplicationToSave);

        String jobApplicationUpdateRequestJson = """
                {
                    "jobTitle": "update jobTitle"
                }
        """;

        mockMvc.perform(patch("/api/job_applications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jobApplicationUpdateRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.jobTitle").value("update jobTitle"))
                .andExpect(jsonPath("$.companyName").value(savedJobApplication.getCompanyName()))
                .andExpect(jsonPath("$.status.id").value(savedJobApplication.getStatus().getId()))
                .andExpect(jsonPath("$.companyLink").value(savedJobApplication.getCompanyLink()))
                .andExpect(jsonPath("$.jobLink").value(savedJobApplication.getJobLink()))
                .andExpect(jsonPath("$.notes").value(savedJobApplication.getNotes()))
                .andExpect(jsonPath("$.user.id").value(savedJobApplication.getUser().getId()))
        ;
    }

    @Test
    void canUpdateStatusFieldForAJobApplication() throws Exception {
        jobApplicationRepository.save(jobApplicationToSave);

        Status updateStatus =  statusRepository.save(new Status("update status"));

        String jobApplicationUpdateRequestJson = """
                {
                    "status": "2"
                }
        """;

        mockMvc.perform(patch("/api/job_applications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jobApplicationUpdateRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.jobTitle").value(savedJobApplication.getJobTitle()))
                .andExpect(jsonPath("$.companyName").value(savedJobApplication.getCompanyName()))
                .andExpect(jsonPath("$.status.id").value(updateStatus.getId()))
                .andExpect(jsonPath("$.companyLink").value(savedJobApplication.getCompanyLink()))
                .andExpect(jsonPath("$.jobLink").value(savedJobApplication.getJobLink()))
                .andExpect(jsonPath("$.notes").value(savedJobApplication.getNotes()))
                .andExpect(jsonPath("$.user.id").value(savedJobApplication.getUser().getId()))
        ;
    }

    @Test
    void canUpdateUserFieldForAJobApplication() throws Exception {
        jobApplicationRepository.save(jobApplicationToSave);

        User updateUser = userRepository.save(new User(
                null,
                "update userName",
                "update firstName",
                "update lastName",
                "update jobTitle",
                "update email",
                "update password",
                savedRole
        ));

        String jobApplicationUpdateRequestJson = """
                {
                    "app_user_id": "2"
                }
        """;

        mockMvc.perform(patch("/api/job_applications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jobApplicationUpdateRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.jobTitle").value(savedJobApplication.getJobTitle()))
                .andExpect(jsonPath("$.companyName").value(savedJobApplication.getCompanyName()))
                .andExpect(jsonPath("$.status.id").value(savedJobApplication.getStatus().getId()))
                .andExpect(jsonPath("$.companyLink").value(savedJobApplication.getCompanyLink()))
                .andExpect(jsonPath("$.jobLink").value(savedJobApplication.getJobLink()))
                .andExpect(jsonPath("$.notes").value(savedJobApplication.getNotes()))
                .andExpect(jsonPath("$.user.id").value(updateUser.getId()))
        ;
    }

    @Test
    void deleteAJobApplication() throws Exception {
        jobApplicationRepository.save(jobApplicationToSave);

        assertEquals(1, jobApplicationRepository.count());

        mockMvc.perform(delete("/api/job_applications/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(0, jobApplicationRepository.count());
    }
  
}