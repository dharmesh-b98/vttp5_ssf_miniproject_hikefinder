package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.SunRestService;

@RestController
@RequestMapping("/getSunTimings")
public class SunRestController {
    
    @Autowired
    SunRestService sunRestService;

    @GetMapping
    public ResponseEntity<Map<String, String>> calculateSunriseTime(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date dateTime, HttpSession session) {
        try {
            Long[] combinedSunTimingsLong = sunRestService.getLongSunTimings(dateTime, session);
            Date sunriseTime = new Date(combinedSunTimingsLong[0]);
            Date sunsetTime = new Date(combinedSunTimingsLong[1]);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            String sunriseTimeString = sdf.format(sunriseTime);
            String sunsetTimeString = sdf.format(sunsetTime);

            Map<String, String> response = new HashMap<>();
            response.put("sunriseTime", sunriseTimeString);
            response.put("sunsetTime", sunsetTimeString);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid datetime format"));
        }
    }
}