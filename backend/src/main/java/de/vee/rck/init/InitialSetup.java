package de.vee.rck.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.vee.rck.RecipeApplicationInitializationConfiguration;
import de.vee.rck.item.ItemMapper;
import de.vee.rck.item.ItemRepository;
import de.vee.rck.item.dto.ItemDTO;
import de.vee.rck.recipe.RecipeRepository;
import de.vee.rck.recipe.dto.PackedRecipe;
import de.vee.rck.recipe.RecipeMapper;
import de.vee.rck.units.Unit;
import de.vee.rck.units.UnitMapper;
import de.vee.rck.units.UnitRepository;
import de.vee.rck.units.dto.UnitDetails;
import de.vee.rck.user.*;
import de.vee.rck.user.dto.UserUpdateRequest;
import de.vee.rck.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

@Component
public class InitialSetup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(InitialSetup.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeApplicationInitializationConfiguration config;
    @Autowired
    private UserRoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;
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

    ObjectMapper jsonMapper;

    public InitialSetup(){
        this.setupComplete = false;
        this.jsonMapper = new ObjectMapper();
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

        createRoleIfNotFound("ROLE_ADMIN", Arrays.asList(
                listAll, modifyItemPrivilege, removeItemPrivilege, addRecipePrivilege, modifyRecipePrivilege));
        createRoleIfNotFound("ROLE_USER", Arrays.asList(
                addRecipePrivilege, modifyRecipePrivilege, listAll));
        createRoleIfNotFound("ROLE_ANONYMOUS", List.of());

        if (!userService.anyAdminExists()) {
            UserUpdateRequest admin = new UserUpdateRequest();
            admin.setRoles(List.of("ROLE_ADMIN"));
            admin.setUserName("Admin");
            admin.setPassword("owo");
            admin.setEmail("owo@weeb.de");
            admin.setEnabled(true);
            userService.createAppUser(admin);
            logger.info("Added default user {} to the database", admin.getUserName());
        }


        for (var init : config.getData()){
            if (init.unitsLocation() != null && unitRepo.count() == 0) {
                loadUnits(init.unitsLocation());
            }
            if (init.itemsLocation() != null && itemRepo.count() == 0){
                loadIngredients(init.itemsLocation());
            }
            if (init.recipesLocation() != null && recipeRepo.count() == 0){
                loadRecipes(init.recipesLocation(), init.importAs());
            }
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
    protected void loadUnits(String unitsLocation) {
        try {
            String unitsJsonText = Utils.readResourceAsString(resLoader, unitsLocation);
            List<UnitDetails> initialUnitDetails = jsonMapper.readValue(unitsJsonText,
                    jsonMapper.getTypeFactory().constructCollectionType(List.class, UnitDetails.class));
            List<Unit> initialUnits = unitMapper.unitDetailsToUnit(initialUnitDetails);
            unitRepo.saveAll(initialUnits);
            logger.info("Imported unit definitions from {}", unitsLocation);
        } catch (IOException ex){
            logger.error(MessageFormat.format("Failed to load units from {0}!", unitsLocation));
            logger.error(ex.getMessage());
        }
    }

    @Transactional
    protected void loadIngredients(String ingredientsLocation) {
        try {
            String ingredientsJsonText = Utils.readResourceAsString(resLoader, ingredientsLocation);
            List<ItemDTO> initialIngredients = jsonMapper.readValue(ingredientsJsonText,
                    jsonMapper.getTypeFactory().constructCollectionType(List.class, ItemDTO.class));
            var items = itemMapper.itemDTOToItem(initialIngredients);
            itemRepo.saveAll(items);
            logger.info("Imported item definitions from {}", ingredientsLocation);
        } catch (IOException ex){
            logger.error(MessageFormat.format("Failed to load ingredients from {0}!", ingredientsLocation));
            logger.error(ex.getMessage());
        }
    }

    @Transactional
    protected void loadRecipes(String recipesLocation, String importer) {
        try {
            String recipesJsonText = Utils.readResourceAsString(resLoader, recipesLocation);
            List<PackedRecipe> initialRecipes = jsonMapper.readValue(recipesJsonText,
                    jsonMapper.getTypeFactory().constructCollectionType(List.class, PackedRecipe.class));

            var owner = importer != null ? userService.findUserByName(importer).orElseThrow() : null;

            for (PackedRecipe details : initialRecipes){
                var recipe = recipeMapper.toRecipe(details, null);
                recipe.setOwner(owner);
                recipeRepo.save(recipe);
            }
            if (!initialRecipes.isEmpty())
                if (owner == null)
                    logger.info("Imported recipe definitions from {}", recipesLocation);
                else
                    logger.info("Imported recipe definitions from {} as {}", recipesLocation, importer);

        } catch (IOException ex){
            logger.error(MessageFormat.format("Failed to load recipes from {0}!", recipesLocation));
            logger.error(ex.getMessage());
        }
    }
}
