package pl.springwebmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model
    ) {
        if (error != null) {
            model.addAttribute("loginError", "Nieprawidłowy login lub hasło.");
        }
        if (logout != null) {
            model.addAttribute("logoutMsg", "Zostałeś pomyślnie wylogowany.");
        }
        return "login";
    }
}
