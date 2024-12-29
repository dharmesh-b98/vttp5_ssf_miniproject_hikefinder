package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.constants.Constants;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.AppUser;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.Hike;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.repo.HashRepo;

@Service
public class AdminRestService {

    @Autowired
    UserService userService;

    @Autowired
    HikeService hikeService;

    @Autowired
    HashRepo userRepo;

    //get error json
    public JsonObject getErrorJson(){
        JsonObjectBuilder builder = Json.createObjectBuilder();
        JsonObject errorJson = builder.add("Error","You are not authorised to view that")
                                        .build();
        return errorJson;
    }


    //get app user list json with no password
    public JsonArray getAppUserJsonNoP(){
        List<AppUser> appUserList = getAppUserList();

        JsonArrayBuilder userArrayBuilder = Json.createArrayBuilder();

        for (AppUser appUser : appUserList){
            JsonArrayBuilder hikeArrayBuilder = Json.createArrayBuilder();
            List<String> hostedHikes = appUser.getHostedHikes();
            for (int i = 1 ; i<hostedHikes.size(); i++){
                String hikeId = hostedHikes.get(i);
                JsonObject hikeJson = getHostedHikeJson(hikeId);
                hikeArrayBuilder = hikeArrayBuilder.add(hikeJson);
            }
            JsonArray hikeArray = hikeArrayBuilder.build();

            JsonObjectBuilder userObjectBuilder = Json.createObjectBuilder();
            JsonObject userObject = userObjectBuilder.add("userName",appUser.getUserName())
                                            .add("role", appUser.getRole())
                                            .add("hostedHikes", hikeArray)
                                            .build();
            userArrayBuilder.add(userObject);
        }
    
        JsonArray userArraynoP = userArrayBuilder.build();
        return userArraynoP;
    }


    //get hosted hike json
    public JsonObject getHostedHikeJson(String HikeId){
        Hike hike = hikeService.getHike(HikeId);
        JsonObject hikeJson = hikeService.convertHikeToJson(hike);
        return hikeJson;
    }
    
    
    //get app user list
    public List<AppUser> getAppUserList() {
        List<String> appUserJsonListString = userRepo.values(Constants.usersHashRedisKey);
        
        List<AppUser> appUserList = new ArrayList<>();
        
        for (String appUserString : appUserJsonListString){
            AppUser appUser = userService.convertJsontoUser(appUserString);
            appUserList.add(appUser);
        }
        return appUserList;
    }
}
