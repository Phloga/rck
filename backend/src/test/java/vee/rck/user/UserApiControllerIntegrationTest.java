package vee.rck.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import vee.rck.RecipeApplication;
import vee.rck.user.dto.*;
import org.aspectj.lang.annotation.Before;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = RecipeApplication.class)
public class UserApiControllerIntegrationTest {

    static final String secUsersApi = "/sec-api/user/";

    static final String usersRoot = secUsersApi + "p/";
    static final String allUsersUri = secUsersApi + "all";

    static final String allRolesUri = secUsersApi + "roles/all";
    @Autowired
    MockMvc mvc;


    private static UserUpdateRequest userUpdateRequest;
    private static String userUpdateRequestJson;

    private static UserQueryResponse expectedUserQueryResponse;
    private static String expectedUserQueryResponseJson;

    private static String userUpdateUri;

    @BeforeAll
    static void setupTestData() throws JsonProcessingException {
        userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setUserName("snorelax");
        userUpdateRequest.setEmail("snorelax@lol.com");
        userUpdateRequest.setPassword("123"); //there are no password rules yet
        userUpdateRequest.setEnabled(true);
        userUpdateRequest.setRoles(List.of("ROLE_USER"));
        userUpdateRequestJson = toJson(userUpdateRequest);

        expectedUserQueryResponse = new UserQueryResponse();
        expectedUserQueryResponse.setUserName(userUpdateRequest.getUserName());
        expectedUserQueryResponse.setEmail(userUpdateRequest.getEmail());
        expectedUserQueryResponse.setRoles(userUpdateRequest.getRoles());
        expectedUserQueryResponse.setEnabled(userUpdateRequest.getEnabled());

        expectedUserQueryResponseJson = toJson(expectedUserQueryResponse);

        userUpdateUri = usersRoot + userUpdateRequest.getUserName();
    }

    @Test
    void fetchAllUsers() throws Exception {
        mvc.perform(get(allUsersUri).with(user("snorelax").roles("USER")))
                .andExpect(
                        status().is4xxClientError()
                );
        mvc.perform(get(allUsersUri).with(user("admin").roles("USER","ADMIN")))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
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


    @Test
    void authorizedCreateAndRetrieveUserThenSuccess() throws Exception {
        mvc.perform(put(userUpdateUri)
                        .with(user("admin").roles("ADMIN"))
                        .content(userUpdateRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is2xxSuccessful()
                );

        mvc.perform(get(userUpdateUri)
                        .with(user("admin").roles("ADMIN")))
                .andExpectAll(
                        status().is2xxSuccessful(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(expectedUserQueryResponseJson)
                );
    }

    @Test
    void unauthorizedCreateAndRetrieveUserThenFail() throws Exception {
        String userUri = usersRoot + userUpdateRequest.getUserName();

        mvc.perform(put(userUri)
                        .with(user("lol").roles("USER"))
                        .content(userUpdateRequestJson).contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().is4xxClientError()
                );

        mvc.perform(get(userUri)
                        .with(user("admin").roles("ADMIN")))
                .andExpectAll(
                        status().is4xxClientError()
                );
    }

    static <T> String toJson(T object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
