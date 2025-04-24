package com.amrhefny.jobtracker.jobApplications;

import com.amrhefny.jobtracker.util.PatchField;
import com.amrhefny.jobtracker.util.PatchFieldDeserializer;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class JobApplicationPatchDTO {
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<String> jobTitle = PatchField.notProvided();
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<String> companyName = PatchField.notProvided();
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<String> companyLink = PatchField.notProvided();
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<String> jobLink = PatchField.notProvided();
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<String> notes = PatchField.notProvided();
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<Long> status = PatchField.notProvided();
    @JsonDeserialize(using = PatchFieldDeserializer.class)
    private PatchField<Long> app_user_id = PatchField.notProvided();

    public JobApplicationPatchDTO(){}

    public JobApplicationPatchDTO(PatchField<String> jobTitle, PatchField<String> companyName, PatchField<String> companyLink, PatchField<String> jobLink, PatchField<String> notes, PatchField<Long> status, PatchField<Long> app_user_id) {
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.companyLink = companyLink;
        this.jobLink = jobLink;
        this.notes = notes;
        this.status = status;
        this.app_user_id = app_user_id;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setApp_user_id(PatchField<Long> app_user_id) {
        this.app_user_id = app_user_id;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setStatus(PatchField<Long> status) {
        this.status = status;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setNotes(PatchField<String> notes) {
        this.notes = notes;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setJobLink(PatchField<String> jobLink) {
        this.jobLink = jobLink;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setCompanyLink(PatchField<String> companyLink) {
        this.companyLink = companyLink;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setCompanyName(PatchField<String> companyName) {
        this.companyName = companyName;
    }

    @JsonSetter(nulls = Nulls.SET)
    public void setJobTitle(PatchField<String> jobTitle) {
        this.jobTitle = jobTitle;
    }

    public PatchField<String> getJobTitle() {
        return jobTitle;
    }

    public PatchField<String> getCompanyName() {
        return companyName;
    }

    public PatchField<String> getCompanyLink() {
        return companyLink;
    }

    public PatchField<String> getJobLink() {
        return jobLink;
    }

    public PatchField<String> getNotes() {
        return notes;
    }

    public PatchField<Long> getStatus() {
        return status;
    }

    public PatchField<Long> getApp_user_id() {
        return app_user_id;
    }
}
