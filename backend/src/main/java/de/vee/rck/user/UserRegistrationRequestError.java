package de.vee.rck.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UserRegistrationRequestError extends Throwable{

    private final boolean emailTaken;
    private final boolean nameTaken;
    UserRegistrationRequestError(boolean emailTaken, boolean nameTaken){
        this.emailTaken = emailTaken;
        this.nameTaken = nameTaken;
    }
}
