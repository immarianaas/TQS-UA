package ua.tqs.airquality;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class Cache {
    private Map<String, String> locations = new HashMap<>();

    private Map<String, String> info = new HashMap<>();

    private int misses = 0;
    private int hits = 0;

    public static final long TTL = 5;

    private Map<String, Instant> requestTime = new HashMap<>();

    private static final String LOCATIONS_CODE = "locations+";
    private static final String INFO_CODE = "info+";

    /* --- get info --- */
    public String getLocationsByCountry(String country) {
        // if not in map then returns null
        if (!locations.containsKey(country)) { misses += 1; return null; }

        // check if within TTL
        if (hasTimedOut(requestTime.get(LOCATIONS_CODE+country))) {
            misses += 1;
            locations.remove(country);
            requestTime.remove(LOCATIONS_CODE+country);
            return null;
        }

        // if all good
        hits += 1;
        return locations.get(country);
    }

    public String getInfoByStation(String stationurl) {
        if (!info.containsKey(stationurl)) { misses += 1; return null; }

        // check if within TTL
        if (hasTimedOut(requestTime.get(INFO_CODE+stationurl))) {
            misses += 1;
            locations.remove(stationurl);
            requestTime.remove(INFO_CODE+stationurl);
            return null;
        }

        hits += 1;
        return info.get(stationurl);
    }


    public int getNoHits() { return hits; }
    public int getNoMisses() { return misses; }
    public int getNoRequests() { return hits+misses; }


    /* --- save --- */
    public void saveLocationsByCountry(String country, String response) {
        locations.put(country.toLowerCase(), response);
        String key = LOCATIONS_CODE + country;
        requestTime.put(key, Instant.now());
    }

    public void saveInfoByStation(String stationurl, String data) {
        info.put(stationurl.toLowerCase(), data);
        String key = INFO_CODE + stationurl;
        requestTime.put(key, Instant.now());
    }


    /* --- helper --- */
    private boolean hasTimedOut(Instant reqTime) {
        // true if seconds elapsed >= TTL
        Instant now = Instant.now();
        long elapsed = Duration.between(reqTime, now).toSeconds();
        return elapsed >= TTL;
    }
}
