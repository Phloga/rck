package de.vee.rck.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.vee.rck.user.dto.UserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.MessageFormat;

@Controller
@AllArgsConstructor
public class UserManagementController {

    private UserService userService;

    @GetMapping("/users/index")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    String sendUsersIndex()
    {
        return "user/index";
    }


    @GetMapping("/users/p/{name}")
    @PreAuthorize("isAuthenticated()")
    ModelAndView sendUserPage(@PathVariable String name, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        if (request.getUserPrincipal().getName().equals(name) || request.isUserInRole("ADMIN")){
            var user = userService.loadAppUserDetailsByName(name);
            if (user.isEmpty()){
                //TODO may throw an exception here due to the inconsistent state
                // of having an authenticated user without their name being in the data base
                response.setStatus(500);
                return null;
            }
            //response.setHeader("Cache-Control", "private");
            ObjectMapper mapper = new ObjectMapper();
            String jsonText = mapper.writeValueAsString(user.get());
            return new ModelAndView("user/editor", "user", jsonText);
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return null;
    }

    @GetMapping("/users/new")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ModelAndView sendNewUserPage(){
        return new ModelAndView("user/editor", "user", "");
    }
}
