package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.http.HttpSession;

@Service
public class SunRestService {
    
    RestTemplate restTemplate = new RestTemplate();


    public Long[] getLongSunTimings(Date dateTime, HttpSession session) throws ParseException{
        String apiResponse = getSunApiResponse(dateTime, session);
        JsonObject apiResultJson = convertApiResponsetoJson(apiResponse);

        String sunriseTimeString = apiResultJson.getString("sunrise");
        String sunsetTimeString = apiResultJson.getString("sunset");

        Date combinedSunriseTime = addDateToSunTime(dateTime, sunriseTimeString, true, session);
        Date combinedSunsetTime = addDateToSunTime(dateTime, sunsetTimeString, false, session);

        Long combinedSunriseTimeLong = combinedSunriseTime.getTime();
        Long combinedSunsetTimeLong = combinedSunsetTime.getTime();

        Long[] combinedSunTimingsLong = new Long[]{combinedSunriseTimeLong, combinedSunsetTimeLong};
        return combinedSunTimingsLong;        
    }


    public Date addDateToSunTime(Date dateTime, String sunTimeString, Boolean isSunrise, HttpSession session) throws ParseException{
        if(isSunrise){
            if (sunTimeString.contains("PM")){
                Instant dateTimeInstant = dateTime.toInstant();
                dateTimeInstant = dateTimeInstant.minus(Duration.ofDays(1));
                dateTime = Date.from(dateTimeInstant);
            }
        }
        
        SimpleDateFormat dateTimeSDF = new SimpleDateFormat("yyyy-MM-dd");
        String dateTimeString = dateTimeSDF.format(dateTime);

        String combinedDateString = dateTimeString + "-" + sunTimeString + "_" + "UTC"; //timezone
        SimpleDateFormat combinedDateSDF = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss aa_z");
        Date combinedDate = combinedDateSDF.parse(combinedDateString);//date and time of that correct instant in time(neeed to change to new timezone)

        combinedDateSDF.setTimeZone(TimeZone.getTimeZone((String)session.getAttribute("timeZone")));//need to get dynamically
        String combinedDateString_NewTimeZone = combinedDateSDF.format(combinedDate);
        String[] splitTimeZoneString = combinedDateString_NewTimeZone.split("_");

        SimpleDateFormat combinedDateSDF_NoTimeZone = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss aa");
        Date combinedDate_NewTimeZone = combinedDateSDF_NoTimeZone.parse(splitTimeZoneString[0]);

        return combinedDate_NewTimeZone;
    }


    public JsonObject convertApiResponsetoJson(String response){
        JsonReader reader = Json.createReader(new StringReader(response));
        JsonObject responseJson = reader.readObject();
        JsonObject responseResultJson = responseJson.getJsonObject("results");
        return responseResultJson;
    }


    public String getSunApiResponse(Date dateTime,HttpSession session){
        String lat = (String) session.getAttribute("lat");
        String lng = (String) session.getAttribute("lng");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateTimeString = sdf.format(dateTime);
        

        String url = UriComponentsBuilder
                        .fromUriString("https://api.sunrise-sunset.org/json")
                        .queryParam("lat", lat)
                        .queryParam("lng",lng)
                        .queryParam("date",dateTimeString)
                        .toUriString();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String response = responseEntity.getBody();
        return response;
    }

    
}
