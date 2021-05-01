package ua.tqs.airquality;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.springframework.stereotype.Service;

@Service
public class CallExterior {
    private final String TOKEN = "00f39d0202548c6b433775ef228bc9588b58ff28";

    public String getLocationsByCountry(String country) {
        String url_ = "http://api.waqi.info/search/?keyword="+ country +"&token=" + TOKEN;
        URL url;
        String inline = null;
        try {
            url = new URL(url_);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
                    // get the resp. code
            int respcode = conn.getResponseCode();

            if (respcode != 200) {
                throw new RuntimeException("Response code not OK.");
            }
            Scanner sc = new Scanner(url.openStream());
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            inline = sb.toString();
            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return inline;

    }

    public String getInfoByStation(String stationurl) {
        String url_ = "http://api.waqi.info/feed/"+ stationurl +"/?token=" + TOKEN;
        URL url;
        // valuesPerDate = new TreeMap<String, HashMap<String, Integer[]>>();
        String inline = null;
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
            inline = sb.toString();
            sc.close();

        } catch (Exception e) {
            return null;
        }

        
        return inline;
    }


}
