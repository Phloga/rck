package de.vee.rck.user;

import de.vee.rck.user.dto.UserQueryResponse;
import de.vee.rck.user.dto.AppUserPreview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collection;

@Mapper(componentModel="spring")
public interface UserMapper {

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
    UserQueryResponse appUserToUserQueryResponse(AppUser user);

    @Mappings({
            @Mapping(target = "userName"),
            @Mapping(target = "enabled"),
            @Mapping(target = "roles")
    })
    AppUserPreview appUserToAppUserPreview(AppUser user);


    default Collection<String> map(Collection<UserRole> value) {
        return value.stream().map(UserRole::getName).toList();
    }
}
