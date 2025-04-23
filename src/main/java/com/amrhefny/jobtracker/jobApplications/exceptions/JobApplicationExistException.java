package com.amrhefny.jobtracker.jobApplications.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class JobApplicationExistException extends RuntimeException{
    public JobApplicationExistException (String message){
        super(message);
    }
}
