package de.vee.rck.user;

import de.vee.rck.user.dto.UserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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


    @PutMapping("/users/p/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void receiveNewUserInformation(@RequestBody UserUpdateRequest userData, HttpServletRequest request, HttpServletResponse response){
        var newUser = userService.updateAppUser(userData, userData.getUserName());
        response.setHeader("Location", MessageFormat.format("/users/p/{0}", newUser.getUserName()));
        response.setStatus(HttpStatus.CREATED.value());
    }

    @PostMapping("/users/p/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void receiveUserInformation(@PathVariable String name, @RequestBody UserUpdateRequest userData, HttpServletRequest request, HttpServletResponse response){
        var updatedUser = userService.updateAppUser(userData, name);
        response.setHeader("Location", MessageFormat.format("/users/p/{0}", updatedUser.getUserName()));
        response.setStatus(HttpStatus.CREATED.value());
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
            return new ModelAndView("/user/editor", "user", user.get());
        }
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return null;
    }
}
