package de.vee.rck.user.dto;

import java.util.Collection;

public interface UserCard {
    public String getUserName(); //contains only the user name at this point
    public Collection<String> getRoles();
}
