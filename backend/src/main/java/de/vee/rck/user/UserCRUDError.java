package de.vee.rck.user;

import lombok.Getter;

@Getter
public class UserCRUDError extends Throwable {

    private final boolean nameTaken;
    private final boolean emailTaken;

    UserCRUDError(boolean nameTaken, boolean emailTaken){
        this.nameTaken = nameTaken;
        this.emailTaken = emailTaken;
    }
}
