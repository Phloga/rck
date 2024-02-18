package de.vee.rck.item;

import de.vee.rck.item.dto.ItemDetails;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
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
    @GetMapping(path="/api/items/allItems", produces="application/json")
    List<ItemDetails> allIngredients() {
        var result = StreamSupport.stream(itemRepo.findAll().spliterator(), false).map((item) -> {
                    return itemMapper.itemToItemDetails(item);
        }).collect(Collectors.toList());
        return result;
    }

    @GetMapping(path="/api/items/commonIngredients", produces="application/json")
    List<ItemDetails> commonIngredients() {
        Collection<Item> ingredients = itemRepo.findByIsBaseIngredient(true);
        return itemMapper.itemToItemDetails(ingredients);
    }


    @PreAuthorize("hasAuthority('MODIFY_ITEM')")
    @PostMapping(path="/api/items/modify")
    public void changeItem(@RequestBody List<ItemDetails> itemDetails){
        itemRepo.saveAll(itemMapper.itemDetailsToItem(itemDetails));
    }

    @PreAuthorize("hasAuthority('REMOVE_ITEM')")
    @DeleteMapping(path="/api/item/{id}")
    public void removeItem(@PathVariable Long id){
        itemRepo.findById(id).ifPresent((item) -> {
            itemRepo.delete(item);
        });
    }

    @GetMapping(path="/api/item/{id}", produces="application/json")
    ItemDetails sendItemDetails(@PathVariable Long id) {
        var item = itemRepo.findById(id);
        if (item.isPresent()){
            return itemMapper.itemToItemDetails(item.get());
        }
        return new ItemDetails();
    }

}
