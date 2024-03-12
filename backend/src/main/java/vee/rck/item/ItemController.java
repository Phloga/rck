package vee.rck.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path="/item")
@AllArgsConstructor
public class ItemController {

    @GetMapping(path="/index")
    public String getItems(){
        return "item/index";
    }
}
