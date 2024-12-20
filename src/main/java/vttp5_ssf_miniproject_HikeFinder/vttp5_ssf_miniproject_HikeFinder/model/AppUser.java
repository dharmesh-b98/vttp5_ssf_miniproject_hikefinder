package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.*;

public class AppUser {

    @Size(min=5, max=15, message= "Length has to be inbetween between 5 and 15")
    @NotEmpty(message = "Mandatory")
    private String userName;

    //@Size(min=5, max=15, message= "Length has to be inbetween between 5 and 15")
    @NotEmpty(message="Mandatory")//need to write message for the password
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[@#$%^&+=]).{8,}$", message="Ensure that the password format matches the following rules")
    private String password;
    
    private String role;
    private List<String> hikesCompleted;


    public AppUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.role = "USER";
        this.hikesCompleted = new ArrayList<>();
    }

    public AppUser() {
        this.role = "USER";
        this.hikesCompleted = new ArrayList<>();
    }

    public AppUser(String userName, String password, String user, List<String> hikesCompleted) {
        this.userName = userName;
        this.password = password;
        this.role = user;
        this.hikesCompleted = hikesCompleted;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getHikesCompleted() {
        return hikesCompleted;
    }

    public void setHikesCompleted(List<String> hikesCompleted) {
        this.hikesCompleted = hikesCompleted;
    }



    

    
}
