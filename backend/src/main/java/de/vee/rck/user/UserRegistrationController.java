package de.vee.rck.user;


import de.vee.rck.user.dto.RegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
@AllArgsConstructor
public class UserRegistrationController {

    private UserService userService;
    private UserMapper userMapper;


    private SmartValidator validator;

    @GetMapping(path="/register")
    public String registerPage(Model model){
        RegistrationRequest registrationRequest= new RegistrationRequest();
        model.addAttribute("user", registrationRequest);
        return "register";
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public String validationError(){
        return "";
    }

    @ExceptionHandler({UserRegistrationRequestError.class})
    public String registrationError(){
        return "";
    }


    @PostMapping(path="/register")
    public ModelAndView registerUser(@Valid @ModelAttribute("user") RegistrationRequest requestData, Errors errors,
                                        HttpServletRequest request) {
        if (errors.hasErrors()){
            ModelMap map = new ModelMap();
            map.addAttribute("user", requestData);
            map.addAttribute("errors", errors);
            return new ModelAndView("register", map);
        }
        AppUser user = userService.makeAppUser(requestData.getUserName(), requestData.getPassword(), Arrays.asList("ROLE_USER"));
        user.setEmail(requestData.getEmail());
        try {
            user = userService.registerNewUser(user);
        } catch (UserRegistrationRequestError e){
            ModelMap map = new ModelMap();
            if (e.isNameTaken()){
                errors.rejectValue("userName","message.userNameUnavailable");
            }
            if (e.isEmailTaken()){
                errors.rejectValue("email","message.emailUnavailable");
            }
            map.addAttribute("user", requestData);
            map.addAttribute("errors",errors);
            return new ModelAndView("register", map);
        }
        //redirect to success page
        return new ModelAndView("registerSuccess", "user", userMapper.appUserToUserDetails(user));
    }
}
