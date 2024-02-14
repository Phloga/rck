package de.vee.rck.home;

import de.vee.rck.item.ItemRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@NoArgsConstructor
public class HomePageController {

    static final String rootRedirect = "/index.html";

    Logger logger = LoggerFactory.getLogger(HomePageController.class);;

    /*
    @GetMapping("/")
    public RedirectView indexRedirect() {
        return new RedirectView(rootRedirect);
    }*/

    @GetMapping("/")
    public String index(HttpServletRequest request) {
        //CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        //logger.info("{}={}", token.getHeaderName(), token.getToken());

        return "index";
    }
}
