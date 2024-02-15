package de.vee.rck.user;

public class RoleNotFoundError extends RuntimeException {
    public RoleNotFoundError(String message){
        super(message);
    }
}
