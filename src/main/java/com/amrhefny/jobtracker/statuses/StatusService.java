package com.amrhefny.jobtracker.statuses;

import com.amrhefny.jobtracker.statuses.exceptions.StatusExistException;
import com.amrhefny.jobtracker.statuses.exceptions.StatusNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {
    private final StatusRepository statusRespository;

    public StatusService(StatusRepository statusRespository) {
        this.statusRespository = statusRespository;
    }

    public Status createStatus(StatusDTO statusRequest){
        if(statusRespository.existsByStatus(statusRequest.status())){
            throw new StatusExistException("Status already exists");
        }
        Status statusToBeSaved = new Status(statusRequest.status());

        return statusRespository.save(statusToBeSaved);
    }

    public Status getAStatus(Long id){
        Optional<Status> returnedStatus = statusRespository.findById(id);
        if(returnedStatus.isEmpty()){
            throw new StatusNotFoundException("The status with id " + id + " not found");
        }

        return returnedStatus.get();
    }

    public List<Status> getAllStatuses(){
        return statusRespository.findAll();
    }

    public Optional<Status> updateAStatus(StatusDTO statusRequest, Long id){
        Optional<Status> returnedStatus = statusRespository.findById(id);
        if(returnedStatus.isEmpty()){
            throw new StatusNotFoundException("The status with id " + id + " not found");
        }

        Status statusToBeUdpated = returnedStatus.get();
        statusToBeUdpated.setStatus(statusRequest.status());
        return Optional.of(statusRespository.save(statusToBeUdpated));
    }
}
