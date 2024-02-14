package de.vee.rck.user;

import de.vee.rck.user.dto.AvailabilityCheckRequest;
import de.vee.rck.user.dto.AvailabilityCheckResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RestController
@AllArgsConstructor
public class UserRestController {
    private AppUserRepository userRepo;

    @PostMapping(path="/checkUserName", produces="application/json")
    public AvailabilityCheckResponse checkUserNameAvailability(AvailabilityCheckRequest userNameReq){
        if (userRepo.existsByUserName(userNameReq.getValue())) {
            return new AvailabilityCheckResponse("taken");
        }
        //TODO maybe add a call to a profanity filter here
        return new AvailabilityCheckResponse("ok");
    }
}
