package de.vee.rck.user;

import de.vee.rck.user.dto.UserQueryResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserManagementController {

    static final String usersSite = "/users/index";

    @GetMapping("/users/index")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ModelAndView sendUsersPage(){
        return null;
    }


    @GetMapping("/api/users/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    UserQueryResponse sendAllUsers(){
        return null;
    }
}
