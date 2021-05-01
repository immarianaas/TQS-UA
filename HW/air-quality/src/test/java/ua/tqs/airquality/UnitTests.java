package ua.tqs.airquality;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UnitTests {

    Cache cache = new Cache();
    
    @Test
    public void testCache_saveLocationsByCountry() {
        Map<String, String> loc = new HashMap<String, String>();
        loc.put("Aveiro", "Aveiro/Aveiro/Portugal");
        loc.put("Porto", "Porto/Portugal");
        cache.saveLocationsByCountry("portugal", loc);

        Map<String, String> ret = cache.getLocationsByCountry("portugal");
        assertThat(ret.size(), is(2));
        assertTrue(ret.containsKey("Aveiro"));
        assertTrue(ret.containsKey("Porto"));
        assertThat(ret.get("Aveiro"), equalTo("Aveiro/Aveiro/Portugal"));
    }

    @Test
    public void testCache_saveInfoByStation() {
        String stationname = "Aveiro";
        String stationurl = "Aveiro/Aveiro/Portugal";
        TreeMap<String, HashMap<String, Integer[]>> data = new TreeMap<String, HashMap<String, Integer[]>>();
        HashMap<String, Integer[]> temp = new HashMap<String, Integer[]>();
        // avg, min, max
        temp.put("uvi", new Integer[] {3, 2, 4});
        temp.put("pm10", new Integer[] {4, 3, 4});
        data.put("29-04-2021", temp);
        cache.saveInfoByStation(stationurl, stationname, data);


        TreeMap<String, HashMap<String, Integer[]>> ret = cache.getInfoByStation(stationurl);
        String ret_name = cache.getStationNameByUrl(stationurl);
        assertThat(ret_name, equalTo(stationname));
        assertThat(ret.size(), is(1));
        assertTrue(ret.containsKey("29-04-2021"));
        HashMap<String, Integer[]> temp_teste = ret.get("29-04-2021");
        assertThat(temp_teste.size(), is(2));
        assertTrue(temp_teste.containsKey("uvi"));
        assertTrue(temp_teste.containsKey("pm10"));
        assertThat(temp_teste.get("uvi")[0], is(3));

    }

}
