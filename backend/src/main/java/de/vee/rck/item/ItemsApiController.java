package de.vee.rck.item;

import de.vee.rck.item.dto.ItemDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public void changeItem(@RequestBody List<ItemDTO> itemDetails){
        itemRepo.saveAll(itemMapper.itemDTOToItem(itemDetails));
    }

    @PreAuthorize("hasAuthority('REMOVE_ITEM')")
    @DeleteMapping(path="/api/item/{id}")
    public void removeItem(@PathVariable Long id){
        itemRepo.findById(id).ifPresent((item) -> {
            itemRepo.delete(item);
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
