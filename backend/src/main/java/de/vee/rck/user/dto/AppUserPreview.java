package de.vee.rck.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

/**
 * Contains user Information for display on the site
 */
@Data
public class AppUserPreview implements UserCard {
    private String userName;
    private Collection<String> roles;
    private boolean enabled;
}
