package vee.rck.security;

public class UserUpdateError extends RuntimeException{
    UserUpdateError(String msg, Throwable cause){
        super(msg, cause);
    }
}
