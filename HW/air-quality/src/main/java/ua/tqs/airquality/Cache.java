package ua.tqs.airquality;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Cache {
    private Map<String, Map<String, String>> locations = new HashMap<String, Map<String, String>>();
    private Map<String, TreeMap<String, HashMap<String, Integer[]>>> info = new HashMap<String, TreeMap<String, HashMap<String, Integer[]>>>();
    private Map<String, String> urlToName = new HashMap<String, String>();

    public Map<String, String> getLocationsByCountry(String country) {
        if (!locations.containsKey(country)) return null;
        return locations.get(country);
    }

    public void saveLocationsByCountry(String country, Map<String, String> loc) {
        locations.put(country, loc);
    }

    public TreeMap<String, HashMap<String, Integer[]>> getInfoByStation(String stationurl) {
        if (!info.containsKey(stationurl)) return null;
        else return info.get(stationurl);
    }

    public String getStationNameByUrl(String stationurl) {
        if (!urlToName.containsKey(stationurl)) return null;
        return urlToName.get(stationurl);
    }

    public void saveInfoByStation(String stationurl, String stationname, TreeMap<String, HashMap<String, Integer[]>> data) {
        info.put(stationurl, data);
        urlToName.put(stationurl, stationname);
    }
}
