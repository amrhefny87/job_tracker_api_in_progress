package com.amrhefny.jobtracker.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class UserExistException extends RuntimeException{
    public UserExistException(String message){
        super(message);
    }
}
