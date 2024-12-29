package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.constants.Constants;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.AppUser;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.Hike;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.HikeSpot;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.repo.HashRepo;

@Service
public class HikeService {

    @Autowired
    HashRepo hikeRepo;

    @Autowired
    HikeSpotService hikeSpotService;

    @Autowired
    UserService userService;

    //join a hike
    public void joinHike(String userName, String hikeId){
        Hike hike = getHike(hikeId);
        List<String> usersJoined = hike.getUsersJoined();

        if (!usersJoined.contains(userName)){
            usersJoined.add(userName);
        }

        hike.setUsersJoined(usersJoined);
        saveHike(hike);
    }


    //unjoin a hike
    public void unjoinHike(String userName, String hikeId){
        Hike hike = getHike(hikeId);
        List<String> usersJoined = hike.getUsersJoined();
        
        if (!(hike.getHost().equals(userName))){
            usersJoined.remove(userName);
        }
        hike.setUsersJoined(usersJoined);
        saveHike(hike);
    }


    //save a hike
    public void saveHike(Hike hike){
        JsonObject hikeJson = convertHikeToJson(hike);
        
        String host = hike.getHost();
        AppUser appUser = userService.getAppUser(host);
        List<String> hostedHikes = appUser.getHostedHikes();
        if (!(hostedHikes.contains(hike.getId()))){
            hostedHikes.add(hike.getId());
        }
        appUser.setHostedHikes(hostedHikes);
        userService.saveUser(appUser);

        hikeRepo.put(Constants.hikeHashRedisKey,hike.getId(),hikeJson.toString());
    }


    //remove a hike
    public void removeHike(String id){
        Hike hike = getHike(id);
        String host = hike.getHost();
        AppUser appUser = userService.getAppUser(host);
        List<String> hostedHikes = appUser.getHostedHikes();
        hostedHikes.remove(hike.getId());
        appUser.setHostedHikes(hostedHikes);
        userService.saveUser(appUser);

        hikeRepo.delete(Constants.hikeHashRedisKey, id);
    }


    //get country of a hike
    public String getHikeCountry(String hikeSpotName){
        HikeSpot hikeSpot = hikeSpotService.getHikeSpot(hikeSpotName);
        return hikeSpot.getCountry();
    }


    //get one hike
    public Hike getHike(String id){
        String hikeJsonString = (String) hikeRepo.get(Constants.hikeHashRedisKey, id);
        Hike hike = convertJsonToHike(hikeJsonString);
        return hike;
    }


    //filterring hikespots by username
    public List<Hike> getPersonalHostedHikeList(String userName){
        List<Hike> hikeList = getHikeList();
        List<Hike> personalHostedHikeList = hikeList.stream().filter(hike -> hike.getHost().equals(userName)).toList();
        return personalHostedHikeList;
    }


    //filtering hikelist by country
    public List<Hike> getFilteredHikeList(String filterBy){
        List<Hike> hikeList = getHikeList();
        List<Hike> filteredHikeList = new ArrayList<>();
        if (filterBy.equals("Japan") || filterBy.equals("Singapore") || filterBy.equals("India")){
            filteredHikeList = hikeList.stream().filter(hike -> hike.getCountry().equals(filterBy)).toList();
            System.out.println(filteredHikeList.toString());
        }
        else{
            filteredHikeList = hikeList.stream().toList();
        }
        return filteredHikeList;        
    }


    //getting hikelist
    public List<Hike> getHikeList(){
        List<String> hikeListJson = hikeRepo.values(Constants.hikeHashRedisKey);

        List<Hike> hikeList = new ArrayList<>();
        for (String hikeJson: hikeListJson){
            Hike hike = convertJsonToHike(hikeJson);
            hikeList.add(hike);
        }

        return hikeList;
    }

      
    //converting json to hike
    public Hike convertJsonToHike(String hikeJsonString){
        JsonReader reader = Json.createReader(new StringReader(hikeJsonString));
        JsonObject hikeJson = reader.readObject();

        String id = hikeJson.getString("id");
        String host = hikeJson.getString("host");
        String country = hikeJson.getString("country");
        String hikeSpotName = hikeJson.getString("hikeSpotName");

        Long dateTimeLong = Long.parseLong(hikeJson.getString("dateTime"));
        Date dateTime = new Date(dateTimeLong);

        Long sunriseTimeLong = Long.parseLong(hikeJson.getString("sunriseTime"));
        Date sunriseTime = new Date(sunriseTimeLong);

        Long sunsetTimeLong = Long.parseLong(hikeJson.getString("sunsetTime"));
        Date sunsetTime = new Date(sunsetTimeLong);

        List<String> usersJoined = new ArrayList<>();
        String [] usersJoinedString = hikeJson.getString("usersJoined").replace("[","").replace("]","").split(",");
        for (String userJoinedString :  usersJoinedString){
            usersJoined.add(userJoinedString.trim());
        }

        Hike hike = new Hike(id,host,country,hikeSpotName,dateTime,sunriseTime,sunsetTime,usersJoined);
        
        return hike;
    }


    //converting hike to json
    public JsonObject convertHikeToJson(Hike hike){
        String dateTimeLongString = String.valueOf(hike.getDateTime().getTime());
        String sunriseTimeLongString = String.valueOf(hike.getSunriseTime().getTime());
        String sunsetTimeLongString = String.valueOf(hike.getSunsetTime().getTime());
        String usersJoined = hike.getUsersJoined().toString();
        
        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonObject hikeJson = job.add("id", hike.getId())
                                    .add("host", hike.getHost())
                                    .add("country", hike.getCountry())
                                    .add("hikeSpotName", hike.getHikeSpotName())
                                    .add("dateTime", dateTimeLongString)
                                    .add("sunriseTime", sunriseTimeLongString)
                                    .add("sunsetTime", sunsetTimeLongString)
                                    .add("usersJoined", usersJoined)
                                    .build();

        return hikeJson;
    }
}
