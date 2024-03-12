package vee.rck.user;

import vee.rck.user.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Maps AppUser entities to a varity of partial projections of the entity
 */
@Mapper(componentModel="spring")
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleRepository userRoleRepo;

    /*
    @Mappings({
            @Mapping(target = "userName"),
            @Mapping(target = "email"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "recipes", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "roles"),
            @Mapping(target = "tokenExpired", ignore = true)
    })
    AppUser userDetailsToAppUser(AppUserDetails details);
    */

    @Mappings({
            @Mapping(target = "userName"),
            @Mapping(target = "email"),
            @Mapping(target = "roles"),
            @Mapping(target = "enabled")
    })
    public abstract UserQueryResponse appUserToUserQueryResponse(AppUser user);

    @Mappings({
            @Mapping(target = "userName"),
            @Mapping(target = "enabled"),
            @Mapping(target = "roles")
    })
    public abstract AppUserPreview appUserToAppUserPreview(AppUser user);


    /**
     *      copies non null properties to user
     */
    public void updateAppUserFromAppUserDetails(AppUser user, AppUserDetails details, String newPassword) {
        if (details.getUserName() != null)
            user.setUserName(details.getUserName());
        if (details.getEmail() != null)
            user.setEmail(details.getEmail());
        if (details.getEnabled() != null)
            user.setEnabled(details.getEnabled());
        if (newPassword != null)
            user.setPassword(passwordEncoder.encode(newPassword));
        if (details.getRoles() != null)
            user.setRoles(new ArrayList<>());
        for (String role : details.getRoles()){
            addRoleToAppUser(user, role);
        }
    }

    /** loads role from repository and adds it to user*/
    void addRoleToAppUser(AppUser user, String roleName){
        Optional<UserRole> role = userRoleRepo.findByName(roleName);
        if (role.isPresent()){
            user.getRoles().add(role.get());
        } else {
            throw new UserRoleNotFoundError(MessageFormat.format("addRole(..) was unable to find role with name {0}", roleName));
        }
    }

    public Collection<String> map(Collection<UserRole> value) {
        return value.stream().map(UserRole::getName).toList();
    }
}
