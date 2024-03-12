package vee.rck.item;

import vee.rck.item.dto.ItemDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Controlls the "/api/item/**" and "/api/items/**" end points*/
@RestController
@AllArgsConstructor
public class ItemsApiController {

    ItemMapper itemMapper;
    ItemRepository itemRepo;

    private static final Logger logger = LoggerFactory.getLogger(ItemsApiController.class);

    @PreAuthorize("hasAuthority('LIST_ALL')")
    @GetMapping(path="/api/items/all", produces="application/json")
    List<ItemDTO> allIngredients() {
        var result = StreamSupport.stream(itemRepo.findAll().spliterator(), false).map((item) -> {
                    return itemMapper.itemToItemDTO(item);
        }).collect(Collectors.toList());
        return result;
    }

    @GetMapping(path="/api/items/common-ingredients", produces="application/json")
    List<ItemDTO> commonIngredients() {
        Collection<Item> ingredients = itemRepo.findByIsBaseIngredient(true);
        return itemMapper.itemToItemDTO(ingredients);
    }


    @PreAuthorize("hasAuthority('MODIFY_ITEM')")
    @PostMapping(path="/api/items/modified")
    public void changeItem(@RequestBody List<ItemDTO> itemDetails, Authentication authentication){
        var iterator = itemRepo.saveAll(itemMapper.itemDTOToItem(itemDetails));
        var affectedItemIds = StreamSupport.stream(iterator.spliterator(), false)
                .map(item -> item.getId().toString())
                .collect(Collectors.joining());
        logger.info("{} changed/added item definitions for item {}", authentication.getName(), affectedItemIds);
    }

    @PreAuthorize("hasAuthority('REMOVE_ITEM')")
    @DeleteMapping(path="/api/item/{id}")
    public void removeItem(@PathVariable Long id, Authentication authentication){
        itemRepo.findById(id).ifPresent((item) -> {
            itemRepo.delete(item);
            logger.info("{} removed item {}", authentication.getName(), id);
        });
    }

    @GetMapping(path="/api/item/{id}", produces="application/json")
    ItemDTO sendItemDetails(@PathVariable Long id) {
        var item = itemRepo.findById(id);
        if (item.isPresent()){
            return itemMapper.itemToItemDTO(item.get());
        }
        return new ItemDTO();
    }

}
