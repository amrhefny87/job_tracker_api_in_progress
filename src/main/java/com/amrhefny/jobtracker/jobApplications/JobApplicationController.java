package com.amrhefny.jobtracker.jobApplications;

import com.amrhefny.jobtracker.users.User;
import com.amrhefny.jobtracker.users.UserDTO;
import com.amrhefny.jobtracker.users.UserPatchDTO;
import com.amrhefny.jobtracker.users.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/job_applications")
@CrossOrigin("*")
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    public ResponseEntity<JobApplication> createAJobApplication(@RequestBody JobApplicationDTO jobApplicationRequest){
        return ResponseEntity.status(201).body(jobApplicationService.createJobApplication(jobApplicationRequest));
    }

    @GetMapping
    public ResponseEntity<List<JobApplication>> findAllJobApplications(){
        return ResponseEntity.status(200).body(jobApplicationService.getAllJobApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> findAJobApplication(@PathVariable Long id){
        return ResponseEntity.status(200).body(jobApplicationService.getAJobApplication(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<JobApplication> updateAJobApplication(@RequestBody JobApplicationPatchDTO jobApplicationPatchRequest, @PathVariable Long id){
        return ResponseEntity.status(200).body(jobApplicationService.updateAJobApplication(jobApplicationPatchRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAJobApplication(@PathVariable Long id){
        jobApplicationService.deleteAJobApplication(id);
        return ResponseEntity.status(200).body("JobApplication with id " + id + " is deleted");
    }

}
