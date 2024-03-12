package vee.rck.security.dto;

import lombok.Data;

import java.util.Collection;

/**
 * Contains user Information for display on the site
 */
@Data
public class AppUserPreview implements AppUserAbstract {
    private String userName;
    private Collection<String> roles;
    private boolean enabled;
}
