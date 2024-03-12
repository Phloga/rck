package vee.rck.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import vee.rck.RecipeApplicationInitializationConfiguration;
import vee.rck.item.ItemMapper;
import vee.rck.item.ItemRepository;
import vee.rck.item.dto.ItemDTO;
import vee.rck.recipe.RecipeRepository;
import vee.rck.recipe.dto.PackedRecipe;
import vee.rck.recipe.RecipeMapper;
import vee.rck.security.*;
import vee.rck.units.Unit;
import vee.rck.units.UnitMapper;
import vee.rck.units.UnitRepository;
import vee.rck.units.dto.UnitDetails;
import vee.rck.security.dto.UserUpdateRequest;
import vee.rck.utils.Utils;
import lombok.Setter;
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

    @Autowired @Setter
    private RecipeApplicationInitializationConfiguration config;
    @Autowired @Setter
    private UserService userService;
    @Autowired @Setter
    private UserRoleRepository roleRepository;
    @Autowired @Setter
    private PrivilegeRepository privilegeRepository;
    @Autowired @Setter
    private ResourceLoader resLoader;

    @Autowired @Setter
    private RecipeMapper recipeMapper;
    @Autowired @Setter
    private RecipeRepository recipeRepo;
    @Autowired @Setter
    private UnitRepository unitRepo;
    @Autowired @Setter
    private UnitMapper unitMapper;
    @Autowired @Setter
    private ItemRepository itemRepo;
    @Autowired @Setter
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
        //createRoleIfNotFound("ROLE_ANONYMOUS", List.of());

        if (config.getUsersLocation() != null) {
            loadUsers(config.getUsersLocation());
        }

        if (userService.adminCount() == 0) {
            UserUpdateRequest admin = new UserUpdateRequest();
            admin.setRoles(List.of("ROLE_ADMIN"));
            admin.setUserName("Admin");
            admin.setPassword("owo");
            admin.setEmail("owo@weeb.de");
            admin.setEnabled(true);
            userService.createAppUser(admin);
            logger.info("Added default user {} to the database", admin.getUserName());
        }

        boolean initUnits = unitRepo.count() == 0;
        boolean initItems = itemRepo.count() == 0;
        boolean initRecipes = recipeRepo.count() == 0;

        for (var init : config.getData()){
            if (init.unitsLocation() != null && initUnits) {
                loadUnits(init.unitsLocation());
            }
            if (init.itemsLocation() != null && initItems){
                loadIngredients(init.itemsLocation());
            }
            if (init.recipesLocation() != null && initRecipes){
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
    UserRole createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Optional<UserRole> role = roleRepository.findByName(name);
        if (role.isEmpty()) {
            UserRole newRole = new UserRole(name);
            newRole.setPrivileges(privileges);
            return roleRepository.save(newRole);
        }
        return role.get();
    }

    /***
     * Creates users from data provided by the json file pointed to by location
     * If an user name is already in use, the user with the same name in the json file will be ignored
     * @param usersLocation (class) path to json file
     */
    @Transactional
    public void loadUsers(String usersLocation){
        try {
            String usersJsonText = Utils.readResourceAsString(resLoader, usersLocation);
            List<UserUpdateRequest> initialUsers = jsonMapper.readValue(usersJsonText,
                    jsonMapper.getTypeFactory().constructCollectionType(List.class, UserUpdateRequest.class));

            for (var user : initialUsers){
                if (userService.findUserByName(user.getUserName()).isEmpty())
                    userService.updateAppUser(user, user.getUserName());
            }
            logger.info("Imported users from {}", usersLocation);
        } catch (IOException ex){
            logger.error(MessageFormat.format("Failed to load users from {0}!", usersLocation));
            logger.error(ex.getMessage());
        }
    }

    @Transactional
    public void loadUnits(String unitsLocation) {
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
    public void loadIngredients(String ingredientsLocation) {
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
    public void loadRecipes(String recipesLocation, String importer) {
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
