package com.amrhefny.jobtracker.statuses.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class StatusExistException extends RuntimeException{
    public StatusExistException (String message){
        super(message);
    }
}
