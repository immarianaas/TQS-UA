package ua.tqs.airquality;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class Cache {
    // private Map<String, Map<String, String>> locations = new HashMap<String, Map<String, String>>();
    private Map<String, String> locations = new HashMap<String, String>();

    // private Map<String, TreeMap<String, HashMap<String, Integer[]>>> info = new HashMap<String, TreeMap<String, HashMap<String, Integer[]>>>();
    private Map<String, String> info = new HashMap<String, String>();
    private Map<String, String> urlToName = new HashMap<String, String>();
    private int misses = 0;
    private int hits = 0;

    public String getLocationsByCountry(String country) {
        if (!locations.containsKey(country)) { misses += 1; return null; }
        hits += 1;
        return locations.get(country);
    }

    public void saveLocationsByCountry(String country, String response) {
        locations.put(country, response);
    }

    public String getInfoByStation(String stationurl) {
        if (!info.containsKey(stationurl)) { misses += 1; return null; }
        hits += 1;
        return info.get(stationurl);
    }

    public String getStationNameByUrl(String stationurl) {
        if (!urlToName.containsKey(stationurl)) return null;
        return urlToName.get(stationurl);
    }

    public void saveInfoByStation(String stationurl, String data) {
        info.put(stationurl, data);
        // urlToName.put(stationurl, stationname);
    }

    public int getNoHits() { return hits; }
    public int getNoMisses() { return misses; }
    public int getNoRequests() { return hits+misses; }
}
