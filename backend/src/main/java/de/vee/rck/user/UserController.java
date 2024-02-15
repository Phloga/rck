package de.vee.rck.user;


import de.vee.rck.user.dto.RegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private UserMapper userMapper;


    @GetMapping(path="/register")
    public String registerPage(Model model){
        RegistrationRequest registrationRequest= new RegistrationRequest();
        model.addAttribute("user", registrationRequest);
        return "register";
    }

    @PostMapping(path="/register")
    public ModelAndView registerRequest(@ModelAttribute("user") @Valid RegistrationRequest requestData,
                                        HttpServletRequest request, Errors errors) {
        AppUser user = userService.makeAppUser(requestData.getUserName(), requestData.getPassword(), Arrays.asList("ROLE_USER"));
        user.setEmail(requestData.getEmail());
        try {
            user = userService.registerNewUser(user);
        } catch (UserRegistrationRequestError e) {
            //TODO add error messages
            return new ModelAndView("register", "user", requestData);
        }
        //redirect to success page
        return new ModelAndView("registerSuccess", "user", userMapper.appUserToUserDetails(user));
    }
}
