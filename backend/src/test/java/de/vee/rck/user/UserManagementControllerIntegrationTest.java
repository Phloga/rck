package de.vee.rck.user;

import de.vee.rck.RecipeApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = RecipeApplication.class)
public class UserManagementControllerIntegrationTest {

    static final String usersSite = "/users/index";

    @Autowired
    MockMvc mvc;

    @Test
    void getUsers_asAdmin_thenStatus200() throws Exception {
        mvc
                .perform(get(usersSite)
                        .with(user("admin").roles("USER","ADMIN")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8))

                );
    }

    @Test
    void getUsers_asUser_thenFail() throws Exception {
        mvc
                .perform(get(usersSite)
                        .with(user("snorlax").roles("USER")))
                .andExpect(
                        status().is4xxClientError());
    }
}
