package ua.tqs.airquality;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    // dá para ir buscar dados à localizacao mais proxima do utilizador, pode ser interessasnte!!
    // https://aqicn.org/json-api/doc/

    private final String TOKEN = "00f39d0202548c6b433775ef228bc9588b58ff28";

    @GetMapping("/")
    public String home(Model model) {//throws MalformedURLException, IOException, ParseException {
        model.addAttribute("title", "Air Quality Forecast!");
        Map<String, String> loc = getLocationsByCountry("finland");
        // System.out.println(loc);

        model.addAttribute("locations", loc );
        // o simbolo da "/" supostamente corresponde a %2F em URL encoded
        return "index";     
    }

    @GetMapping("/info")
    public String infoByCity(@RequestParam(required=true) String city, Model model) {
        model.addAttribute("title", "yay!!");
        TreeMap<String, HashMap<String, Integer[]>> info = new TreeMap<String, HashMap<String, Integer[]>>();
        String loc = getInfoByStation(city, info);

        model.addAttribute("info", info);
        model.addAttribute("loc", loc);
         return "index::showinfo";
        //return "index";
    }

    
    private Map<String, String> getLocationsByCountry(String country) {
        String url_ = "http://api.waqi.info/search/?keyword="+ country +"&token=" + TOKEN;
        URL url;
        HashMap<String, String> locations = new HashMap<String, String>();

        try {
            url = new URL(url_);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
                    // get the resp. code
            int respcode = conn.getResponseCode();


            if (respcode != 200) {
                throw new RuntimeException("HELPPPP");
            }
            Scanner sc = new Scanner(url.openStream());
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            String inline = sb.toString();
            sc.close();

            
            JSONParser parse = new JSONParser();
            JSONObject data_obj = (JSONObject) parse.parse(inline);

            JSONArray obj = (JSONArray) data_obj.get("data");

            //System.out.println(((JSONObject) obj.get(0)).toJSONString());

            for (int i = 0; i<obj.size(); i++) {
                JSONObject entry = (JSONObject) ((JSONObject) obj.get(i)).get("station");
                locations.put(entry.get("name").toString(), encodeURLSlash(entry.get("url").toString()));
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // System.out.println(locations.toString());
        return locations;

    }
    
    private String encodeURLSlash(String url) {
        return url.replace("/", "%2F");
    }

    // http://api.waqi.info/search/?keyword=portugal&token=00f39d0202548c6b433775ef228bc9588b58ff28

    private String getInfoByStation(String stationurl, TreeMap<String, HashMap<String, Integer[]>> valuesPerDate) {
        String url_ = "http://api.waqi.info/feed/"+ stationurl +"/?token=" + TOKEN;
        URL url;
        // valuesPerDate = new TreeMap<String, HashMap<String, Integer[]>>();
        String cityret = null;
        try {
            url = new URL(url_);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
                    // get the resp. code
            int respcode = conn.getResponseCode();


            if (respcode != 200) {
                throw new RuntimeException("HELPPPP");
            }
            Scanner sc = new Scanner(url.openStream());
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            String inline = sb.toString();
            sc.close();

            
            JSONParser parse = new JSONParser();
            JSONObject data1 = (JSONObject) ((JSONObject) parse.parse(inline)).get("data");
            Object cityobj = ((JSONObject) data1.get("city")).get("name");
            JSONObject data_obj = (JSONObject) ((JSONObject) data1.get("forecast")).get("daily");
            // System.out.println(data_obj.keySet());

            cityret = cityobj.toString();

            for (Object typeobj : data_obj.keySet()) {
                String type = typeobj.toString();
                JSONArray data = (JSONArray) data_obj.get(type);
                for (int j = 0; j<data.size(); j++) {

                    JSONObject info = (JSONObject) data.get(j);
                    String date = info.get("day").toString();
                    Integer avg = Integer.parseInt(info.get("avg").toString());
                    Integer min = Integer.parseInt(info.get("min").toString());
                    Integer max = Integer.parseInt(info.get("max").toString());
                    Integer[] numbers = new Integer[] {avg, min, max};
                    if (!valuesPerDate.containsKey(date))
                        valuesPerDate.put(date, new HashMap<String, Integer[]>());
                    
                    HashMap<String, Integer[]> datahm = valuesPerDate.get(date);
                    
                    datahm.put(type, numbers);


                }
            }

            // System.out.println(valuesPerDate);

            /*
            JSONArray obj = (JSONArray) data_obj.get("data");

            //System.out.println(((JSONObject) obj.get(0)).toJSONString());

            for (int i = 0; i<obj.size(); i++) {
                JSONObject entry = (JSONObject) ((JSONObject) obj.get(i)).get("station");
                locations.put(entry.get("name").toString(), encodeURLSlash(entry.get("url").toString()));
            }
            */

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*
        System.out.println(locations.toString());
        return locations;
        */
        return cityret;
    }

    private enum Values {
        AVG, MIN, MAX
    }
}
