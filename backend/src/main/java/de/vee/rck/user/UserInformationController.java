package de.vee.rck.user;

import de.vee.rck.user.dto.UserCard;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
public class UserInformationController {

    private AppUserRepository userRepo;
    private UserMapper userMapper;

    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    @GetMapping("/api/user/currentUser")
    public UserCard sendUserInformation(Authentication authentication, HttpServletResponse response){
        var user = userRepo.findByUserName( authentication.getName());
        if (user.isPresent()){
            response.setHeader("Cache-Control", "private");
            return userMapper.appUserToUserDetails(user.get());
        }
        response.setStatus(403);
        return null;
    }
}
