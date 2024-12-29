package vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.constants.Constants;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.model.HikeSpot;
import vttp5_ssf_miniproject_HikeFinder.vttp5_ssf_miniproject_HikeFinder.repo.HashRepo;

@Service
public class HikeSpotService {
    @Autowired
    HashRepo hikeSpotRepo;

    @Value("${google.api.key}")
    String googleApiKey;


    //finding center coordinate of map
    public String getCenterCoordinates(List<HikeSpot> hikeSpotList){
        Double latSum = 0.0;
        Double lngSum = 0.0;
        for (HikeSpot hikeSpot : hikeSpotList){
            latSum += hikeSpot.getLat();
            lngSum += hikeSpot.getLng();
        }
        Double latAve = latSum/hikeSpotList.size();
        Double lngAve = lngSum/hikeSpotList.size();
        
        String output = String.valueOf(latAve) + "," + String.valueOf(lngAve);
        return output;
    }


    //getting map zoom level
    public String getMapZoom(String filterBy){
        if (filterBy.equals("Japan")){
            return "4";
        }
        else if (filterBy.equals("India")){
            return "3.5";
        }
        else if (filterBy.equals("Singapore")) {
            return "10";
        }
        return "3";
    }


    //getting google map api link
    public String getGoogleMapApiUrl(){
        String googleMapApiUrl = UriComponentsBuilder
                        .fromUriString("https://maps.googleapis.com/maps/api/js")
                        .queryParam("key", googleApiKey)
                        .queryParam("callback","initMap")
                        .toUriString();
        return googleMapApiUrl;
    }


    //getting google map marker link
    public String getGoogleMapMarkerUrl(){
        String googleMapMarkerUrl = UriComponentsBuilder
                        .fromUriString("https://maps.googleapis.com/maps/api/js")
                        .queryParam("key", googleApiKey)
                        .queryParam("libraries","maps,marker")
                        .queryParam("v","beta")
                        .toUriString();
        return googleMapMarkerUrl;
    }


    //filtering hikespotlist by country
    public List<HikeSpot> getFilteredHikeSpotList(String filterBy){
        List<HikeSpot> hikeSpotList = getHikeSpotList();
        List<HikeSpot> filteredHikeSpotList = new ArrayList<>();
        if (filterBy.equals("Japan") || filterBy.equals("Singapore") || filterBy.equals("India")){
            filteredHikeSpotList = hikeSpotList.stream().filter(hikeSpot -> hikeSpot.getCountry().equals(filterBy)).toList();
        }
        else{
            filteredHikeSpotList = hikeSpotList.stream().toList();
        }
        return filteredHikeSpotList;        
    }


    //get one hikespot
    public HikeSpot getHikeSpot(String hikeSpotName){
        String hikeSpotString = (String) hikeSpotRepo.get(Constants.hikeSpotHashRedisKey, hikeSpotName);
        HikeSpot hikeSpot = convertJsontoHikeSpot(hikeSpotString);
        return hikeSpot;
    }


    //get hikespotlist
    public List<HikeSpot> getHikeSpotList(){
        List<String> hikeSpotListString = hikeSpotRepo.values(Constants.hikeSpotHashRedisKey);
        List<HikeSpot> hikeSpotList = new ArrayList<>();
        for (String hikeSpotJsonString : hikeSpotListString){
            HikeSpot hikeSpot = convertJsontoHikeSpot(hikeSpotJsonString);
            hikeSpotList.add(hikeSpot);
        }
        return hikeSpotList;
    }


    //converting json to hikespot
    public HikeSpot convertJsontoHikeSpot(String hikeSpotJsonString){
        JsonReader reader = Json.createReader(new StringReader(hikeSpotJsonString));
        JsonObject hikeSpotJson = reader.readObject();

        Double lat = Double.parseDouble(hikeSpotJson.getString("lat"));
        Double lng = Double.parseDouble(hikeSpotJson.getString("lng"));
        String name = hikeSpotJson.getString("name");
        String description = hikeSpotJson.getString("description");
        String country = hikeSpotJson.getString("country");
        String timeZone = hikeSpotJson.getString("timeZone");

        List<String> visitorList = new ArrayList<>();
        String [] visitorListString = hikeSpotJson.getString("visitList").replace("[","").replace("]","").split(",");
        for (String visitorString : visitorListString ){
            visitorList.add(visitorString.trim());
        }

        HikeSpot hikeSpot = new HikeSpot(lat, lng, name, description,country,timeZone,visitorList);
        return hikeSpot;
    }


    //initialising hikespots into redis
    public void addHikeSpots() throws IOException{
        JsonArray hikeSpotJsonArray = getHikeSpotJsonArray(Constants.hikeSpotJsonDatapath);
        for (int i = 0; i < hikeSpotJsonArray.size(); i++){
            JsonObject hikeSpot = hikeSpotJsonArray.getJsonObject(i);
            hikeSpotRepo.put(Constants.hikeSpotHashRedisKey, hikeSpot.getString("name"),hikeSpot.toString());
        }
    }

    
    //converting the read string into json array
    public JsonArray getHikeSpotJsonArray(String inputFilePath) throws IOException{
        String hikeSpotJsonString = ReadHikeSpotJson(new File(inputFilePath));
        JsonReader reader = Json.createReader(new StringReader(hikeSpotJsonString));
        JsonArray hikeSpotJsonArray = reader.readArray();
        return hikeSpotJsonArray;
    }


    //reading hikespotjson file
    public String ReadHikeSpotJson(File inputFilePath) throws IOException{
		FileReader fr = new FileReader(inputFilePath);
		BufferedReader br = new BufferedReader(fr);
		
		String fullJsonString = "";
		String line = "";
		while ((line = br.readLine()) != null){
			fullJsonString += line;
		}
		br.close();
		fr.close();

		return fullJsonString;
	}
}
