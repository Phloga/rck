package vee.rck.user;

public class UserRoleNotFoundError extends RuntimeException {
    public UserRoleNotFoundError(String message){
        super(message);
    }
}
