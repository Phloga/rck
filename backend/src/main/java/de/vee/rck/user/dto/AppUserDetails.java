package de.vee.rck.user.dto;

public interface AppUserDetails extends AppUserAbstract {
    String getEmail();
    Boolean getEnabled();
}
