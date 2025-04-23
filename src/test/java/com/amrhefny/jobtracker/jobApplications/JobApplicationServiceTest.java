package com.amrhefny.jobtracker.jobApplications;

import com.amrhefny.jobtracker.jobApplications.exceptions.JobApplicationExistException;
import com.amrhefny.jobtracker.jobApplications.exceptions.JobApplicationNotFoundException;
import com.amrhefny.jobtracker.roles.Role;
import com.amrhefny.jobtracker.statuses.Status;
import com.amrhefny.jobtracker.statuses.StatusDTO;
import com.amrhefny.jobtracker.statuses.StatusRepository;
import com.amrhefny.jobtracker.users.User;
import com.amrhefny.jobtracker.users.UserDTO;
import com.amrhefny.jobtracker.users.UserRepository;
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
class JobApplicationServiceTest {
    @Mock
    JobApplicationRepository jobApplicationRepository;

    @Mock
    StatusRepository statusRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    JobApplicationService jobApplicationService;

    Role savedRole = new Role(1L, "test role");

    Status savedStatus;
    StatusDTO statusRequest;

    User savedUser;
    UserDTO userRequest;

    JobApplicationDTO jobApplicationRequest;
    JobApplication savedJobApplication;

    @BeforeEach
    public void beforeEachTest(){
        this.statusRequest = new StatusDTO("test status");
        this.savedStatus = new Status(1L, "test status");

        this.userRequest = new UserDTO(
                "test userName",
                "test firstName",
                "test lastName",
                "test jobTitle",
                "test email",
                "test password",
                1L
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
    }

    @Test
    void testAJobApplicationCanBeCreated(){
        Mockito.when(statusRepository.findById(savedStatus.getId())).thenReturn(Optional.of(savedStatus));
        Mockito.when(userRepository.findById(savedUser.getId())).thenReturn(Optional.of(savedUser));

        Mockito.when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(savedJobApplication);

        JobApplication jobApplicationResponse = jobApplicationService.createJobApplication(jobApplicationRequest);

        assertEquals(savedJobApplication, jobApplicationResponse);
        verify(jobApplicationRepository).save(any(JobApplication.class));
    }

    @Test
    void testAJobApplicationCanNotBeCreatedWithExistingJobLink(){
        Mockito.when(jobApplicationRepository.existsByJobLink(savedJobApplication.getJobLink())).thenReturn(true);

        Exception exception = assertThrows(JobApplicationExistException.class, ()-> jobApplicationService.createJobApplication(jobApplicationRequest));

        assertEquals("JobApplication already exists", exception.getMessage());
        verify(jobApplicationRepository, never()).save(any(JobApplication.class));
    }

    @Test
    void testAJobApplicationCanBeRetrievedByItsId(){
        Mockito.when(jobApplicationRepository.findById(1L)).thenReturn(Optional.of(savedJobApplication));

        JobApplication jobApplicationResponse = jobApplicationService.getAJobApplication(1L);

        assertEquals(savedJobApplication, jobApplicationResponse);
    }

    @Test
    void testAnExceptionThrownWhenJobApplicationNotFound(){
        Mockito.when(jobApplicationRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(JobApplicationNotFoundException.class, ()-> jobApplicationService.getAJobApplication(2L));

        assertEquals("JobApplication with id 2 not found", exception.getMessage());

        verify(jobApplicationRepository, Mockito.times(1)).findById(any(Long.class));
    }

    @Test
    void testTheListOfJobApplicationsCanBeRetrieved(){
        List<JobApplication> jobApplicationsList = List.of(savedJobApplication);

        Mockito.when(jobApplicationRepository.findAll()).thenReturn(jobApplicationsList);

        List<JobApplication> jobApplicationResponse = jobApplicationService.getAllJobApplications();

        assertEquals(jobApplicationsList,jobApplicationResponse);
    }

    @Test
    void testAllJobApplicationFieldsCanBeUpdated(){
        Status updateStatus = new Status(2L,"update status");
        User updateUser = new User(
                2L,
                "update userName",
                "update firstName",
                "update lastName",
                "update jobTitle",
                "update email",
                "update password",
                savedRole

        );

        Mockito.when(statusRepository.findById(2L)).thenReturn(Optional.of(updateStatus));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(updateUser));
        Mockito.when(jobApplicationRepository.findById(1L)).thenReturn(Optional.of(savedJobApplication));
        Mockito.when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(savedJobApplication);

        JobApplicationPatchDTO jobApplicationPatchRequestAllFields = new JobApplicationPatchDTO(
                PatchField.of("update jobTitle"),
                PatchField.of("update companyName"),
                PatchField.of("update companyLink"),
                PatchField.of("update jobLink"),
                PatchField.of("update notes"),
                PatchField.of(2L),
                PatchField.of(2L)
        );

        JobApplication jobApplicationResponse = jobApplicationService.updateAJobApplication(jobApplicationPatchRequestAllFields, 1L);

        assertEquals("update jobTitle", jobApplicationResponse.getJobTitle());
        assertEquals("update companyName", jobApplicationResponse.getCompanyName());
        assertEquals("update companyLink", jobApplicationResponse.getCompanyLink());
        assertEquals("update jobLink", jobApplicationResponse.getJobLink());
        assertEquals("update notes", jobApplicationResponse.getNotes());
        assertEquals(updateStatus, jobApplicationResponse.getStatus());
        assertEquals(updateUser, jobApplicationResponse.getUser());
    }

    @Test
    void testOneJobApplicationFieldCanBeUpdated(){
        Mockito.when(jobApplicationRepository.findById(1L)).thenReturn(Optional.of(savedJobApplication));
        Mockito.when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(savedJobApplication);

        JobApplicationPatchDTO jobApplicationPatchRequestOneField = new JobApplicationPatchDTO();
        jobApplicationPatchRequestOneField.setJobTitle(PatchField.of("update jobTitle"));


        JobApplication jobApplicationResponse = jobApplicationService.updateAJobApplication(jobApplicationPatchRequestOneField, 1L);

        assertEquals("update jobTitle", jobApplicationResponse.getJobTitle());
        assertEquals("test companyName", jobApplicationResponse.getCompanyName());
        assertEquals("test companyLink", jobApplicationResponse.getCompanyLink());
        assertEquals("test jobLink", jobApplicationResponse.getJobLink());
        assertEquals("test notes", jobApplicationResponse.getNotes());
        assertEquals(savedStatus, jobApplicationResponse.getStatus());
        assertEquals(savedUser, jobApplicationResponse.getUser());
    }

    @Test
    void testOnlyStatusFieldCanBeUpdated(){
        Status updateStatus = new Status(2L,"update status");

        Mockito.when(statusRepository.findById(2L)).thenReturn(Optional.of(updateStatus));
        Mockito.when(jobApplicationRepository.findById(1L)).thenReturn(Optional.of(savedJobApplication));
        Mockito.when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(savedJobApplication);

        JobApplicationPatchDTO jobApplicationPatchRequestStatusField = new JobApplicationPatchDTO();
        jobApplicationPatchRequestStatusField.setStatus(PatchField.of(2L));


        JobApplication jobApplicationResponse = jobApplicationService.updateAJobApplication(jobApplicationPatchRequestStatusField, 1L);

        assertEquals("test jobTitle", jobApplicationResponse.getJobTitle());
        assertEquals("test companyName", jobApplicationResponse.getCompanyName());
        assertEquals("test companyLink", jobApplicationResponse.getCompanyLink());
        assertEquals("test jobLink", jobApplicationResponse.getJobLink());
        assertEquals("test notes", jobApplicationResponse.getNotes());
        assertEquals(updateStatus, jobApplicationResponse.getStatus());
        assertEquals(savedUser, jobApplicationResponse.getUser());
    }

    @Test
    void testOnlyUserFieldCanBeUpdated(){
        User updateUser = new User(
                2L,
                "update userName",
                "update firstName",
                "update lastName",
                "update jobTitle",
                "update email",
                "update password",
                savedRole

        );

        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(updateUser));
        Mockito.when(jobApplicationRepository.findById(1L)).thenReturn(Optional.of(savedJobApplication));
        Mockito.when(jobApplicationRepository.save(any(JobApplication.class))).thenReturn(savedJobApplication);

        JobApplicationPatchDTO jobApplicationPatchRequestUserField = new JobApplicationPatchDTO();
        jobApplicationPatchRequestUserField.setApp_user_id(PatchField.of(2L));


        JobApplication jobApplicationResponse = jobApplicationService.updateAJobApplication(jobApplicationPatchRequestUserField, 1L);

        assertEquals("test jobTitle", jobApplicationResponse.getJobTitle());
        assertEquals("test companyName", jobApplicationResponse.getCompanyName());
        assertEquals("test companyLink", jobApplicationResponse.getCompanyLink());
        assertEquals("test jobLink", jobApplicationResponse.getJobLink());
        assertEquals("test notes", jobApplicationResponse.getNotes());
        assertEquals(savedStatus, jobApplicationResponse.getStatus());
        assertEquals(updateUser, jobApplicationResponse.getUser());
    }

    @Test
    void testAJobApplicationCanBeDeletedByItsId(){
        Mockito.when(jobApplicationRepository.findById(1L)).thenReturn(Optional.of(savedJobApplication));

        jobApplicationService.deleteAJobApplication(1L);

        verify(jobApplicationRepository, Mockito.times(1)).deleteById(1L);
    }
  
}