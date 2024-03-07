package de.vee.rck.user;

import de.vee.rck.user.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class UserService {
    private AppUserRepository userRepo;
    private UserRoleRepository userRoleRepo;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;

    private static UserQueryResponse anonymousUser;

    @Transactional
    public Collection<String> availableUserRoles() {
        return StreamSupport.stream(userRoleRepo.findAll().spliterator(),false)
                .map(UserRole::getName)
                .toList();
    }

    @Transactional
    public Collection<AppUserPreview> collectAllUsers(){
        return StreamSupport.stream(userRepo.findAll().spliterator(),false)
                .map(userMapper::appUserToAppUserPreview)
                .toList();
    }

    public UserCard anonymousUserCard(){
        if (anonymousUser == null){
            anonymousUser = new UserQueryResponse("anonymous", "", new ArrayList<>(), false);
        }
        return anonymousUser;
    }

    @Transactional
    public Optional<UserQueryResponse> loadAppUserDetailsByName(String userName){
        var user = userRepo.findByUserName( userName);
        return user.map(appUser -> userMapper.appUserToUserQueryResponse(appUser));
    }

    @Transactional
    public Optional<AppUser> findUserByName(String name){
        return userRepo.findByUserName(name);
    }

    /// check whether a user with role admin exists
    public boolean anyAdminExists(){
        var adminRole = userRoleRepo.findByName("ROLE_ADMIN");
        return userRepo.countByRole(adminRole.getId()) > 0;
    }

    public AppUser createAppUser(UserUpdateRequest user) {
        if (user.getUserName() == null)
            throw new IllegalArgumentException("Attempted to create user without name");

        if (userRepo.findByUserName(user.getUserName()).isPresent()) {
            throw new IllegalArgumentException("Attempted to create already existing user");
        }
        AppUser newUser = new AppUser();
        userMapper.updateAppUserFromAppUserDetails(newUser, user, user.getPassword());
        return userRepo.save(newUser);
    }

    /**
     * Business logic for a password reset
     * @return true if successful
     */
    @Transactional
    public boolean updatePassword(String userName, String oldPassword, String newPassword){
        var user = userRepo.findByUserName(userName).orElseThrow();
        if (passwordEncoder.matches(oldPassword, user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
            return true;
        }
        return false;
    }

    @Transactional
    public AppUser updateAppUser(UserUpdateRequest user, String name) {
        var userEntity = userRepo.findByUserName(name);
        if (userEntity.isEmpty()){
            // create new user
            if (user.getUserName().equals(name)){
                return createAppUser(user);
            } else {
                throw new IllegalArgumentException("User name for new user mismatches with parameter name");
            }
        } else {
            // update exiting user
            AppUser updatedUser = userEntity.get();
            userMapper.updateAppUserFromAppUserDetails(updatedUser, user, user.getPassword());
            return userRepo.save(updatedUser);
        }
    }

    /*
    @Transactional
    AppUser registerNewUser(AppUser user, boolean allowDuplicateEmail) throws UserCRUDError {
        boolean nameAvailable = false;
        boolean emailAvailable = false;
        nameAvailable = userRepo.findByUserName(user.getUserName()).isEmpty();
        emailAvailable = allowDuplicateEmail || userRepo.findByEmail(user.getEmail()).isEmpty();

        if (!(nameAvailable && emailAvailable)){
            throw new UserCRUDError(!nameAvailable, !emailAvailable);
        }
        return userRepo.save(user);
    }*/
}
