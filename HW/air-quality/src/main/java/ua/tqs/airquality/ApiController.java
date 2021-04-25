package ua.tqs.airquality;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @Autowired
    private CallExterior serv;


    @GetMapping("/stations")
    public ResponseEntity<Map<String, String>> getStationsByCountry(@RequestParam(required=true) String country) {
        Map<String, String> loc = serv.getLocationsByCountry(country);
        //if (loc == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "country not found");
        if (loc == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(loc, HttpStatus.OK);
    }

    @GetMapping("/forecast")
    public ResponseEntity<TreeMap<String, HashMap<String, Integer[]>>> getInfoByStation(@RequestParam(required=true) String station, @RequestParam(required=false) String type, @RequestParam(required=false) String date) {
        TreeMap<String, HashMap<String, Integer[]>> info = new TreeMap<String, HashMap<String, Integer[]>>();
        String loc = serv.getInfoByStation(station, info);

        if (type != null) {;}
        if (date != null) {;}
        if (loc==null) {
            System.err.println("is null!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(info, HttpStatus.OK);


    }
}
