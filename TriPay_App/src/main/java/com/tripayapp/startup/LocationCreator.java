package com.tripayapp.startup;

import com.tripayapp.entity.Banks;
import com.tripayapp.entity.LocationDetails;
import com.tripayapp.repositories.LocationDetailsRepository;
import com.tripayapp.util.StartupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationCreator {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LocationDetailsRepository locationDetailsRepository;

    public LocationCreator(LocationDetailsRepository locationDetailsRepository) {
        this.locationDetailsRepository = locationDetailsRepository;
    }

    public void create(){
        String fileName = StartupUtil.CSV_FILE+"locations.csv";
        ArrayList<LocationDetails> locations = readLocationsFromFile(fileName);
        if(locations != null){
            for(LocationDetails location : locations){
                if(location != null) {
                    String pinCode = location.getPinCode();
                    LocationDetails exists = locationDetailsRepository.findLocationByPin(pinCode);
                    if (exists == null) {
                        locationDetailsRepository.save(location);
                    }
                }
            }
        }


    }


    public ArrayList<LocationDetails> readLocationsFromFile(String fileName) {
        ArrayList<LocationDetails> locations = new ArrayList<>();
        System.err.println(fileName);
        BufferedReader br = null;
        String line = "";
        try {

            br = new BufferedReader(new FileReader(fileName));
            LocationDetails location = null;
            while ((line = br.readLine()) != null) {
                Pattern p = Pattern.compile("(([^\"][^,]*)|\"([^\"]*)\"),?");
                Matcher m = p.matcher(line);

                location = new LocationDetails();
                String value = null;
                int index = 1;
                while (m.find()) {
                    if (m.group(2) != null) {
                        value = m.group(2);
                        System.err.println("group 2 ::"+value);
                    }

                    if (m.group(3) != null) {
                        value = m.group(3);
                        System.err.println("group 3 ::"+value);
                    }

                    if (value != null) {
                        if (location != null) {
                            switch (index) {
                                case 1:
                                    location.setLocality(value);
                                    break;
                                case 2:
                                    location.setPinCode(value);
                                    break;
                                case 3:
                                    location.setRegionName(value);
                                    break;
                                case 4:
                                    location.setCircleName(value);
                                    break;
                                case 5:
                                    location.setDistrictName(value);
                                    break;
                                case 6:
                                    location.setStateName(value);
                                    break;
                                default:
                                    break;
                            }
                            index = index + 1;
                        }
                    }
                    locations.add(location);

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return locations;
    }


}
