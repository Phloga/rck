package vee.rck.security.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserUpdateRequest implements AppUserDetails {
    @NotNull
    private String userName;
    private String email;
    private Collection<String> roles;
    private Boolean enabled;
    private String password;
}
