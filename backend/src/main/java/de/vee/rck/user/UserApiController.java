package de.vee.rck.user;

import de.vee.rck.user.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.MessageFormat;
import java.util.Collection;

@Controller
@AllArgsConstructor
@RestController
public class UserApiController {

    private UserService userService;

    @GetMapping("/api/user/self")
    public AppUserAbstract sendCurrentUserInformation(Authentication authentication, HttpServletResponse response){
        if (authentication == null || !authentication.isAuthenticated()) {
            return userService.anonymousUserCard();
        }
        var userCard = userService.loadAppUserDetailsByName(authentication.getName());
        if (userCard.isPresent()){
            response.setHeader("Cache-Control", "private");
            return userCard.get();
        }
        response.setStatus(500);
        return null;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/user/new-password")
    public void updateUserPassword(@RequestBody PasswordReplaceRequest request, Authentication auth) {
        boolean success = userService.updatePassword(auth.getName(), request.getCurrentPassword(), request.getNewPassword());
        if (!success){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "wrong password");
        }
    }

    @GetMapping(path="/sec-api/user/p/{name}", produces="application/json")
    @PreAuthorize("isAuthenticated()")
    UserQueryResponse sendUserInformation(@PathVariable String name, HttpServletRequest request, HttpServletResponse response) {
        if (request.getUserPrincipal().getName().equals(name) || request.isUserInRole("ADMIN")){
            var user = userService.loadAppUserDetailsByName(name);
            if (user.isEmpty()){
                response.setStatus(HttpStatus.NOT_FOUND.value());
                return null;
            }
            response.setHeader("Cache-Control", "private");
            return user.get();
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return null;
    }

    @GetMapping(path="/sec-api/user/all", produces="application/json")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    Collection<AppUserPreview> sendAllUsers(){
        return userService.collectAllUsers();
    }


    @GetMapping(path="/sec-api/user/roles/all", produces="application/json")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    Collection<String> sendAllRoles() {
        return userService.availableUserRoles();
    }

    /*
    @PutMapping("/sec-api/users/p/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void receiveNewUserInformation(@RequestBody UserUpdateRequest userData, HttpServletRequest request, HttpServletResponse response){
        var newUser = userService.updateAppUser(userData, userData.getUserName());
        response.setHeader("Location", MessageFormat.format("/users/p/{0}", newUser.getUserName()));
        response.setStatus(HttpStatus.CREATED.value());
    }*/

    @PutMapping("/sec-api/user/p/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void receiveUpdatedUserInformation(@PathVariable String name, @RequestBody UserUpdateRequest userData, HttpServletRequest request, HttpServletResponse response){
        var updatedUser = userService.updateAppUser(userData, name);
        response.setHeader("Location", MessageFormat.format("/user/p/{0}", updatedUser.getUserName()));
        response.setStatus(HttpStatus.CREATED.value());
    }
}
