package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.*;

public class Hike {
    
    @NotEmpty(message = "mandatory")
    private String id;

    @NotEmpty(message = "mandatory")
    private String host;

    @NotEmpty(message = "mandatory")
    private String country;

    @NotEmpty(message = "choose a hike spot")
    private String hikeSpotName;

    @NotNull(message = "mandatory")
    @Future(message = "Has to be future date")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateTime;

    @NotNull(message = "mandatory")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date sunriseTime;

    @NotNull(message = "mandatory")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date sunsetTime;

    @NotEmpty(message = "mandatory")
    private List<String> usersJoined;
    
    
    public Hike() {
        this.id = UUID.randomUUID().toString();
        this.usersJoined = new ArrayList<String>();
    }  

    public Hike(@NotEmpty(message = "mandatory") String id, @NotEmpty(message = "mandatory") String host,
            @NotEmpty(message = "mandatory") String country,
            @NotEmpty(message = "choose a hike spot") String hikeSpotName,
            @NotNull(message = "mandatory") @Future(message = "Has to be future date") Date dateTime,
            @NotNull(message = "mandatory") Date sunriseTime, @NotNull(message = "mandatory") Date sunsetTime) {
        this.id = id;
        this.host = host;
        this.country = country;
        this.hikeSpotName = hikeSpotName;
        this.dateTime = dateTime;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;

        List<String> emptyUsersJoined = new ArrayList<>();
        emptyUsersJoined.add(host);
        this.usersJoined = emptyUsersJoined;
    }

    public Hike(@NotEmpty(message = "mandatory") String id, @NotEmpty(message = "mandatory") String host,
            @NotEmpty(message = "mandatory") String country,
            @NotEmpty(message = "choose a hike spot") String hikeSpotName,
            @NotNull(message = "mandatory") @Future(message = "Has to be future date") Date dateTime,
            @NotNull(message = "mandatory") Date sunriseTime, @NotNull(message = "mandatory") Date sunsetTime,
            @NotEmpty(message = "mandatory") List<String> usersJoined) {
        this.id = id;
        this.host = host;
        this.country = country;
        this.hikeSpotName = hikeSpotName;
        this.dateTime = dateTime;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.usersJoined = usersJoined;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHikeSpotName() {
        return hikeSpotName;
    }

    public void setHikeSpotName(String hikeSpotName) {
        this.hikeSpotName = hikeSpotName;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Date getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(Date sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public Date getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(Date sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public List<String> getUsersJoined() {
        return usersJoined;
    }

    public void setUsersJoined(List<String> usersJoined) {
        this.usersJoined = usersJoined;
    }

}
