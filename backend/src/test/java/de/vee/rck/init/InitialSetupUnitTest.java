package de.vee.rck.init;


import de.vee.rck.DataInitialization;
import de.vee.rck.RecipeApplicationInitializationConfiguration;
import de.vee.rck.item.ItemMapper;
import de.vee.rck.item.ItemRepository;
import de.vee.rck.recipe.RecipeMapper;
import de.vee.rck.recipe.RecipeRepository;
import de.vee.rck.units.UnitMapper;
import de.vee.rck.units.UnitRepository;
import de.vee.rck.user.AppUserRepository;
import de.vee.rck.user.PrivilegeRepository;
import de.vee.rck.user.UserRoleRepository;
import de.vee.rck.user.UserService;
import de.vee.rck.user.dto.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ResourceLoader;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class InitialSetupUnitTest {

    static final String usersPath = "classpath:data/test-users.json";
    static final String unitsPath = "classpath:data/test-units.json";
    static final String itemsPath = "classpath:data/test-items.json";
    static final String recipesPath = "classpath:data/test-recipes.json";
    static final String importAs = "Admin";

    @Mock
    ItemRepository itemRepo;
    @Mock
    UserRoleRepository roleRepo;
    @Mock
    RecipeRepository recipeRepo;
    @Mock
    UserService userService;
    @Mock
    RecipeMapper recipeMapper;
    @Mock
    ItemMapper itemMapper;
    @Mock
    PrivilegeRepository privilegeRepository;
    @Mock
    ResourceLoader resourceLoader;
    @Mock
    UnitRepository unitRepo;
    @Mock
    UnitMapper unitMapper;
    RecipeApplicationInitializationConfiguration initConfig;

    InitialSetup dut;

    @BeforeEach
    void setup(){
        dut = new InitialSetup();
        initConfig = new RecipeApplicationInitializationConfiguration();
        initConfig.setUsersLocation(usersPath);
        DataInitialization dataInit =
                new DataInitialization(recipesPath,itemsPath,unitsPath,"admin");
        initConfig.setData(List.of(dataInit));
        dut.setConfig(initConfig);
        dut.setItemRepo(itemRepo);
        dut.setItemMapper(itemMapper);
        dut.setPrivilegeRepository(privilegeRepository);
        dut.setRoleRepository(roleRepo);
        dut.setResLoader(resourceLoader);
        dut.setUserService(userService);
        dut.setRecipeRepo(recipeRepo);
        dut.setRecipeMapper(recipeMapper);
        dut.setUnitRepo(unitRepo);
        dut.setUnitMapper(unitMapper);
    }


    @Test
    void initEvent()  {
        String testUsersJson = "[" +
                "  {" +
                "    \"userName\" : " +
                "\"snorlax\"," +
                "    \"email\" : \"lo" +
                "l\"," +
                "    \"roles\" :  [\"" +
                "ROLE_USER\"]," +
                "    \"enabled\" : tr" +
                "ue," +
                "    \"password\": \"" +
                "12345\"" +
                "  }" +
                "]";

        //when(userService.adminCount()).thenReturn(0);
        when(resourceLoader.getResource(usersPath)).thenReturn(new ByteArrayResource(testUsersJson.getBytes()));

        dut.loadUsers(usersPath);
        InOrder order = inOrder(userService);
        order.verify(userService).findUserByName("snorlax");
        order.verify(userService).updateAppUser(any(UserUpdateRequest.class), eq("snorlax"));
    }
}
