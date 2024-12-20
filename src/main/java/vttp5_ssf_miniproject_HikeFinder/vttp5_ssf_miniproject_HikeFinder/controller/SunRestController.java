package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculate-sunrise-time")
public class SunRestController {
    
    @GetMapping
    public ResponseEntity<Map<String, String>> calculateSunriseTime(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date dateTime) {
        try {
            // Use Calendar to manipulate the datetime
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTime);

            // Example logic: Add 1 hour to simulate sunrise time
            calendar.add(Calendar.HOUR, 1);

            // Get the sunrise time
            Date sunriseTime = calendar.getTime();

            // Format the datetime as a string
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            String sunriseTimeStr = dateTimeFormat.format(sunriseTime);

            // Return the result as a JSON response
            Map<String, String> response = new HashMap<>();
            response.put("sunriseTime", sunriseTimeStr);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Handle errors
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid datetime format"));
        }
    }
}