package ua.tqs.airquality;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {
    private static final Logger LOGGER = LogManager.getLogger();


    @Autowired
    private DataAccess serv;

    @GetMapping("/")
    public String home(Model model) {//throws MalformedURLException, IOException, ParseException {
        LOGGER.info("This is working.");
        model.addAttribute("title", "Air Quality Forecast!");
        Map<String, String> loc = serv.getLocationsByCountry("finland");
        // System.out.println(loc);

        model.addAttribute("locations", loc );
        // o simbolo da "/" supostamente corresponde a %2F em URL encoded
        return "index";     
    }

    @GetMapping("/info")
    public String infoByCity(@RequestParam(required=true) String city, Model model) {
        model.addAttribute("title", "yay!!");
        TreeMap<String, HashMap<String, Integer[]>> info = serv.getInfoByStation(city);
        String loc = serv.getNameByUrl(city);

        model.addAttribute("info", info);
        model.addAttribute("loc", loc);
         return "index::showinfo";
        //return "index";
    }

    
    /* // afinal acho que não é preciso!
    private String encodeURLSlash(String url) {
        return url.replace("/", "%2F");
    }
    */

    // http://api.waqi.info/search/?keyword=portugal&token=00f39d0202548c6b433775ef228bc9588b58ff28


}
