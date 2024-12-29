package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.Hike;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.HikeService;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.UserService;

@Controller
@RequestMapping("/user")
public class UserProfileController {

    @Autowired
    UserService userService;

    @Autowired
    HikeService hikeService;
    
    @GetMapping("/{userName}/{user}")
    public String showUserDetails(@PathVariable("userName") String userName, @PathVariable("user") String user, HttpSession session, Model model){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null || (!sessionUserName.equals(userName))){
            return "redirect:/?loginErrorMsg=You do not have access to that";
        }     

        Boolean ownProfile = false;
        if (userName.equals(user)){
            ownProfile=true;
        }

        Boolean isAdmin = false;
        if (userService.getAppUser(userName).getRole().equals("ADMIN")){
            ownProfile=true;
            isAdmin= true;
        }

        List<Hike> personalHostedHikeList = hikeService.getPersonalHostedHikeList(user);
        
        model.addAttribute("ownProfile", ownProfile);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("user", user);
        model.addAttribute("userName", userName);
        model.addAttribute("personalHostedHikeList", personalHostedHikeList);
        return "userprofile";
    }
}
