package de.vee.rck.user;

public class UserUpdateError extends RuntimeException{
    UserUpdateError(String msg, Throwable cause){
        super(msg, cause);
    }
}
