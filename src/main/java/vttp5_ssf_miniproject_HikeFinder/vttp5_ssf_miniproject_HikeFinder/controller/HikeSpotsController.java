package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.HikeSpot;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.HikeSpotService;

@Controller
@RequestMapping("/hikeSpots")
public class HikeSpotsController {
    
    @Autowired
    HikeSpotService hikeSpotService;

    @GetMapping("/{userName}/home")
    public String showHikeSpotList(@PathVariable("userName") String userName, @RequestParam(name="filterBy", defaultValue = "Unfiltered") String filterBy, HttpSession session, Model model ){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null || (!sessionUserName.equals(userName))){
            return "redirect:/?loginErrorMsg=You do not have access to that";
        }

        List<HikeSpot> hikeSpotList = hikeSpotService.getFilteredHikeSpotList(filterBy);
        String centerCoordinates = hikeSpotService.getCenterCoordinates(hikeSpotList);
        String mapZoom = hikeSpotService.getMapZoom(filterBy);

        model.addAttribute("hikeSpotList", hikeSpotList);
        model.addAttribute("centerCoordinates", centerCoordinates);
        model.addAttribute("mapZoom", mapZoom);
        model.addAttribute("userName", userName);
        model.addAttribute("googleMapApiUrl",hikeSpotService.getGoogleMapApiUrl());
        model.addAttribute("googleMapMarkerUrl",hikeSpotService.getGoogleMapMarkerUrl());
        return "hikespotoverview";
    }


}
