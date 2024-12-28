package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.HikeSpotService;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service.UserService;

@SpringBootApplication
public class Vttp5SsfMiniprojectHikeFinderApplication implements CommandLineRunner{
	@Autowired
	HikeSpotService hikeSpotService;

	@Autowired
	UserService userService;

	@Value("${admin.username}")
	String adminUserName;

	@Value("${admin.password}")
	String adminPassword;
	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Vttp5SsfMiniprojectHikeFinderApplication.class);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		hikeSpotService.addHikeSpots();
		userService.initialiseUserList(adminUserName, adminPassword);
	}


}
