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
import org.springframework.web.bind.annotation.RequestParam;

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

    //Add Hikes GET
    @GetMapping("{userName}/addHike/{hikeSpotName}")
    public String addHike(@PathVariable("userName") String userName,@PathVariable("hikeSpotName") String hikeSpotName, HttpSession session, Model model){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null || (!sessionUserName.equals(userName))){
            return "redirect:/?loginErrorMsg=You do not have access to that";
        }
               
        
        Hike hike = new Hike();
        hike.setHost(userName);
        hike.setCountry(hikeService.getHikeCountry(hikeSpotName));
        hike.setHikeSpotName(hikeSpotName);
        List<String> usersJoined = hike.getUsersJoined();
        usersJoined.add(userName);
        hike.setUsersJoined(usersJoined);

        model.addAttribute("hike", hike);
        model.addAttribute("userName", userName);
        model.addAttribute("hikeSpotName", hikeSpotName);

        HikeSpot hikeSpot = hikeSpotService.getHikeSpot(hikeSpotName);
        session.setAttribute("lat", hikeSpot.getLat().toString());// to be used by functions in SunRestService
        session.setAttribute("lng", hikeSpot.getLng().toString());
        session.setAttribute("timeZone", hikeSpot.getTimeZone());
        return "addhikeform";
    }


    //Add Hikes POST
    @PostMapping("{userName}/addHike/{hikeSpotName}")
    public String addHikePost(@Valid @ModelAttribute Hike hike, BindingResult binding, @PathVariable("userName") String userName, @PathVariable("hikeSpotName") String hikeSpotName, HttpSession session){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null || (!sessionUserName.equals(userName))){
            return "redirect:/?loginErrorMsg=You do not have access to that";
        }

        if (binding.hasErrors()){
            return "addhikeform";
        }

        hikeService.saveHike(hike);
        return "redirect:/hostedhikes/" + userName + "/hostedHikeList";
    }


    //Remove Hike GET (button in profile)
    @GetMapping("{userName}/{user}/removeHike/{hikeId}")
    public String addHikePost( @PathVariable("userName") String userName, @PathVariable("user") String user, @PathVariable("hikeId") String hikeId, HttpSession session){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null || (!sessionUserName.equals(userName))){
            return "redirect:/?loginErrorMsg=You do not have access to that";
        }

        Hike hike = hikeService.getHike(hikeId);
        if (hike.getHost().equals(userName)){
            hikeService.removeHike(hikeId);
        }
        
        return "redirect:/user/" + userName + "/" + user;
    }


    //Get HikeList
    @GetMapping("{userName}/hostedHikeList")
    public String getHostedHikeList(@PathVariable("userName") String userName, @RequestParam(name="filterBy", defaultValue="Unfiltered") String filterBy, HttpSession session, Model model){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null || (!sessionUserName.equals(userName))){
            return "redirect:/?loginErrorMsg=You do not have access to that";
        }

        List<Hike> hikeList = hikeService.getFilteredHikeList(filterBy);
        
        model.addAttribute("hikeList", hikeList);
        model.addAttribute("userName", userName);

        return "hostedhikelist";
    }

    
    //Join Hike
    @GetMapping("{userName}/join/{hikeId}")
    public String joinHike(@PathVariable("userName") String userName, @PathVariable("hikeId") String hikeId, HttpSession session){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null || (!sessionUserName.equals(userName))){
            return "redirect:/?loginErrorMsg=You do not have access to that";
        }

        hikeService.joinHike(userName, hikeId);
        return "redirect:/hostedhikes/"+ userName + "/hostedHikeList";    
    }


    //Unjoin Hike
    @GetMapping("{userName}/unjoin/{hikeId}")
    public String unjoinHike(@PathVariable("userName") String userName, @PathVariable("hikeId") String hikeId, HttpSession session){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null || (!sessionUserName.equals(userName))){
            return "redirect:/?loginErrorMsg=You do not have access to that";
        }

        hikeService.unjoinHike(userName, hikeId);
        return "redirect:/hostedhikes/"+ userName + "/hostedHikeList";    
    }
}
