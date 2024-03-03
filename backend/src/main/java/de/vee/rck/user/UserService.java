package de.vee.rck.user;

import de.vee.rck.user.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
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
    public Collection<AppUserPreview> collectAllUsers(){
        return StreamSupport.stream(userRepo.findAll().spliterator(),false)
                .map(userMapper::appUserToAppUserPreview)
                .toList();
    }

    @Transactional
    public Optional<UserQueryResponse> loadAppUserDetails(Long id){
        var user = userRepo.findById(id);
        return user.map(appUser -> userMapper.appUserToUserQueryResponse(appUser));
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

    @Transactional
    AppUser registerNewUser(AppUser user, boolean allowDuplicateEmail) throws UserCRUDError {
        boolean nameAvailable = false;
        boolean emailAvailable = false;
        nameAvailable = userRepo.findByUserName(user.getUserName()) == null;
        emailAvailable = allowDuplicateEmail || userRepo.findByEmail(user.getEmail()) == null;

        if (!(nameAvailable && emailAvailable)){
            throw new UserCRUDError(!nameAvailable, !emailAvailable);
        }
        return userRepo.save(user);
    }
}
