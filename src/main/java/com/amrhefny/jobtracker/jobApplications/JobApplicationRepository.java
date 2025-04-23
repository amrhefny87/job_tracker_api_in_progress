package com.amrhefny.jobtracker.jobApplications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    Boolean existsByJobLink(String jobLink);
}
