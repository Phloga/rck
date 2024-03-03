package de.vee.rck.user;

import de.vee.rck.user.dto.AppUserPreview;
import de.vee.rck.user.dto.UserQueryResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
@AllArgsConstructor
public class UserManagementController {

    private UserService userService;

    @GetMapping("/users/index")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String sendUsersIndex()
    {
        return "user/users-index";
    }


    @GetMapping("/users/p/{name}")
    ModelAndView sendUserPage(@PathVariable String name, HttpServletRequest request, HttpServletResponse response) {
        if (request.getUserPrincipal().getName().equals(name) || request.isUserInRole("ADMIN")){
            var user = userService.loadAppUserDetailsByName(name);
            if (user.isEmpty()){
                //TODO may throw an exception here due to the inconsistent state
                // of having an authenticated user without their name being in the data base
                response.setStatus(500);
                return null;
            }
            response.setHeader("Cache-Control", "private");
            return new ModelAndView("/user/user-editor", "user", user.get());
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return null;
    }
}
