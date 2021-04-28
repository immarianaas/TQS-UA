package ua.tqs.airquality;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataAccess {
    
    @Autowired
    private CallExterior callExterior;

    @Autowired
    private Cache cache;

    public Map<String, String> getLocationsByCountry(String country) {
        Map<String, String> ret = cache.getLocationsByCountry(country);
        if (ret != null) return ret;
        ret = callExterior.getLocationsByCountry(country);
        cache.saveLocationsByCountry(country, ret);
        return ret;
    }

    public String getInfoByStation(String stationurl, TreeMap<String, HashMap<String, Integer[]>> valuesPerDate) {
        TreeMap<String, HashMap<String, Integer[]>> ret = cache.getInfoByStation(stationurl);
        String station_name = null;
        if (ret != null) { 
            valuesPerDate.putAll(ret);
            station_name = cache.getStationNameByUrl(stationurl);
            if (station_name != null) return station_name; // should ALWAYS happen
        }
        station_name = callExterior.getInfoByStation(stationurl, valuesPerDate);
        cache.saveInfoByStation(stationurl, station_name, valuesPerDate);
        return station_name;
    }


}
