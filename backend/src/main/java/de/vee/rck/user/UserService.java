package de.vee.rck.user;

import de.vee.rck.user.dto.AppUserDetails;
import de.vee.rck.user.dto.UserCard;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private AppUserRepository userRepo;
    private UserRoleRepository userRoleRepo;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    private static AppUserDetails anonymousUser;

    public UserCard anonymousUserCard(){
        if (anonymousUser == null){
            anonymousUser = new AppUserDetails("anonymous", "", new ArrayList<>());
        }
        return anonymousUser;
    }

    @Transactional
    public Optional<UserCard> loadUserCard(String userName){
        var user = userRepo.findByUserName( userName);
        return user.map(appUser -> userMapper.appUserToUserDetails(appUser));
    }

    public AppUser makeAppUser(String name, String password, Collection<String> roles){
        AppUser user = new AppUser();
        user.setUserName(name);
        user.setPassword(passwordEncoder.encode(password));
        for (String role : roles){
            addRole(user, role);
        }
        return user;
    }

    /** loads role from repository and adds it to user*/
    void addRole(AppUser user, String roleName){
        UserRole role = userRoleRepo.findByName(roleName);
        if (role != null){
            user.getRoles().add(role);
        } else {
            throw new UserRoleNotFoundError(MessageFormat.format("addRole(..) was unable to find role with name {0}", roleName));
        }
    }

    AppUser registerNewUser(AppUser user) throws UserRegistrationRequestError {
        return registerNewUser(user, false);
    }
    @Transactional
    AppUser registerNewUser(AppUser user, boolean allowDuplicateEmail) throws UserRegistrationRequestError {
        boolean nameAvailable = false;
        boolean emailAvailable = false;
        nameAvailable = userRepo.findByUserName(user.getUserName()) == null;
        emailAvailable = allowDuplicateEmail || userRepo.findByEmail(user.getEmail()) == null;

        if (!(nameAvailable && emailAvailable)){
            throw new UserRegistrationRequestError(!nameAvailable, !emailAvailable);
        }
        return userRepo.save(user);
    }
}
