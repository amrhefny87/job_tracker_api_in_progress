package com.amrhefny.jobtracker.statuses.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StatusNotFoundException extends RuntimeException{
    public StatusNotFoundException (String message){
        super(message);
    }
}
