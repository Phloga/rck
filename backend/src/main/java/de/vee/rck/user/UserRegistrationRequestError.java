package de.vee.rck.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UserRegistrationRequestError extends Throwable {

    private final boolean nameTaken;
    private final boolean emailTaken;

    UserRegistrationRequestError(boolean nameTaken, boolean emailTaken){
        this.nameTaken = nameTaken;
        this.emailTaken = emailTaken;
    }
}
