package com.amrhefny.jobtracker.users;

import com.amrhefny.jobtracker.util.PatchField;
import com.amrhefny.jobtracker.util.PatchFieldDeserializer;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class UserPatchDTO {
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<String> userName = PatchField.notProvided();
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<String> firstName = PatchField.notProvided();
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<String> lastName = PatchField.notProvided();
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<String> jobTitle = PatchField.notProvided();
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<String> email = PatchField.notProvided();
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<String> password = PatchField.notProvided();
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<Long> role = PatchField.notProvided();

    public UserPatchDTO() {}

    public UserPatchDTO(PatchField<String> userName, PatchField<String> firstName, PatchField<String> lastName, PatchField<String> jobTitle, PatchField<String> email, PatchField<String> password, PatchField<Long> role) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobTitle = jobTitle;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setUserName(PatchField<String> userName) {
        this.userName = userName;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setFirstName(PatchField<String> firstName) {
        this.firstName = firstName;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setLastName(PatchField<String> lastName) {
        this.lastName = lastName;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setJobTitle(PatchField<String> jobTitle) {
        this.jobTitle = jobTitle;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setEmail(PatchField<String> email) {
        this.email = email;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setPassword(PatchField<String> password) {
        this.password = password;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setRole(PatchField<Long> role) {
        this.role = role;
    }

    public PatchField<String> getUserName() {
        return userName;
    }

    public PatchField<String> getFirstName() {
        return firstName;
    }

    public PatchField<String> getLastName() {
        return lastName;
    }

    public PatchField<String> getJobTitle() {
        return jobTitle;
    }

    public PatchField<String> getEmail() {
        return email;
    }

    public PatchField<String> getPassword() {
        return password;
    }

    public PatchField<Long> getRole() {
        return role;
    }
}
