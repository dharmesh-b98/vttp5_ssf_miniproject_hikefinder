package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.Hike;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.HikeSpot;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.HikeService;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.HikeSpotService;

@Controller
@RequestMapping("/hostedhikes")
public class HostedHikesController {

    @Autowired
    HikeService hikeService;

    @Autowired
    HikeSpotService hikeSpotService;

    @GetMapping("{userName}/addHike/{hikeSpotName}")
    public String addHike(@PathVariable("userName") String userName,@PathVariable("hikeSpotName") String hikeSpotName, HttpSession session, Model model){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null){
            return "redirect:/";
        }   
        if (!sessionUserName.equals(userName)){
            return "redirect:/";
        }
               
        
        Hike hike = new Hike();
        hike.setHost(userName);
        hike.setHikeSpotName(hikeSpotName);
        List<String> usersJoined = hike.getUsersJoined();
        usersJoined.add(userName);
        hike.setUsersJoined(usersJoined);

        model.addAttribute("hike", hike);
        model.addAttribute("userName", userName);
        model.addAttribute("hikeSpotName", hikeSpotName);

        HikeSpot hikeSpot = hikeSpotService.getHikeSpot(hikeSpotName);
        session.setAttribute("lat", hikeSpot.getLat().toString());
        session.setAttribute("lng", hikeSpot.getLng().toString());
        session.setAttribute("timeZone", hikeSpot.getTimeZone());
        return "addhikeform";
    }

    @PostMapping("{userName}/addHike/{hikeSpotName}")
    public String addHikePost(@Valid @ModelAttribute Hike hike, BindingResult binding, @PathVariable("userName") String userName, @PathVariable("hikeSpotName") String hikeSpotName, HttpSession session){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null){
            return "redirect:/";
        }   
        if (!sessionUserName.equals(userName)){
            return "redirect:/";
        }

        if (binding.hasErrors()){
            return "addhikeform";
        }

        hikeService.saveHike(hike);
        return "redirect:/";
    }


    @GetMapping("{userName}/hostedHikeList")
    public String gethostedHikeList(@PathVariable("userName") String userName, HttpSession session, Model model){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null){
            return "redirect:/";
        }   
        if (!sessionUserName.equals(userName)){
            return "redirect:/";
        }

        List<Hike> hikeList = hikeService.getHikeList();
        model.addAttribute("hikeList", hikeList);
        return "hostedhikelist";
    }
}
