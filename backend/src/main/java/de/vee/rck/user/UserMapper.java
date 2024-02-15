package de.vee.rck.user.dto;

import de.vee.rck.user.AppUser;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel="spring")
public interface UserMapper {

    @Mappings({
            @Mapping(target = "userName"),
            @Mapping(target = "email")
    })
    AppUser userDetailsToAppUser(UserDetails details);

    @InheritInverseConfiguration
    UserDetails appUserToUserDetails(AppUser user);
}
