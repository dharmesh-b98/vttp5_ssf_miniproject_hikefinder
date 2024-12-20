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
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.constants.*;;;

@Service
public class UserService {
    
    @Autowired
    HashRepo userRepo;

    @Autowired
    ValueRepo userValueRepo;

    public void saveUser(AppUser appUser){
        JsonObject userJson = convertUsertoJson(appUser);
        userRepo.put(Constants.usersHashRedisKey, appUser.getUserName(), userJson.toString());
    }


    public Boolean checkLoginCredentials(String userName, String password){
        initialiseUserList();
        if (checkUserExists(userName)){
            System.out.println("\n\n\n\n\n\n\n\n"+"USERNAME EXISTS");
            if (checkPasswordCorrect(userName, password)){
                return true;
            }
        }
        return false;
    }

    public void initialiseUserList(){
        Boolean userListExists = userValueRepo.checkExists(Constants.usersHashRedisKey);
        if(!userListExists){
            //need to add password to env variable
            AppUser admin = new AppUser("admin","adminpassword","ADMIN", new ArrayList<>());
            String adminString = convertUsertoJson(admin).toString();
            userRepo.put(Constants.usersHashRedisKey,admin.getUserName(),adminString);
        }
    }

    public Boolean checkUserExists(String userName){
        return userRepo.hasKey(Constants.usersHashRedisKey, userName);
    }


    public Boolean checkPasswordCorrect(String userName, String password){ //only if user exists
        String appUserJsonString = (String) userRepo.get(Constants.usersHashRedisKey, userName);
        AppUser appUser = convertJsontoUser(appUserJsonString);

        if (password.equals(appUser.getPassword())){
            return true;
        }
        return false;
    }


    public JsonObject convertUsertoJson(AppUser appUser){
        String userName = appUser.getUserName();
        String password = appUser.getPassword();

        String role = appUser.getRole();
        List<String> hikesCompleted = appUser.getHikesCompleted();

        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonObject userJson = job.add("userName",userName)
                                    .add("password", password)
                                    .add("role", role)
                                    .add("hikesCompleted", hikesCompleted.toString())
                                    .build();

        return userJson;
    }


    public AppUser convertJsontoUser(String appUserJsonString){
        JsonReader reader = Json.createReader(new StringReader(appUserJsonString));
        JsonObject appUserJson= reader.readObject();

        String userName = appUserJson.getString("userName");
        String password = appUserJson.getString("password");
        String role = appUserJson.getString("role");
        String hikesCompletedString = appUserJson.getString("hikesCompleted");
        List<String> hikesCompleted = new ArrayList<>();
        
        String [] hikesCompletedStringArray = hikesCompletedString.replace("[","").replace("]","").split(",");
        for (String hikesCompletedStringEntry : hikesCompletedStringArray ){
            hikesCompleted.add(hikesCompletedStringEntry.trim());
        }

        AppUser appUser = new AppUser(userName, password, role, hikesCompleted);
        return appUser;
    }

}
