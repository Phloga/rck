package vee.rck.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryResponse implements AppUserDetails {
    private String userName;
    private String email;
    private Collection<String> roles;
    private Boolean enabled;
}
