package ua.tqs.airquality;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private DataAccess serv;

    @Autowired
    private Cache cache;

    @GetMapping("/cache")
    public ResponseEntity<Map<String, Integer>> getCacheInfo() {
        Map<String, Integer> resp = new HashMap<String, Integer>();
        resp.put("hits", cache.getNoHits());
        resp.put("misses", cache.getNoMisses());
        resp.put("requests", cache.getNoRequests());
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/stations")
    public ResponseEntity<Map<Object, Object>> getStationsByCountry(@RequestParam(required=true) String country) {
        country = country.toLowerCase();
        /* {    country: "Finland",
                stations: [
                    { name: "Hannikaisenkatu, Jyväskylä, Finland",
                      uri: "finland/jyvaskyla/hannikaisenkatu" }
                ]
         */

        Map<String, String> loc = serv.getLocationsByCountry(country);
        if (loc == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        ArrayList<Map<String, String>> stationsList = new ArrayList<>();

        for (String stat : loc.keySet()) {
            stationsList.add(Stream.of(new String[][] {
                { "name" , stat },
                { "uri", loc.get(stat) }
            }).collect(Collectors.toMap(data -> data[0], data -> data[1])));
        }

        Map<Object, Object> ready = Stream.of(new Object[][] {
                { "country" , country },
                { "stations", stationsList }
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

        //if (loc == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "country not found");
        return new ResponseEntity<>(ready, HttpStatus.OK);
    }

    @GetMapping("/forecast")
    public ResponseEntity<Object> getInfoByStation(@RequestParam(required=true) String station, @RequestParam(required=false) String type) {
        // public ResponseEntity<TreeMap<String, HashMap<String, Integer[]>>> getInfoByStation(@RequestParam(required=true) String station, @RequestParam(required=false) String type) {

        TreeMap<String, HashMap<String, Integer[]>> info = serv.getInfoByStation(station);
        String loc = serv.getNameByUrl(station);

        if (loc==null || info == null) {
            System.err.println("is null!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String[] possible_types = new String[] {"o3", "pm10", "pm25", "uvi"};
        TreeMap<String, HashMap<String, Integer[]>> temp;

        if (type != null) {
            temp = new TreeMap<String, HashMap<String, Integer[]>>();
            type = type.toLowerCase();

            if (!Arrays.stream(possible_types).anyMatch(type::equals))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            for (String k : info.keySet()) {
                if (info.get(k).containsKey(type)) {
                    HashMap<String, Integer[]> temptemp = new HashMap<String, Integer[]>();
                    temptemp.put(type, info.get(k).get(type));
                    temp.put(k, temptemp);
                }
            }
            info = temp;
        }

                /*
        {
            stationname: "Escaldes Engordany, Andorra",
            stationuri: "andorra/fixa",
            forecast: [ { date : 11-01-2021,
                 values : [
                    {   type : "o3" ,
                        avg : 30 ,
                        min : 27 ,
                        max : 32
                    },
                    {   type : "pm25",
                        avg : .....
                    }
                ]
            ]
        }
         */

        Map<String, Object> ready = new TreeMap<>() {{
            put("stationname", loc);
            put("stationuri", station);
            put("forecast", new ArrayList<>());
        }};


        for (String date : info.keySet()) {
            ArrayList<Map<Object, Object>> infoPerDate = new ArrayList<>();
            ((List<Map<String, Object>>) ready.get("forecast")).add(new TreeMap<String, Object>() {{
                put("date", date);
                put("values", infoPerDate);
            }});
            for (String t : info.get(date).keySet()) {
                infoPerDate.add(Stream.of(new Object[][] {
                        { "type" , t },
                        { "avg", info.get(date).get(t)[0] },
                        { "min", info.get(date).get(t)[1] },
                        { "max", info.get(date).get(t)[2] }
                }).collect(Collectors.toMap(data -> data[0], data -> data[1])));
            }

        }


        return new ResponseEntity<>(ready, HttpStatus.OK);


    }
}
