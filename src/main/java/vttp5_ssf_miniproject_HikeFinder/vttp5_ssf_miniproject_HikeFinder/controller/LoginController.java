package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    //Login GET
    @GetMapping("/")
    public String showLogin(@RequestParam(name="loginErrorMsg", defaultValue="") String loginErrorMsg, Model model) throws IOException{
        model.addAttribute("loginErrorMsg", loginErrorMsg);
        return "login";
    }
    

    //Login POST
    @PostMapping("")
    public String showLoginPost(@RequestBody MultiValueMap<String, String> loginMap, HttpSession session, Model model){
        String userName = loginMap.getFirst("userName");
        String password = loginMap.getFirst("password");
        
        if (userService.checkLoginCredentials(userName, password)){
            session.setAttribute("userName", userName );
            return "redirect:/hikeSpots/"+userName+"/home";
        }
        return "redirect:/?loginErrorMsg=Wrong Credentials";
    }


    //Logout
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/?loginErrorMsg=Logged Out";
    }


    //Register GET
    @GetMapping("/register")
    public String showRegister(Model model){
        AppUser appUser = new AppUser();
        model.addAttribute("appUser", appUser);
        return "register";
    }


    //Register POST
    @PostMapping("/register")
    public String showRegisterPost(@Valid @ModelAttribute("appUser") AppUser appUser, BindingResult binding){
        if (binding.hasErrors()){
            return "register";
        }

        if (userService.getUserNameList().contains(appUser.getUserName())){
            ObjectError err = new ObjectError("globalError", "Username is already taken");
            binding.addError(err);
            return "register";
        }

        userService.saveUser(appUser);
        return "redirect:/?loginErrorMsg=Registration successful";
    }
}