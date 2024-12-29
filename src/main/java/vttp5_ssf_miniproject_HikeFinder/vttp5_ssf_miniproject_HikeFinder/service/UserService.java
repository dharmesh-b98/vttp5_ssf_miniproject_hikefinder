package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.*;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.AppUser;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.repo.HashRepo;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.repo.ValueRepo;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.constants.*;

@Service
public class UserService {
    
    @Autowired
    HashRepo userRepo;

    @Autowired
    ValueRepo userValueRepo;
    
    //getting list of userNames
    public List<String> getUserNameList(){
       List<String> userNameList = new ArrayList<>(userRepo.keys(Constants.usersHashRedisKey));
       return userNameList;
    }


    //gets a user from redis
    public AppUser getAppUser(String UserName){
        String appUserJson = (String) userRepo.get(Constants.usersHashRedisKey, UserName);
        AppUser appUser = convertJsontoUser(appUserJson);
        return appUser;
    }


    //save a user to redis
    public void saveUser(AppUser appUser){
        JsonObject userJson = convertUsertoJson(appUser);
        userRepo.put(Constants.usersHashRedisKey, appUser.getUserName(), userJson.toString());
    }


    //checks if username exists and checks if password correct
    public Boolean checkLoginCredentials(String userName, String password){
        if (checkUserExists(userName)){
            if (checkPasswordCorrect(userName, password)){
                return true;
            }
        }
        return false;
    }


    //check if username exists (helper)
    public Boolean checkUserExists(String userName){
        return userRepo.hasKey(Constants.usersHashRedisKey, userName);
    }


    //check password (helper)
    public Boolean checkPasswordCorrect(String userName, String password){ //only if user exists
        String appUserJsonString = (String) userRepo.get(Constants.usersHashRedisKey, userName);
        AppUser appUser = convertJsontoUser(appUserJsonString);

        if (password.equals(appUser.getPassword())){
            return true;
        }
        return false;
    }


    //initialise a userList (used in command line runner)
    public void initialiseUserList(String adminUserName, String adminPassword){
        Boolean userListExists = userValueRepo.checkExists(Constants.usersHashRedisKey);
        if(!userListExists){
            //need to add password to env variable
            AppUser admin = new AppUser(adminUserName, adminPassword,"ADMIN", new ArrayList<>());
            String adminString = convertUsertoJson(admin).toString();
            userRepo.put(Constants.usersHashRedisKey,admin.getUserName(),adminString);
        }
    }


    //convert user to json
    public JsonObject convertUsertoJson(AppUser appUser){
        String userName = appUser.getUserName();
        String password = appUser.getPassword();

        String role = appUser.getRole();
        List<String> hostedHikes = appUser.getHostedHikes();

        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonObject userJson = job.add("userName",userName)
                                    .add("password", password)
                                    .add("role", role)
                                    .add("hostedHikes", hostedHikes.toString())
                                    .build();

        return userJson;
    }


    //convert json to user
    public AppUser convertJsontoUser(String appUserJsonString){
        JsonReader reader = Json.createReader(new StringReader(appUserJsonString));
        JsonObject appUserJson= reader.readObject();

        String userName = appUserJson.getString("userName");
        String password = appUserJson.getString("password");
        String role = appUserJson.getString("role");
        String hostedHikesString = appUserJson.getString("hostedHikes");
        List<String> hostedHikes = new ArrayList<>();
        
        String [] hostedHikesStringArray = hostedHikesString.replace("[","").replace("]","").split(",");
        for (String hostedHikesStringEntry : hostedHikesStringArray ){
            hostedHikes.add(hostedHikesStringEntry.trim());
        }

        AppUser appUser = new AppUser(userName, password, role, hostedHikes);
        return appUser;
    }

}
