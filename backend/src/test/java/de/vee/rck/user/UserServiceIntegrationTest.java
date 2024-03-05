package de.vee.rck.user;

import de.vee.rck.RecipeApplication;
import de.vee.rck.user.dto.UserUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = RecipeApplication.class)
public class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

    @Test
    void createUser() {
        var testUser = new UserUpdateRequest();
        testUser.setUserName("tuff");
        testUser.setPassword("1234lol");
        testUser.setRoles(Arrays.asList("ROLE_USER"));
        testUser.setEmail("test@test.com");
        testUser.setEnabled(true);
        userService.createAppUser(testUser);

        var loadedUser = userService.loadAppUserDetailsByName("tuff");
        assertTrue(loadedUser.isPresent());
        assertEquals("tuff", loadedUser.get().getUserName());
        assertTrue(loadedUser.get().getRoles().contains("ROLE_USER"));
    }

}
