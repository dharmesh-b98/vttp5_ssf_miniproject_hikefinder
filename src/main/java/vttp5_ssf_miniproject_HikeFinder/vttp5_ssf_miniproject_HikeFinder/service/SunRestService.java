package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.servlet.http.HttpSession;

@Service
public class SunRestService {
    
    RestTemplate restTemplate = new RestTemplate();


    public String getLongSunTimings(Date dateTime, String response, HttpSession session){
        String apiResponse = getSunApiResponse(dateTime, session);
        JsonObject apiResultJson = convertApiResponsetoJson(apiResponse);
        String sunriseTimeString = apiResultJson.getString("sunrise");
        String sunsetTimeString = apiResultJson.getString("sunset");

        
    }

    public Date addDateToSunTime(Date dateTime, String sunTime){
        get
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
