package com.amrhefny.jobtracker.roles.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class RoleExistException extends RuntimeException{
    public RoleExistException (String message){
        super(message);
    }
}
