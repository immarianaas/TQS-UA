package ua.tqs.airquality;

import java.util.*;
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
        Map<String, Integer> resp = new HashMap<>();
        resp.put("hits", cache.getNoHits());
        resp.put("misses", cache.getNoMisses());
        resp.put("requests", cache.getNoRequests());
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/stations")
    public ResponseEntity<Map<Object, Object>> getStationsByCountry(@RequestParam(required=true) String country) {
        String countr = country.toLowerCase();
        /*
         * {    country: "Finland",
         *       stations: [
         *           { name: "Hannikaisenkatu, Jyväskylä, Finland",
         *             uri: "finland/jyvaskyla/hannikaisenkatu" }
         *       ]
         * }
         */

        Map<String, String> loc = serv.getLocationsByCountry(countr);
        if (loc == null) return new ResponseEntity<>(HttpStatus.FAILED_DEPENDENCY);

        ArrayList<Map<String, String>> stationsList = new ArrayList<>();

        for (Map.Entry<String, String> entry: loc.entrySet()) {
            stationsList.add(Stream.of(new String[][] {
                { "name" , entry.getKey() },
                { "uri", entry.getValue() }
            }).collect(Collectors.toMap(data -> data[0], data -> data[1])));
        }

        Map<Object, Object> ready = Stream.of(new Object[][] {
                { "country" , countr },
                { "stations", stationsList }
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        return new ResponseEntity<>(ready, HttpStatus.OK);
    }

    @GetMapping("/forecast")
    public ResponseEntity<Object> getInfoByStation(@RequestParam(required=true) String station, @RequestParam(required=false) String type) {
        TreeMap<String, HashMap<String, Integer[]>> info = serv.getInfoByStation(station);
        String loc = serv.getNameByUrl(station);

        if (loc==null || info == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String[] possibleTypes = new String[] {"o3", "pm10", "pm25", "uvi"};
        TreeMap<String, HashMap<String, Integer[]>> temp;

        if (type != null) {
            temp = new TreeMap<>();
            type = type.toLowerCase();

            if (Arrays.stream(possibleTypes).noneMatch(type::equals))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            for (Map.Entry<String, HashMap<String, Integer[]>> entry : info.entrySet()) {
                if (entry.getValue().containsKey(type)) {
                    HashMap<String, Integer[]> temptemp = new HashMap<>();
                    temptemp.put(type, entry.getValue().get(type));
                    temp.put(entry.getKey(), temptemp);
                }
            }
            info = temp;
        }

        /*
         *  {
         *      stationname: "Escaldes Engordany, Andorra",
         *      stationuri: "andorra/fixa",
         *      forecast: [ { date : 11-01-2021,
         *           values : [
         *              {   type : "o3" ,
         *                  avg : 30 ,
         *                  min : 27 ,
         *                  max : 32
         *              },
         *              {   type : "pm25",
         *                  avg : .....
         *              }
         *          ]
         *      ]
         *  }
         */

        Map<String, Object> ready = new TreeMap<>();

        ready.put("stationname", loc);
        ready.put("stationuri", station);

        ArrayList<TreeMap<String, Object>> l = new ArrayList<>();


        for (Map.Entry<String, HashMap<String, Integer[]>> entry : info.entrySet()) {
            ArrayList<Map<Object, Object>> infoPerDate = new ArrayList<>();

            TreeMap<String, Object> dataMap = new TreeMap<>();
            dataMap.put("date", entry.getKey());
            dataMap.put("values", infoPerDate);

            l.add(dataMap);

            for (Map.Entry<String, Integer[]> entry2 : entry.getValue().entrySet()) {
                infoPerDate.add(Stream.of(new Object[][] {
                        { "type" , entry2.getKey() },
                        { "avg", entry2.getValue()[0] },
                        { "min", entry2.getValue()[1] },
                        { "max", entry2.getValue()[2] }
                }).collect(Collectors.toMap(data -> data[0], data -> data[1])));
            }

        }
        ready.put("forecast", l);
        return new ResponseEntity<>(ready, HttpStatus.OK);
    }
}
