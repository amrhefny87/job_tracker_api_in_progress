package com.amrhefny.jobtracker.statuses;

import com.amrhefny.jobtracker.statuses.exceptions.StatusExistException;
import com.amrhefny.jobtracker.statuses.exceptions.StatusNotFoundException;
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
class StatusServiceTest {
    @Mock
    StatusRepository statusRespository;

    @InjectMocks
    StatusService statusService;

    StatusDTO statusRequest;
    Status savedStatus;

    @BeforeEach
    public void beforeEachTest(){
        this.statusRequest = new StatusDTO("test status");
        this.savedStatus = new Status(1L, "test status");
    }

    @Test
    void testAStatusCanBeCreated(){
        Mockito.when(statusRespository.save(any(Status.class))).thenReturn(savedStatus);

        Status statusResponse = statusService.createStatus(statusRequest);

        assertEquals(1L, statusResponse.getId());
        assertEquals("test status", statusResponse.getStatus());
        verify(statusRespository).save(any(Status.class));
    }

    @Test
    void testAStatusCanNotBeCreatedWithAnExistingStatus(){
        Mockito.when((statusRespository.existsByStatus(savedStatus.getStatus()))).thenReturn(true);

        Exception exception = assertThrows(StatusExistException.class, ()-> statusService.createStatus(statusRequest));

        assertEquals("Status already exists", exception.getMessage());
        verify(statusRespository, never()).save(any(Status.class));
    }

    @Test
    void testAStatusCanBeRetrievedByItsId(){
        Mockito.when(statusRespository.findById(1L)).thenReturn(Optional.of(savedStatus));

        Status statusResponse = statusService.getAStatus(1L);

        assertEquals(savedStatus, statusResponse);
    }

    @Test
    void testAnExceptionThrownWhenStatusNotFound(){
        Mockito.when((statusRespository.findById(2L))).thenReturn(Optional.empty());
        Exception exception = assertThrows(StatusNotFoundException.class, ()-> statusService.getAStatus(2L));

        assertEquals("The status with id 2 not found", exception.getMessage());
        verify(statusRespository, Mockito.times(1)).findById(any(Long.class));
    }

    @Test
    void testTheListOfStatusesCanBeRetrieved(){
        List<Status> statusesList = List.of(savedStatus);

        Mockito.when(statusRespository.findAll()).thenReturn(statusesList);

        List<Status> statusResponse = statusRespository.findAll();

        assertEquals(statusesList, statusResponse);
    }

    @Test
    void testAStatusCanBeUpdated(){
        Mockito.when(statusRespository.findById(1L)).thenReturn(Optional.of(savedStatus));
        Mockito.when(statusRespository.save(any(Status.class))).thenReturn(savedStatus);

        StatusDTO statusRequest = new StatusDTO("update status");

        Optional<Status> statusResponse = statusService.updateAStatus(statusRequest, 1L);

        assertTrue(statusResponse.isPresent());

        Status updatedStatus = statusResponse.get();

        assertEquals("update status", updatedStatus.getStatus());
    }

    @Test
    void testAStatusCanNotBeUpdatedIfDoesNotExist(){
        Mockito.when(statusRespository.findById(1L)).thenReturn(Optional.empty());

        StatusDTO statusRequest = new StatusDTO("update status");

        Exception exception = assertThrows(StatusNotFoundException.class, ()-> statusService.updateAStatus(statusRequest, 1L));

        assertEquals("The status with id 1 not found", exception.getMessage());
        verify(statusRespository, never()).save(any(Status.class));
    }
}