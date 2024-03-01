package de.vee.rck.user;

import de.vee.rck.user.dto.AppUserDetails;
import de.vee.rck.user.dto.UserCard;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;

@Controller
@AllArgsConstructor
public class UserInformationController {

    private AppUserRepository userRepo;
    private UserService userService;

    @ResponseBody
    @GetMapping("/api/users/self")
    public UserCard sendUserInformation(Authentication authentication, HttpServletResponse response){
        if (authentication == null || !authentication.isAuthenticated()) {
            return new AppUserDetails("anonymous", "", new ArrayList<>());
        }
        var userCard = userService.loadUserCard(authentication.getName());
        if (userCard.isPresent()){
            response.setHeader("Cache-Control", "private");
            return userCard.get();
        }
        response.setStatus(500);
        return null;
    }
}
