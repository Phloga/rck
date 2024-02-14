package de.vee.rck.item;

import de.vee.rck.item.dto.ItemDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path="/item")
@AllArgsConstructor
public class ItemController {

    private ItemRepository itemRepo;
    private ItemMapper itemMapper;
    private final int initialQueryResultBufferSize = 256;
    //display, modify
    @GetMapping(path="/details/{id}")
    public ModelAndView getItem(@PathVariable Long id, Model model, HttpServletRequest request){

        Item item = itemRepo.findById(id).orElseThrow();
        model.addAttribute("id", item.getId());
        model.addAttribute("name", item.getName());
        model.addAttribute("baseIngredient", item.getIsBaseIngredient());
        model.addAttribute("editEnabled", request.isUserInRole("ROLE_ADMIN"));
        return new ModelAndView("item/edit", "item",model);
    }
    //create new
    @GetMapping(path="/create")
    @PreAuthorize("hasAuthority('MODIFY_ITEM')")
    public void createItem(Model model){
        //TODO edit page without data
    }


    @GetMapping(path="/index")
    public ModelAndView getItems(Model model){
        /*
        ArrayList<ItemDetails> allItems = new ArrayList<>(initialQueryResultBufferSize);
        itemRepo.findAll().forEach((item) -> {
            allItems.add(itemMapper.itemToItemDetails(item));
        });
        model.addAttribute("items", allItems);*/
        return new ModelAndView("item/index", "model", model);
    }

    @PreAuthorize("hasAuthority('REMOVE_ITEM')")
    @PostMapping(path="/remove")
    public void removeItem(Long id){
        itemRepo.findById(id).ifPresent((item) -> {
            itemRepo.delete(item);
        });
    }

    @PreAuthorize("hasAuthority('MODIFY_ITEM')")
    @PostMapping(path="/modifyAll")
    @ResponseBody
    public void changeItem(@RequestBody List<ItemDetails> itemDetails){
        itemRepo.saveAll(itemMapper.itemDetailsToItem(itemDetails));
    }

    @PreAuthorize("hasAuthority('MODIFY_ITEM')")
    @PostMapping(path="/modify")
    public void changeItem(@RequestBody ItemDetails itemDetails){
        itemRepo.save(itemMapper.itemDetailsToItem(itemDetails));
    }

}
