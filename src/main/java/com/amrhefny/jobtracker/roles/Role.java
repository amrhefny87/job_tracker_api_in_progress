package com.amrhefny.jobtracker.roles;

import com.amrhefny.jobtracker.jobApplications.JobApplication;
import com.amrhefny.jobtracker.users.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Role {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false)
    private String role;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<User> users = new HashSet<>();

    public Role(String status) {
        this.role = status;
    }

    public Role() {

    }

    public Role(Long id, String status) {
        this(status);
        this.id = id;
    }

    public String getRole() {
        return role;
    }
}
