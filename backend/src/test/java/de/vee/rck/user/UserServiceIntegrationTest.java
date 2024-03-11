package de.vee.rck.user;

import de.vee.rck.RecipeApplication;
import de.vee.rck.user.dto.UserUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@DirtiesContext
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = RecipeApplication.class)
public class UserServiceIntegrationTest {

    static final String adminName = "Admin";

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

    @Test
    void attemptedAdminSelfOwn() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setRoles(List.of());
        assertThrows(UserUpdateError.class, () -> {userService.updateAppUser(request, adminName);});
    }

    @Test
    void checkAdmin() {
        // check for the default admin
        assertTrue(userService.adminCount() > 0);
    }
}
