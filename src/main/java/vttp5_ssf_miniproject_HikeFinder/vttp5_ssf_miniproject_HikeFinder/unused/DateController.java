package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.unused;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
@RequestMapping("/calculate-end-date")
public class DateController {

    @GetMapping
    public ResponseEntity<Map<String, String>> calculateEndDate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate) {
        try {
            // Use Calendar to manipulate the date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            // Add 7 days to the start date
            calendar.add(Calendar.DATE, 7);

            // Get the end date
            Date endDate = calendar.getTime();

            // Format the end date as a string
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String endDateStr = dateFormat.format(endDate);

            // Return the result as a JSON response
            Map<String, String> response = new HashMap<>();
            response.put("endDate", endDateStr);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Handle errors
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date format"));
        }
    }
}
