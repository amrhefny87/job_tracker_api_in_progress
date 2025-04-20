package com.amrhefny.jobtracker.statuses;

import com.amrhefny.jobtracker.jobApplications.JobApplication;
import com.amrhefny.jobtracker.users.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "status", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<JobApplication> jobApplications = new HashSet<>();

    public Status(String status) {
        this.status = status;
    }

    public Status(Long id, String status) {
        this(status);
        this.id = id;
    }

    public Status() {

    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
