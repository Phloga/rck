package de.vee.rck.item;

import de.vee.rck.item.dto.ItemDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@AllArgsConstructor
public class ItemRestController {

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
}
