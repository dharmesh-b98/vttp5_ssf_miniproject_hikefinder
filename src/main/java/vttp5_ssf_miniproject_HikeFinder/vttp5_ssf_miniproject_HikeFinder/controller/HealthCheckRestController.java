package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.HikeSpot;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.HikeSpotService;

@RestController
@RequestMapping("healthCheck")
public class HealthCheckRestController {
    @Autowired
    HikeSpotService hikeSpotService;

    @ResponseBody
    @GetMapping("/status")
    public ResponseEntity<String> checkHealth(){
        try{
            List<HikeSpot> hikeSpotList = hikeSpotService.getHikeSpotList();
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body("{}");
            return response;
        }
        catch (Exception e){
            ResponseEntity<String> response = ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).contentType(MediaType.APPLICATION_JSON).body("{}");
            return response;
        }
    }
}
