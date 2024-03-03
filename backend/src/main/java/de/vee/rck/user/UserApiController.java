package de.vee.rck.user;

import de.vee.rck.user.dto.UserQueryResponse;
import de.vee.rck.user.dto.AppUserPreview;
import de.vee.rck.user.dto.UserCard;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@Controller
@AllArgsConstructor
@RestController
public class UserApiController {

    private UserService userService;

    @GetMapping("/api/users/self")
    public UserCard sendUserInformation(Authentication authentication, HttpServletResponse response){
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

    @GetMapping(path="/api/users/p/{name}", produces="application/json")
    @PreAuthorize("isAuthenticated()")
    UserQueryResponse sendSpecificUserInformation(@PathVariable String name, HttpServletRequest request, HttpServletResponse response) {
        if (request.getUserPrincipal().getName().equals(name) || request.isUserInRole("ADMIN")){
            var user = userService.loadAppUserDetailsByName(name);
            if (user.isEmpty()){
                //TODO may throw an exception here due to the inconsistent state
                // of having an authenticated user without their name being in the data base
                response.setStatus(500);
                return null;
            }
            response.setHeader("Cache-Control", "private");
            return user.get();
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return null;
    }

    @GetMapping(path="/api/users", produces="application/json")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    Collection<AppUserPreview> sendAllUsers(){
        return userService.collectAllUsers();
    }
}
