package de.vee.rck.user;

import de.vee.rck.user.dto.AppUserPreviewRecord;
import de.vee.rck.user.dto.UserQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class UserManagementController {

    private UserService userService;

    static final String usersSite = "/users/index";

    @GetMapping("/users/index")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ModelAndView sendUsersPage(){
        return null;
    }

    @GetMapping(path="/api/users", produces="application/json")
    @ResponseBody
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    Collection<AppUserPreviewRecord> sendAllUsers(){
        return userService.collectAllUsers();
    }
}
