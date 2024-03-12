package vee.rck.security;

public class UserRoleNotFoundError extends RuntimeException {
    public UserRoleNotFoundError(String message){
        super(message);
    }
}
