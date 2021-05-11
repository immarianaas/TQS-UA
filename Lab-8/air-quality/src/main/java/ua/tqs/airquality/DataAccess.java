package ua.tqs.airquality;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DataAccess {

    private static final String ERRORSTATUS = "error";
    private static final String STATUSFIELD = "status";

    @Autowired
    private CallExterior callExterior;

    @Autowired
    private Cache cache;

    public Map<String, String> getLocationsByCountry(String country) {
        String inline = cache.getLocationsByCountry(country);
        if (inline == null) {
            inline = callExterior.getLocationsByCountry(country);
            if (inline == null) return null;
            cache.saveLocationsByCountry(country, inline);
        }

        return getLocationInfoFromString(inline);
    }

    public TreeMap<String, HashMap<String, Integer[]>> getInfoByStation(String stationurl) {
        String inline = cache.getInfoByStation(stationurl);
        if (inline == null) { 
            inline = callExterior.getInfoByStation(stationurl); 
            if (inline == null) return null;
            cache.saveInfoByStation(stationurl, inline);
        }
        return getStationInfoFromString(inline);
    }

    public String getNameByUrl(String stationurl) {
        String inline = cache.getInfoByStation(stationurl);
        if (inline == null) { 
            inline = callExterior.getInfoByStation(stationurl); 
            if (inline == null) return null;
            cache.saveInfoByStation(stationurl, inline);
        }
        return getNameFromString(inline);
    }



    /* --- helper --- */

    public static Map<String, String> getLocationInfoFromString(String inline) {
        Map<String, String> locations = new TreeMap<>();

        JSONParser parse = new JSONParser();
        JSONObject dataObj;
        try {
            dataObj = (JSONObject) parse.parse(inline);
        } catch (ParseException e) { return null; }

        if (dataObj.containsKey(STATUSFIELD) && dataObj.get(STATUSFIELD).toString().equals(ERRORSTATUS))
            return null;

        JSONArray obj = (JSONArray) dataObj.get("data");


        for (int i = 0; i<obj.size(); i++) {
            JSONObject entry = (JSONObject) ((JSONObject) obj.get(i)).get("station");
            locations.put(entry.get("name").toString(), entry.get("url").toString());
        }
        
        return locations;
    }


    public static TreeMap<String, HashMap<String, Integer[]>> getStationInfoFromString(String inline) {
        TreeMap<String, HashMap<String, Integer[]>> valuesPerDate = new TreeMap<>();


        JSONParser parse = new JSONParser();
        JSONObject data0 = null;
        try {
            data0 = (JSONObject) parse.parse(inline);
        } catch (ParseException e) {
            return null;
        }

        if (data0.containsKey(STATUSFIELD) && data0.get(STATUSFIELD).toString().equals(ERRORSTATUS))
            return null;

        JSONObject data1 = (JSONObject) data0.get("data");
        JSONObject dataObj = (JSONObject) ((JSONObject) data1.get("forecast")).get("daily");

        for (Object typeobj : dataObj.keySet()) {
            String type = typeobj.toString();
            JSONArray data = (JSONArray) dataObj.get(type);
            for (int j = 0; j<data.size(); j++) {

                JSONObject info = (JSONObject) data.get(j);
                String date = info.get("day").toString();
                Integer avg = Integer.parseInt(info.get("avg").toString());
                Integer min = Integer.parseInt(info.get("min").toString());
                Integer max = Integer.parseInt(info.get("max").toString());
                Integer[] numbers = new Integer[] {avg, min, max};

                valuesPerDate.computeIfAbsent(date, k -> new HashMap<>());
                HashMap<String, Integer[]> datahm = valuesPerDate.get(date);
                
                datahm.put(type, numbers);
            }
        }
        return valuesPerDate;
    }


    public static String getNameFromString(String inline) {
        JSONParser parse = new JSONParser();
        JSONObject data0 = null;
        try {
            data0 = (JSONObject) parse.parse(inline);
        } catch (ParseException e) {
            return null;
        }

        if (data0.containsKey(STATUSFIELD) && data0.get(STATUSFIELD).toString().equals(ERRORSTATUS))
            return null;

        JSONObject data1 = (JSONObject) data0.get("data");
        Object cityobj = ((JSONObject) data1.get("city")).get("name");
        return cityobj.toString();
    }


}
