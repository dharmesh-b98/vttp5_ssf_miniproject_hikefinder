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
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.Hike;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.repo.HashRepo;

@Service
public class HikeService {

    @Autowired
    HashRepo hikeRepo;

    public void saveHike(Hike hike){
        JsonObject hikeJson = convertHikeToJson(hike);
        hikeRepo.put(Constants.hikeHashRedisKey,hike.getId(),hikeJson.toString());
    }

    
    public Hike getHike(String id){
        String hikeJsonString = (String) hikeRepo.get(Constants.hikeHashRedisKey, id);
        Hike hike = convertJsonToHike(hikeJsonString);
        return hike;
    }


    public List<Hike> getHikeList(){
        List<String> hikeListJson = hikeRepo.values(Constants.hikeHashRedisKey);

        List<Hike> hikeList = new ArrayList<>();
        for (String hikeJson: hikeListJson){
            Hike hike = convertJsonToHike(hikeJson);
            hikeList.add(hike);
        }
        return hikeList;
    }
    

    public Hike convertJsonToHike(String hikeJsonString){
        JsonReader reader = Json.createReader(new StringReader(hikeJsonString));
        JsonObject hikeJson = reader.readObject();

        String id = hikeJson.getString("id");
        String host = hikeJson.getString("host");
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
            usersJoined.add(userJoinedString);
        }

        Hike hike = new Hike(id,host,hikeSpotName,dateTime,sunriseTime,sunsetTime,usersJoined);
        
        return hike;
    }


    public JsonObject convertHikeToJson(Hike hike){
        String dateTimeLongString = String.valueOf(hike.getDateTime().getTime());
        String sunriseTimeLongString = String.valueOf(hike.getSunriseTime().getTime());
        String sunsetTimeLongString = String.valueOf(hike.getSunsetTime().getTime());
        String usersJoined = hike.getUsersJoined().toString();
        
        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonObject hikeJson = job.add("id", hike.getId())
                                    .add("host", hike.getHost())
                                    .add("hikeSpotName", hike.getHikeSpotName())
                                    .add("dateTime", dateTimeLongString)
                                    .add("sunriseTime", sunriseTimeLongString)
                                    .add("sunsetTime", sunsetTimeLongString)
                                    .add("usersJoined", usersJoined)
                                    .build();

        return hikeJson;
    }
}
