package de.vee.rck.user;

import de.vee.rck.RecipeApplication;
import de.vee.rck.user.dto.UserUpdateRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = RecipeApplication.class)
public class UserApiControllerIntegrationTest {

    static final String secUsersApi = "/sec-api/users/";
    static final String allUsersUri = secUsersApi + "all";

    static final String allRolesUri = secUsersApi + "roles/all";
    @Autowired
    MockMvc mvc;

    @Test
    void fetchAllUsers() throws Exception {
        mvc.perform(get(allUsersUri).with(user("admin").roles("USER","ADMIN")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
        mvc.perform(get(allUsersUri).with(user("snorelax").roles("USER")))
                .andExpect(
                        status().is4xxClientError()
                );
    }

    @Test
    void fetchRoles() throws Exception {
        mvc.perform(get(allRolesUri).with(user("admin").roles("USER","ADMIN")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }
}
