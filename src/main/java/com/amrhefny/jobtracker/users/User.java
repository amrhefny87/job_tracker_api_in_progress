package com.amrhefny.jobtracker.users;

import com.amrhefny.jobtracker.jobApplications.JobApplication;
import com.amrhefny.jobtracker.roles.Role;
import com.amrhefny.jobtracker.statuses.Status;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String jobTitle;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<JobApplication> jobApplications = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    public User(String userName, String firstName, String lastName, String jobTitle, String email, String password, Role role) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(Long id, String userName, String firstName, String lastName, String jobTitle, String email, String password, Role role) {
        this(userName, firstName, lastName, jobTitle, email, password, role);
        this.id = id;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
