package de.vee.rck.user;

import de.vee.rck.user.dto.*;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class UserService {
    private AppUserRepository userRepo;
    private UserRoleRepository userRoleRepo;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private Long adminRoleIdMem;

    public static final String adminRoleName = "ROLE_ADMIN";

    private static UserQueryResponse anonymousUser;

    public UserService(AppUserRepository userRepo, UserRoleRepository roleRepo, PasswordEncoder passwordEncoder, UserMapper userMapper){
        this.userRepo = userRepo;
        this.userRoleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    long getAdminRoleId(){
        if (adminRoleIdMem == null){
            adminRoleIdMem = userRoleRepo.findByName(adminRoleName).orElseThrow().getId();
        }
        return adminRoleIdMem;
    }

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

    public AppUserAbstract anonymousUserCard(){
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

    /// check how many users with role admin exist
    public int adminCount() {
        var x = userRepo.countByRole(getAdminRoleId());
        return x;
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

            boolean targetIsAdmin = updatedUser.getRoles().stream().anyMatch(
                    role -> {return  role.getId() == getAdminRoleId();});

            boolean targetIsLastAdmin = adminCount() == 1;

            userMapper.updateAppUserFromAppUserDetails(updatedUser, user, user.getPassword());
            if (targetIsAdmin && targetIsLastAdmin && !updatedUser.hasRole(getAdminRoleId())){
                throw new UserUpdateError("can't remove last admin",null);
            }
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
