package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.*;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.HikeSpotService;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.UserService;

@Controller
@RequestMapping("")
public class LoginController {
    
    @Autowired
    UserService userService;

    @Autowired
    HikeSpotService hikeSpotService;


    //login
    @GetMapping("")
    public String showLogin() throws IOException{
        hikeSpotService.addHikeSpots(); // REMEMBER TO CHANGE
        return "login";
    }

    @PostMapping("")
    public String showLoginPost(@RequestBody MultiValueMap<String, String> loginMap, HttpSession session, Model model){
        String userName = loginMap.getFirst("userName");
        String password = loginMap.getFirst("password");

        System.out.println("\n\n\n\n\n" + userName + "\n\n\n\n");
        
        if (userService.checkLoginCredentials(userName, password)){
            session.setAttribute("userName", userName );
            System.out.println("\n\n\n\n\n Reached this point \n\n\n\n\n");
            return "redirect:/hikeSpots/"+userName+"/home";
        }
        return "redirect:/";
        
    }

    //register
    @GetMapping("/register")
    public String showRegister(Model model){
        AppUser appUser = new AppUser();
        model.addAttribute("appUser", appUser);
        return "register";
    }

    @PostMapping("/register")
    public String showRegisterPost(@Valid @ModelAttribute("appUser") AppUser appUser, BindingResult binding){
        if (binding.hasErrors()){
            return "register";
        }

        userService.saveUser(appUser);
        return "redirect:/";
    }
}