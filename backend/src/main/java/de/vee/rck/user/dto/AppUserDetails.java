package de.vee.rck.user.dto;

import java.util.Collection;

public interface AppUserDetails extends UserCard{
    String getEmail();
    Boolean getEnabled();
}
