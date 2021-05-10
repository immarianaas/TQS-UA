package ua.tqs.airquality;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.springframework.stereotype.Service;

@Service
public class CallExterior {
    private static final String TOKEN = "00f39d0202548c6b433775ef228bc9588b58ff28";

    public String getLocationsByCountry(String country) {
        String urlstring = "http://api.waqi.info/search/?keyword="+ country +"&token=" + TOKEN;
        return getData(urlstring);
    }

    public String getInfoByStation(String stationurl) {
        String urlstring = "http://api.waqi.info/feed/"+ stationurl +"/?token=" + TOKEN;
        return getData(urlstring);
    }


    private String getData(String urlstring) {
        URL url;

        int respcode = -1;
        try {
            url = new URL(urlstring);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            // get the resp. code
            respcode = conn.getResponseCode();

        } catch (Exception e) {
            return null;
        }

        if (respcode != 200) return null;

        try (Scanner sc = new Scanner(url.openStream())) {
            StringBuilder sb = new StringBuilder();
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }



}
