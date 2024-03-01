package de.vee.rck.user.dto;

import lombok.Data;

import java.util.Collection;

/**
 * Contains user Information for display on the site
 */
@Data
public class AppUserPreviewRecord {
    private String userName;
    private Collection<String> roles;
    private boolean enabled;
}
