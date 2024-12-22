package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.HikeSpot;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.HikeSpotService;

@Controller
@RequestMapping("/hikeSpots")
public class HomeController {
    
    @Autowired
    HikeSpotService hikeSpotService;

    @GetMapping("/{userName}/home")
    public String showUserHome(@PathVariable("userName") String userName, HttpSession session, Model model ){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null){
            return "redirect:/";
        }   
        if (!sessionUserName.equals(userName)){
            return "redirect:/";
        }

        List<HikeSpot> hikeSpotList = hikeSpotService.getHikeSpotList();
        model.addAttribute(hikeSpotList);
        return "hikespotoverview";
    }
}
