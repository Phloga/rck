package vee.rck.init;


import vee.rck.DataInitialization;
import vee.rck.RecipeApplicationInitializationConfiguration;
import vee.rck.item.Item;
import vee.rck.item.ItemMapper;
import vee.rck.item.ItemRepository;
import vee.rck.recipe.Recipe;
import vee.rck.recipe.RecipeLine;
import vee.rck.recipe.RecipeMapper;
import vee.rck.recipe.RecipeRepository;
import vee.rck.recipe.dto.PackedRecipe;
import vee.rck.recipe.dto.RecipeDetails;
import vee.rck.units.Unit;
import vee.rck.units.UnitMapper;
import vee.rck.units.UnitRepository;
import vee.rck.units.UnitType;
import vee.rck.user.*;
import vee.rck.user.dto.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ResourceLoader;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class InitialSetupUnitTest {

    static final String usersPath = "classpath:data/test-users.json";
    static final String unitsPath = "classpath:data/test-units.json";
    static final String itemsPath = "classpath:data/test-items.json";
    static final String recipesPath = "classpath:data/test-recipes.json";

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
    void testLoadRecipes() {
        final String importUser = "admin";
        final String testRecipesJson = "[{" +
                "\"name\": \"Mehl Banan\"," +
                "\"content\": \"\"," +
                "\"ingredients\": [{" +
                "\"itemId\": null," +
                "\"itemName\": \"Banane\"," +
                "\"isOptional\": false," +
                "\"unit\": \"pieces\"," +
                "\"amount\": 1 }," +
                "{\"itemId\": null,\"itemName\": \"Mehl\",\"isOptional\": false," +
                "\"unit\": \"gram\",\"amount\": 250}]," +
                "\"products\" : [{\"itemId\": null,\"itemName\": \"Mehl Banan\",\"isOptional\": false,\"unit\": \"pieces\",\"amount\": 60}]" +
                "}]";


        Item banane = new Item(1L, "´Banane", true);
        Item mehl = new Item(2L, "´Mehl", true);

        Unit gram = new Unit(1L, "gram", "g", UnitType.MASS, 1.f);
        Unit tbs = new Unit(1L, "tablespoon_uk", "g", UnitType.VOLUME, 1.f);
        Unit pieces = new Unit(1L, "pieces", "", UnitType.COUNT, 1.f);

        AppUser admin = new AppUser();
        admin.setUserName("admin");

        Recipe mehlBanane = new Recipe(null, "Mehl Banane", "",
                List.of(new RecipeLine()),2, null);

        when(resourceLoader.getResource(any(String.class)))
                .thenReturn(new ByteArrayResource(testRecipesJson.getBytes()));
        //when(itemRepo.findByName(banane.getName())).thenReturn(banane);
        //when(itemRepo.findByName(mehl.getName())).thenReturn(mehl);
        when(recipeMapper.toRecipe(any(RecipeDetails.class), isNull())).thenReturn(mehlBanane);
        when(userService.findUserByName(admin.getUserName())).thenReturn(Optional.of(admin));
        //when(unitRepo.findAllById(any(Iterable.class))).thenReturn(List.of(tbs,gram,pieces));

        dut.loadRecipes(recipesPath, importUser);
        verify(recipeRepo).save(argThat(arg -> {
            return arg.getName().equals(mehlBanane.getName()) && arg.getOwner() == admin;
        }));

    }

    @Test
    void testLoadUsers()  {
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
