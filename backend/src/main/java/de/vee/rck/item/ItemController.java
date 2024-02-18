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

    @GetMapping(path="/index")
    public String getItems(){
        return "item/index";
    }
}
