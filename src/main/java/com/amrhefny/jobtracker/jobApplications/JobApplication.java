package com.amrhefny.jobtracker.jobApplications;

import com.amrhefny.jobtracker.users.User;
import jakarta.persistence.*;

@Entity
@Table(name = "job_application")
public class JobApplication {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String jobTitle;
    @Column(nullable = false)
    private String companyName;
    @Column(nullable = false)
    private String status;
    @Column(nullable = true)
    private String companyLink;
    @Column(nullable = false)
    private String jobLink;
    @Column(nullable = true)
    private String notes;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private User user;

    public JobApplication(String jobTitle, String companyName, String status, String companyLink, String jobLink, String notes, User user) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.status = status;
        this.companyLink = companyLink;
        this.jobLink = jobLink;
        this.notes = notes;
        this.user = user;
    }

    public JobApplication(Long id, String jobTitle, String companyName, String status, String companyLink, String jobLink, String notes, User user) {
        this(jobTitle, companyName, status, companyLink, jobLink, notes, user);
        this.id = id;
    }

    public JobApplication() {

    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getStatus() {
        return status;
    }

    public String getCompanyLink() {
        return companyLink;
    }

    public String getJobLink() {
        return jobLink;
    }

    public String getNotes() {
        return notes;
    }

    public User getUser() {
        return user;
    }
}
