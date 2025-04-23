package com.amrhefny.jobtracker.jobApplications;

import com.amrhefny.jobtracker.jobApplications.exceptions.JobApplicationExistException;
import com.amrhefny.jobtracker.jobApplications.exceptions.JobApplicationNotFoundException;
import com.amrhefny.jobtracker.statuses.Status;
import com.amrhefny.jobtracker.statuses.StatusRepository;
import com.amrhefny.jobtracker.statuses.exceptions.StatusNotFoundException;
import com.amrhefny.jobtracker.users.User;
import com.amrhefny.jobtracker.users.UserRepository;
import com.amrhefny.jobtracker.users.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;


    public JobApplicationService(JobApplicationRepository jobApplicationRepository, StatusRepository statusRepository, UserRepository userRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
    }

    public JobApplication createJobApplication(JobApplicationDTO jobApplicationRequest){
        if(jobApplicationRepository.existsByJobLink(jobApplicationRequest.jobLink())){
            throw new JobApplicationExistException("JobApplication already exists");
        }

        Status jobApplicationRequestStatus = statusRepository.findById(jobApplicationRequest.status())
                .orElseThrow(()-> new StatusNotFoundException("Status with id " + jobApplicationRequest.status() + " not found"));

        User jobApplicationRequestUser = userRepository.findById(jobApplicationRequest.app_user_id())
                .orElseThrow(()-> new UserNotFoundException("User with id " + jobApplicationRequest.app_user_id() + " not found"));

        JobApplication jobApplicationToSave = new JobApplication(
                jobApplicationRequest.jobTitle(),
                jobApplicationRequest.companyName(),
                jobApplicationRequestStatus,
                jobApplicationRequest.companyLink(),
                jobApplicationRequest.jobLink(),
                jobApplicationRequest.notes(),
                jobApplicationRequestUser
        );

        return jobApplicationRepository.save(jobApplicationToSave);
    }

    public JobApplication getAJobApplication(Long id){
        Optional<JobApplication> returnedJobApplication = jobApplicationRepository.findById(id);

        if(returnedJobApplication.isEmpty()){
            throw new JobApplicationNotFoundException("JobApplication with id " + id + " not found");
        }

        return returnedJobApplication.get();
    }

    public List<JobApplication> getAllJobApplications(){
        return jobApplicationRepository.findAll();
    }

    public JobApplication updateAJobApplication(JobApplicationPatchDTO jobApplicationPatchRequest, Long id){
        Optional<JobApplication> returnedJobApplication = jobApplicationRepository.findById(id);

        if(returnedJobApplication.isEmpty()){
            throw new JobApplicationNotFoundException("JobApplication with id " + id + " not found");
        }

        JobApplication jobApplicationToBeUpdated = returnedJobApplication.get();

        jobApplicationPatchRequest.getJobTitle().ifProvided(jobApplicationToBeUpdated::setJobTitle);
        jobApplicationPatchRequest.getCompanyName().ifProvided(jobApplicationToBeUpdated::setCompanyName);
        jobApplicationPatchRequest.getCompanyLink().ifProvided(jobApplicationToBeUpdated::setCompanyLink);
        jobApplicationPatchRequest.getJobLink().ifProvided(jobApplicationToBeUpdated::setJobLink);
        jobApplicationPatchRequest.getNotes().ifProvided(jobApplicationToBeUpdated::setNotes);

        jobApplicationPatchRequest.getStatus().ifProvided(statusId->{
            Status status = statusRepository.findById(statusId)
                    .orElseThrow(()-> new StatusNotFoundException("Status with id " + jobApplicationPatchRequest.getStatus() + " not found"));
            jobApplicationToBeUpdated.setStatus(status);
        });

        jobApplicationPatchRequest.getApp_user_id().ifProvided(userId->{
            User user = userRepository.findById(userId)
                    .orElseThrow(()-> new UserNotFoundException("User with id " + jobApplicationPatchRequest.getApp_user_id() + " not found"));
            jobApplicationToBeUpdated.setUser(user);
        });

        return jobApplicationRepository.save(jobApplicationToBeUpdated);
    }

    public void deleteAJobApplication(Long id){
        Optional<JobApplication> returnedJobApplication = jobApplicationRepository.findById(id);

        if(returnedJobApplication.isEmpty()){
            throw new JobApplicationNotFoundException("JobApplication with id " + id + " not found");
        }

        jobApplicationRepository.deleteById(id);
    }
}
