package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.AppUser;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.AdminRestService;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.UserService;

@RestController
@RequestMapping("/adminInfo")
public class AdminRestController {

    @Autowired
    AdminRestService adminRestService;

    @Autowired
    UserService userService;
    
    @GetMapping(path="/{userName}", produces="application/json")
    public ResponseEntity<String> getUserListJson(@PathVariable("userName") String userName, HttpSession session){
        String sessionUserName = (String) session.getAttribute("userName");
        if (sessionUserName == null || (!sessionUserName.equals(userName))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(adminRestService.getErrorJson().toString());
        }

        AppUser appUser= userService.getAppUser(sessionUserName);
        if (!(appUser.getRole().equals("ADMIN"))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(adminRestService.getErrorJson().toString());
        }

        return ResponseEntity.status(HttpStatus.OK).body(adminRestService.getAppUserJsonNoP().toString());
    }
}
