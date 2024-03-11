package de.vee.rck.user;

import de.vee.rck.user.dto.UserUpdateRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceUnitTest {

    static final String mockUserPassword = "12345";
    static final String mockUserPassword2 = "meepmeppkevinistgenial";
    static final String mockUserName = "mock";
    static final String mockUserName2 = "lol";
    static final String mockUserEmail = "lol@rofl.de";
    static final String mockUserEmail2 = "mockingbird@owo.com";

    private final AppUserRepository userRepo = mock(AppUserRepository.class);
    private final UserRoleRepository roleRepo = mock(UserRoleRepository.class);
    private final UserRoleRepository userRoleRepo = mock(UserRoleRepository.class);
    private final UserMapper userMapper = mock(UserMapper.class);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    private UserService dut;

    private AppUser mockUser;

    private final UserRole roleUser = new UserRole("ROLE_USER", 2);
    private final UserRole roleAdmin = new UserRole("ROLE_ADMIN", 1);

    @BeforeEach
    void setup(){
        dut = new UserService(userRepo, userRoleRepo, passwordEncoder, userMapper);
        mockUser = new AppUser();
        mockUser.setUserName(mockUserName);
        mockUser.setEnabled(false);
        mockUser.setPassword(passwordEncoder.encode(mockUserPassword));
        mockUser.setEmail(mockUserEmail);
        mockUser.setRoles(List.of(roleAdmin));

        when(roleRepo.findById(2L)).thenReturn(Optional.of(roleUser));
        when(roleRepo.findById(1L)).thenReturn(Optional.of(roleAdmin));
        when(roleRepo.findByName("ROLE_ADMIN")).thenReturn(Optional.of(roleAdmin));
        when(roleRepo.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));
    }


    @Test
    void testUpdateAppUserWithEmptyRequest(){
        when(userRepo.findByUserName(mockUserName)).thenReturn(Optional.of(mockUser));
        when(userRepo.save(mockUser)).thenReturn(mockUser);

        UserUpdateRequest request = new UserUpdateRequest();

        assertNotNull(dut.updateAppUser(request, mockUserName));
        verify(userRepo).findByUserName(mockUserName);
        verify(userRepo, atMostOnce()).save(mockUser);
    }

    @Test
    void testUpdateAppUserWithValidRequest(){
        AppUser updatedUser = new AppUser();
        updatedUser.setUserName(mockUserName2);

        when(userRepo.findByUserName(mockUserName)).thenReturn(Optional.of(mockUser));
        when(userRepo.save(mockUser)).thenReturn(updatedUser);

        UserUpdateRequest request = new UserUpdateRequest();
        request.setEnabled(true);
        request.setUserName(mockUserName2);
        request.setEmail(mockUserEmail2);
        request.setRoles(List.of(roleUser.getName()));
        request.setPassword(mockUserPassword2);


        assertEquals(dut.updateAppUser(request, mockUserName), updatedUser);
        verify(userRepo).findByUserName(mockUserName);
        verify(userRepo, atLeastOnce()).save(mockUser);
        verify(userMapper, atLeastOnce())
                .updateAppUserFromAppUserDetails(mockUser ,request, request.getPassword());
    }



    @Test
    void updatePassword(){
        when(userRepo.findByUserName(mockUserName)).thenReturn(Optional.of(mockUser));
        assertTrue(dut.updatePassword(mockUser.getUserName(), mockUserPassword, mockUserPassword2));
    }

    @Test
    void updatePassword_fail(){
        when(userRepo.findByUserName(mockUserName)).thenReturn(Optional.of(mockUser));
        assertFalse(dut.updatePassword(mockUser.getUserName(), mockUserPassword2, mockUserPassword2));
        assertThrows(NoSuchElementException.class ,
                () -> dut.updatePassword(mockUserName2, mockUserPassword2, mockUserPassword2));
    }
}
