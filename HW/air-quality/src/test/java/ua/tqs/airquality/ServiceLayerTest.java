package ua.tqs.airquality;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;


import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceLayerTest {
    @Mock
    CallExterior ext;

    @Mock
    Cache cache;

    @InjectMocks
    DataAccess serv;

    @Test
    void whenAllGood_Locations() {
        String json = "{\"status\":\"ok\",\"data\":[{\"uid\":8411,\"aqi\":\"26\",\"time\":{\"tz\":\"+02:00\",\"stime\":\"2021-05-01 15:00:00\",\"vtime\":1619874000},\"station\":{\"name\":\"Escaldes Engordany, Andorra\",\"geo\":[42.509694,1.539138],\"url\":\"andorra/fixa\"}}]}";

        Map<String, String> expected_res = new TreeMap<String, String>();
        expected_res.put("Escaldes Engordany, Andorra", "andorra/fixa");

        when ( ext.getLocationsByCountry( contains("andorra")) ).thenReturn( json );
        when ( cache.getLocationsByCountry(anyString())).thenReturn(null);

        Map<String, String> result = serv.getLocationsByCountry("andorra");
        assertEquals(expected_res, result);
    }

    @Test
    void whenAllGood_CacheLocations() {
        String json = "{\"status\":\"ok\",\"data\":[{\"uid\":8411,\"aqi\":\"26\",\"time\":{\"tz\":\"+02:00\",\"stime\":\"2021-05-01 15:00:00\",\"vtime\":1619874000},\"station\":{\"name\":\"Escaldes Engordany, Andorra\",\"geo\":[42.509694,1.539138],\"url\":\"andorra/fixa\"}}]}";

        Map<String, String> expected_res = new TreeMap<String, String>();
        expected_res.put("Escaldes Engordany, Andorra", "andorra/fixa");

        // the CallExterior service shouldn't even be needed!
        // when ( ext.getLocationsByCountry( contains("andorra")) ).thenReturn(null);
        when ( cache.getLocationsByCountry(contains("andorra"))).thenReturn(json);

        Map<String, String> result = serv.getLocationsByCountry("andorra");
        assertEquals(expected_res, result);
    }

    @Test
    void whenAllGood_StationInfo() {
        String info_json = "{\"status\":\"ok\",\"data\":{\"aqi\":33,\"idx\":8411,\"attributions\":[{\"url\":\"http://www.mediambient.ad/\",\"name\":\"Departament de Medi Ambient d'Andorra - Qualitat de l'Aire a Andorra\",\"logo\":\"govern-d-andorra.jpg\"},{\"url\":\"https://waqi.info/\",\"name\":\"World Air Quality Index Project\"}],\"city\":{\"geo\":[42.509694,1.539138],\"name\":\"Escaldes Engordany, Andorra\",\"url\":\"https://aqicn.org/city/andorra/fixa\"},\"dominentpol\":\"o3\",\"iaqi\":{\"h\":{\"v\":50.5},\"no2\":{\"v\":5.5},\"o3\":{\"v\":32.5},\"p\":{\"v\":1010.5},\"pm10\":{\"v\":8},\"pm25\":{\"v\":25},\"so2\":{\"v\":0.6},\"t\":{\"v\":10.5},\"w\":{\"v\":2.5},\"wg\":{\"v\":7.5}},\"time\":{\"s\":\"2021-05-01 17:00:00\",\"tz\":\"+02:00\",\"v\":1619888400,\"iso\":\"2021-05-01T17:00:00+02:00\"},\"forecast\":{\"daily\":{\"o3\":[{\"avg\":31,\"day\":\"2021-04-29\",\"max\":33,\"min\":28}],\"pm10\":[{\"avg\":4,\"day\":\"2021-04-29\",\"max\":6,\"min\":4}],\"pm25\":[{\"avg\":14,\"day\":\"2021-04-29\",\"max\":23,\"min\":12}],\"uvi\":[{\"avg\":0,\"day\":\"2021-04-29\",\"max\":3,\"min\":0}]}},\"debug\":{\"sync\":\"2021-05-02T00:26:33+09:00\"}}}";

        TreeMap<String, HashMap<String, Integer[]>> expected = new TreeMap<String, HashMap<String, Integer[]>>();
        expected.put("2021-04-29", new HashMap<String, Integer[]>() {{
            put("o3", new Integer[] {31, 28, 33});
            put("pm10", new Integer[] {4, 4, 6});
            put("pm25", new Integer[] {14, 12, 23});
            put("uvi", new Integer[] {0, 0, 3});
        }});

        when ( ext.getInfoByStation( contains("andorra/fixa")) ).thenReturn( info_json );
        when ( cache.getInfoByStation(anyString())).thenReturn(null);

        TreeMap<String, HashMap<String, Integer[]>> res = serv.getInfoByStation("andorra/fixa");

        assertThat(res.keySet(), equalTo(expected.keySet()));
        for (String k : expected.keySet()) {
            assertThat(res.containsKey(k), is(true));
            for (String type : expected.get(k).keySet()) {
                assertThat(res.get(k).containsKey(type), is(true));
                assertThat(res.get(k).get(type), equalTo(expected.get(k).get(type)));
            }
        }

        verify(ext, times(1)).getInfoByStation("andorra/fixa");
        verify(cache, times(1)).getInfoByStation("andorra/fixa");
    }

    @Test
    void whenBadReq_Locations() {
        String badloc = "bad-location";
        when ( ext.getLocationsByCountry( contains(badloc)) ).thenReturn("{\"status\":\"error\",\"data\":\"Unknown station\"}");
        when ( cache.getLocationsByCountry( contains(badloc)) ).thenReturn(null);
        
        assertNull(serv.getLocationsByCountry(badloc));
    }
    
}
