package vee.rck.security.dto;

public interface AppUserDetails extends AppUserAbstract {
    String getEmail();
    Boolean getEnabled();
}
