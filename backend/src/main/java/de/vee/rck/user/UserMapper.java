package de.vee.rck.user;

import de.vee.rck.user.AppUser;
import de.vee.rck.user.dto.UserDetails;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "userName"),
            @Mapping(target = "email"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "recipes", ignore = true),
            @Mapping(target = "password", ignore = true),
            @Mapping(target = "enabled", ignore = true),
            @Mapping(target = "roles", ignore = true),
            @Mapping(target = "tokenExpired", ignore = true)
    })
    AppUser userDetailsToAppUser(UserDetails details);

    @InheritInverseConfiguration
    UserDetails appUserToUserDetails(AppUser user);
}
