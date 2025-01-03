package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.*;

public class AppUser {

    @Size(min=5, max=15, message= "Length has to be inbetween between 5 and 15")
    @NotEmpty(message = "Mandatory")
    private String userName;

    @NotEmpty(message="Mandatory")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message="Password needs to be 8 characters with at least one number")
    private String password;
    
    private String role;
    private List<String> hostedHikes;


    public AppUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.role = "USER";
        this.hostedHikes = new ArrayList<>();
    }

    public AppUser() {
        this.role = "USER";
        this.hostedHikes = new ArrayList<>();
    }

    public AppUser(String userName, String password, String user, List<String> hostedHikes) {
        this.userName = userName;
        this.password = password;
        this.role = user;
        this.hostedHikes = hostedHikes;
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

    public List<String> getHostedHikes() {
        return hostedHikes;
    }

    public void setHostedHikes(List<String> hostedHikes) {
        this.hostedHikes = hostedHikes;
    }

    



    

    
}
