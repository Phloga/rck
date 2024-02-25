package de.vee.rck.init;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.vee.rck.RecipeApplicationConfiguration;
import de.vee.rck.item.ItemMapper;
import de.vee.rck.item.ItemRepository;
import de.vee.rck.recipe.RecipeRepository;
import de.vee.rck.item.dto.ItemDetails;
import de.vee.rck.recipe.dto.PackedRecipe;
import de.vee.rck.recipe.RecipeMapper;
import de.vee.rck.units.Unit;
import de.vee.rck.units.UnitMapper;
import de.vee.rck.units.UnitRepository;
import de.vee.rck.units.dto.UnitDetails;
import de.vee.rck.user.AppUser;
import de.vee.rck.user.Privilege;
import de.vee.rck.user.UserRole;
import de.vee.rck.user.AppUserRepository;
import de.vee.rck.user.PrivilegeRepository;
import de.vee.rck.user.UserRoleRepository;
import de.vee.rck.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

@Component
@RequiredArgsConstructor
public class InitialSetup implements ApplicationListener<ContextRefreshedEvent> {
    private static final String defaultIngredientsLocation = "classpath:data/default-ingredients.json";
    private static final String defaultRecipesLocation = "classpath:data/default-recipes.json";
    private static final String defaultUnitsLocation = "classpath:data/default-units.json";

    private final Logger logger;
    @Autowired
    private RecipeApplicationConfiguration config;
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private UserRoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ItemRepository itemRepo;
    @Autowired
    private ResourceLoader resLoader;

    @Autowired
    private RecipeMapper recipeMapper;
    @Autowired
    private RecipeRepository recipeRepo;
    @Autowired
    private UnitRepository unitRepo;
    @Autowired
    private UnitMapper unitMapper;
    @Autowired
    private ItemMapper itemMapper;
    private boolean setupComplete;

    public InitialSetup(){
        this.setupComplete = false;
        this.logger = LoggerFactory.getLogger(InitialSetup.class);
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (setupComplete)
            return;

        Privilege listAll
                = createPrivilegeIfNotFound("LIST_ALL");
        Privilege modifyItemPrivilege
                = createPrivilegeIfNotFound("MODIFY_ITEM");
        Privilege removeItemPrivilege
                = createPrivilegeIfNotFound("REMOVE_ITEM");
        Privilege addRecipePrivilege
                = createPrivilegeIfNotFound("ADD_RECIPE");
        Privilege modifyRecipePrivilege
                = createPrivilegeIfNotFound("MODIFY_RECIPE");

        List<Privilege> adminPrivileges = Arrays.asList(listAll,
                modifyItemPrivilege, removeItemPrivilege, addRecipePrivilege, modifyRecipePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(addRecipePrivilege));
        //createRoleIfNotFound("ROLE_ANONYMOUS", Arrays.asList(readPrivilege));

        if (userRepository.count() == 0) {
            UserRole adminRole = roleRepository.findByName("ROLE_ADMIN");
            AppUser user = new AppUser();
            user.setUserName("Admin");
            user.setPassword(passwordEncoder.encode("owo"));
            user.setEmail("owo@weeb.de");
            user.setRoles(Arrays.asList(adminRole));
            user.setEnabled(true);
            userRepository.save(user);
        }

        if (config.getLoadSampleData() && itemRepo.count() == 0){
            addSampleData();
        }
        setupComplete = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    UserRole createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        UserRole role = roleRepository.findByName(name);
        if (role == null) {
            role = new UserRole(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    protected void addSampleData() {
        try {
            String ingredientsJsonText = Utils.readResourceAsString(resLoader, defaultIngredientsLocation);
            String recipesJsonText = Utils.readResourceAsString(resLoader, defaultRecipesLocation);
            String unitsJsonText = Utils.readResourceAsString(resLoader, defaultUnitsLocation);

            ObjectMapper mapper = new ObjectMapper();

            List<UnitDetails> initialUnitDetails = mapper.readValue(unitsJsonText,
                    mapper.getTypeFactory().constructCollectionType(List.class, UnitDetails.class));
            List<Unit> initialUnits = unitMapper.unitDetailsToUnit(initialUnitDetails);

            unitRepo.saveAll(initialUnits);

            List<ItemDetails> initialIngredients = mapper.readValue(ingredientsJsonText,
                    mapper.getTypeFactory().constructCollectionType(List.class, ItemDetails.class));

            //var items = RecipeMapper.toItemList(initialIngredients);
            var items = itemMapper.itemDetailsToItem(initialIngredients);
            itemRepo.saveAll(items);

            List<PackedRecipe> initialRecipes = mapper.readValue(recipesJsonText,
                    mapper.getTypeFactory().constructCollectionType(List.class, PackedRecipe.class));


            for (PackedRecipe details : initialRecipes){
                var recipe = recipeMapper.toRecipe(details, null);
                recipeRepo.save(recipe);
            }
            /*
            RecipeDTO testRecipe = new RecipeDTO("Butter Plätzchen");
            Collection<ItemListingDTO> inputLines = Arrays.asList(
                    new ItemListingDTO(null, "Mehl", false,  Unit.GRAM, 500.f),
                    new ItemListingDTO(null, "Zucker", false,  Unit.GRAM, 250.f),
                    new ItemListingDTO(null, "Butter", false,  Unit.GRAM, 250.f));
            Collection<ItemListingDTO> outputLines = Arrays.asList(
                    new ItemListingDTO(null, "Butter Plätzchen", false,  Unit.NONE, 0.f));

            testRecipe.setIngredients(inputLines);
            testRecipe.setProducts(outputLines);
             */
            //String json = mapper.writeValueAsString(Arrays.asList(testRecipe));
            //HashMap<String, Item> name2itemTranslationMap = new HashMap<String,Item>();
            //itemRepo.findAll().forEach((item) -> {name2itemTranslationMap.put(item.getName(),item);});

            //logger.info(json);

        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize Item to json format!");
            logger.error(e.getMessage());
        } catch (FileNotFoundException e){
            logger.error(MessageFormat.format("Failed to find {0}!", defaultRecipesLocation));
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(MessageFormat.format("Failed read from {0}!", defaultRecipesLocation));
            logger.error(e.getMessage());
        }
    }
}
