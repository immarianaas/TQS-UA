package ua.tqs.airquality;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;


class UnitTests {
    
    Cache cache;

    @BeforeEach
    void setup() {
        cache = new Cache();
    }


    @Test
    void failTest() {
        failTest();
    }

    /* --- Cache --- */
    @Test
    void testCache_saveLocationsByCountry() {
        String andorra = "{\"status\":\"ok\",\"data\":[{\"uid\":8411,\"aqi\":\"26\",\"time\":{\"tz\":\"+02:00\",\"stime\":\"2021-05-01 15:00:00\",\"vtime\":1619874000},\"station\":{\"name\":\"Escaldes Engordany, Andorra\",\"geo\":[42.509694,1.539138],\"url\":\"andorra/fixa\"}}]}";
        cache.saveLocationsByCountry("andorra", andorra);

        String ret = cache.getLocationsByCountry("andorra");
        assertThat(ret, equalTo(andorra));
        assertThat(cache.getNoMisses(), equalTo(0));
        assertThat(cache.getNoHits(), equalTo(1));
        assertThat(cache.getNoRequests(), equalTo(1));
    }

    @Test
    void testCache_saveInfoByStation() {
        String toSave = "{\"status\":\"ok\",\"data\":{\"aqi\":33,\"idx\":8411,\"attributions\":[{\"url\":\"http://www.mediambient.ad/\",\"name\":\"Departament de Medi Ambient d'Andorra - Qualitat de l'Aire a Andorra\",\"logo\":\"govern-d-andorra.jpg\"},{\"url\":\"https://waqi.info/\",\"name\":\"World Air Quality Index Project\"}],\"city\":{\"geo\":[42.509694,1.539138],\"name\":\"Escaldes Engordany, Andorra\",\"url\":\"https://aqicn.org/city/andorra/fixa\"},\"dominentpol\":\"o3\",\"iaqi\":{\"h\":{\"v\":50.5},\"no2\":{\"v\":5.5},\"o3\":{\"v\":32.5},\"p\":{\"v\":1010.5},\"pm10\":{\"v\":8},\"pm25\":{\"v\":25},\"so2\":{\"v\":0.6},\"t\":{\"v\":10.5},\"w\":{\"v\":2.5},\"wg\":{\"v\":7.5}},\"time\":{\"s\":\"2021-05-01 17:00:00\",\"tz\":\"+02:00\",\"v\":1619888400,\"iso\":\"2021-05-01T17:00:00+02:00\"},\"forecast\":{\"daily\":{\"o3\":[{\"avg\":31,\"day\":\"2021-04-29\",\"max\":33,\"min\":28},{\"avg\":30,\"day\":\"2021-04-30\",\"max\":34,\"min\":29},{\"avg\":30,\"day\":\"2021-05-01\",\"max\":37,\"min\":25},{\"avg\":34,\"day\":\"2021-05-02\",\"max\":38,\"min\":31},{\"avg\":33,\"day\":\"2021-05-03\",\"max\":36,\"min\":31},{\"avg\":33,\"day\":\"2021-05-04\",\"max\":36,\"min\":31},{\"avg\":31,\"day\":\"2021-05-05\",\"max\":31,\"min\":31}],\"pm10\":[{\"avg\":4,\"day\":\"2021-04-29\",\"max\":6,\"min\":4},{\"avg\":5,\"day\":\"2021-04-30\",\"max\":6,\"min\":4},{\"avg\":3,\"day\":\"2021-05-01\",\"max\":6,\"min\":1},{\"avg\":5,\"day\":\"2021-05-02\",\"max\":7,\"min\":4},{\"avg\":4,\"day\":\"2021-05-03\",\"max\":5,\"min\":4},{\"avg\":4,\"day\":\"2021-05-04\",\"max\":5,\"min\":4},{\"avg\":5,\"day\":\"2021-05-05\",\"max\":5,\"min\":5}],\"pm25\":[{\"avg\":14,\"day\":\"2021-04-29\",\"max\":23,\"min\":12},{\"avg\":15,\"day\":\"2021-04-30\",\"max\":18,\"min\":12},{\"avg\":9,\"day\":\"2021-05-01\",\"max\":20,\"min\":4},{\"avg\":18,\"day\":\"2021-05-02\",\"max\":26,\"min\":15},{\"avg\":17,\"day\":\"2021-05-03\",\"max\":18,\"min\":15},{\"avg\":15,\"day\":\"2021-05-04\",\"max\":18,\"min\":13},{\"avg\":18,\"day\":\"2021-05-05\",\"max\":19,\"min\":17}],\"uvi\":[{\"avg\":0,\"day\":\"2021-04-29\",\"max\":3,\"min\":0},{\"avg\":0,\"day\":\"2021-04-30\",\"max\":3,\"min\":0},{\"avg\":0,\"day\":\"2021-05-01\",\"max\":4,\"min\":0},{\"avg\":1,\"day\":\"2021-05-02\",\"max\":3,\"min\":0},{\"avg\":1,\"day\":\"2021-05-03\",\"max\":5,\"min\":0},{\"avg\":1,\"day\":\"2021-05-04\",\"max\":6,\"min\":0},{\"avg\":1,\"day\":\"2021-05-05\",\"max\":8,\"min\":0},{\"avg\":0,\"day\":\"2021-05-06\",\"max\":0,\"min\":0}]}},\"debug\":{\"sync\":\"2021-05-02T00:26:33+09:00\"}}}";
        String stationurl = "andorra/fixa";
        cache.saveInfoByStation(stationurl, toSave);
        String ret = cache.getInfoByStation(stationurl);

        assertThat(ret, equalTo(toSave));
        assertThat(cache.getNoMisses(), equalTo(0));
        assertThat(cache.getNoHits(), equalTo(1));
        assertThat(cache.getNoRequests(), equalTo(1));
    }

    @Test
    void testCache_getLocationsNotAvailable() {
        String resp = cache.getLocationsByCountry("andorra");

        assertThat(resp, nullValue());
        assertThat(cache.getNoMisses(), equalTo(1));
        assertThat(cache.getNoHits(), equalTo(0));
        assertThat(cache.getNoRequests(), equalTo(1));
    }

    @Test
    void testCache_getInfoNotAvailable() {
        String resp = cache.getInfoByStation("andorra/fixa");

        assertThat(resp, nullValue());
        assertThat(cache.getNoMisses(), equalTo(1));
        assertThat(cache.getNoHits(), equalTo(0));
        assertThat(cache.getNoRequests(), equalTo(1));
    }

    @Test
    void testCache_testInfoTTL() {
        String toSave = "{\"status\":\"ok\",\"data\":{\"aqi\":33,\"idx\":8411,\"attributions\":[{\"url\":\"http://www.mediambient.ad/\",\"name\":\"Departament de Medi Ambient d'Andorra - Qualitat de l'Aire a Andorra\",\"logo\":\"govern-d-andorra.jpg\"},{\"url\":\"https://waqi.info/\",\"name\":\"World Air Quality Index Project\"}],\"city\":{\"geo\":[42.509694,1.539138],\"name\":\"Escaldes Engordany, Andorra\",\"url\":\"https://aqicn.org/city/andorra/fixa\"},\"dominentpol\":\"o3\",\"iaqi\":{\"h\":{\"v\":50.5},\"no2\":{\"v\":5.5},\"o3\":{\"v\":32.5},\"p\":{\"v\":1010.5},\"pm10\":{\"v\":8},\"pm25\":{\"v\":25},\"so2\":{\"v\":0.6},\"t\":{\"v\":10.5},\"w\":{\"v\":2.5},\"wg\":{\"v\":7.5}},\"time\":{\"s\":\"2021-05-01 17:00:00\",\"tz\":\"+02:00\",\"v\":1619888400,\"iso\":\"2021-05-01T17:00:00+02:00\"},\"forecast\":{\"daily\":{\"o3\":[{\"avg\":31,\"day\":\"2021-04-29\",\"max\":33,\"min\":28},{\"avg\":30,\"day\":\"2021-04-30\",\"max\":34,\"min\":29},{\"avg\":30,\"day\":\"2021-05-01\",\"max\":37,\"min\":25},{\"avg\":34,\"day\":\"2021-05-02\",\"max\":38,\"min\":31},{\"avg\":33,\"day\":\"2021-05-03\",\"max\":36,\"min\":31},{\"avg\":33,\"day\":\"2021-05-04\",\"max\":36,\"min\":31},{\"avg\":31,\"day\":\"2021-05-05\",\"max\":31,\"min\":31}],\"pm10\":[{\"avg\":4,\"day\":\"2021-04-29\",\"max\":6,\"min\":4},{\"avg\":5,\"day\":\"2021-04-30\",\"max\":6,\"min\":4},{\"avg\":3,\"day\":\"2021-05-01\",\"max\":6,\"min\":1},{\"avg\":5,\"day\":\"2021-05-02\",\"max\":7,\"min\":4},{\"avg\":4,\"day\":\"2021-05-03\",\"max\":5,\"min\":4},{\"avg\":4,\"day\":\"2021-05-04\",\"max\":5,\"min\":4},{\"avg\":5,\"day\":\"2021-05-05\",\"max\":5,\"min\":5}],\"pm25\":[{\"avg\":14,\"day\":\"2021-04-29\",\"max\":23,\"min\":12},{\"avg\":15,\"day\":\"2021-04-30\",\"max\":18,\"min\":12},{\"avg\":9,\"day\":\"2021-05-01\",\"max\":20,\"min\":4},{\"avg\":18,\"day\":\"2021-05-02\",\"max\":26,\"min\":15},{\"avg\":17,\"day\":\"2021-05-03\",\"max\":18,\"min\":15},{\"avg\":15,\"day\":\"2021-05-04\",\"max\":18,\"min\":13},{\"avg\":18,\"day\":\"2021-05-05\",\"max\":19,\"min\":17}],\"uvi\":[{\"avg\":0,\"day\":\"2021-04-29\",\"max\":3,\"min\":0},{\"avg\":0,\"day\":\"2021-04-30\",\"max\":3,\"min\":0},{\"avg\":0,\"day\":\"2021-05-01\",\"max\":4,\"min\":0},{\"avg\":1,\"day\":\"2021-05-02\",\"max\":3,\"min\":0},{\"avg\":1,\"day\":\"2021-05-03\",\"max\":5,\"min\":0},{\"avg\":1,\"day\":\"2021-05-04\",\"max\":6,\"min\":0},{\"avg\":1,\"day\":\"2021-05-05\",\"max\":8,\"min\":0},{\"avg\":0,\"day\":\"2021-05-06\",\"max\":0,\"min\":0}]}},\"debug\":{\"sync\":\"2021-05-02T00:26:33+09:00\"}}}";
        String stationurl = "andorra/fixa";
        cache.saveInfoByStation(stationurl, toSave);
        String ret = cache.getInfoByStation(stationurl);
        // double checking that before the TTL it returned
        assertThat(ret, notNullValue());

        // if the cache doesnt return null in Cache.TTL seconds, then it fails!
        Awaitility.await().atMost(Cache.TTL, TimeUnit.SECONDS).until(
                () -> cache.getInfoByStation(stationurl) == null
        );
    }


    /* --- DataAccess ~= Utils --- */
    @Test
    void testDataAccess_getLocationInfoFromString() {
        String andorra = "{\"status\":\"ok\",\"data\":[{\"uid\":8411,\"aqi\":\"26\",\"time\":{\"tz\":\"+02:00\",\"stime\":\"2021-05-01 15:00:00\",\"vtime\":1619874000},\"station\":{\"name\":\"Escaldes Engordany, Andorra\",\"geo\":[42.509694,1.539138],\"url\":\"andorra/fixa\"}}]}";
        Map<String, String> expected = new TreeMap<>();
        expected.put("Escaldes Engordany, Andorra", "andorra/fixa");

        Map<String, String> ret = DataAccess.getLocationInfoFromString(andorra);
        assertThat(ret, equalTo(expected));
    }

    @Test
    void testDataAccess_getStationInfoFromString() {
        String info_json = "{\"status\":\"ok\",\"data\":{\"aqi\":33,\"idx\":8411,\"attributions\":[{\"url\":\"http://www.mediambient.ad/\",\"name\":\"Departament de Medi Ambient d'Andorra - Qualitat de l'Aire a Andorra\",\"logo\":\"govern-d-andorra.jpg\"},{\"url\":\"https://waqi.info/\",\"name\":\"World Air Quality Index Project\"}],\"city\":{\"geo\":[42.509694,1.539138],\"name\":\"Escaldes Engordany, Andorra\",\"url\":\"https://aqicn.org/city/andorra/fixa\"},\"dominentpol\":\"o3\",\"iaqi\":{\"h\":{\"v\":50.5},\"no2\":{\"v\":5.5},\"o3\":{\"v\":32.5},\"p\":{\"v\":1010.5},\"pm10\":{\"v\":8},\"pm25\":{\"v\":25},\"so2\":{\"v\":0.6},\"t\":{\"v\":10.5},\"w\":{\"v\":2.5},\"wg\":{\"v\":7.5}},\"time\":{\"s\":\"2021-05-01 17:00:00\",\"tz\":\"+02:00\",\"v\":1619888400,\"iso\":\"2021-05-01T17:00:00+02:00\"},\"forecast\":{\"daily\":{\"o3\":[{\"avg\":31,\"day\":\"2021-04-29\",\"max\":33,\"min\":28}],\"pm10\":[{\"avg\":4,\"day\":\"2021-04-29\",\"max\":6,\"min\":4}],\"pm25\":[{\"avg\":14,\"day\":\"2021-04-29\",\"max\":23,\"min\":12}],\"uvi\":[{\"avg\":0,\"day\":\"2021-04-29\",\"max\":3,\"min\":0}]}},\"debug\":{\"sync\":\"2021-05-02T00:26:33+09:00\"}}}";

        TreeMap<String, HashMap<String, Integer[]>> expected = new TreeMap<String, HashMap<String, Integer[]>>();
        expected.put("2021-04-29", new HashMap<String, Integer[]>() {{
            put("o3", new Integer[] {31, 28, 33});
            put("pm10", new Integer[] {4, 4, 6});
            put("pm25", new Integer[] {14, 12, 23});
            put("uvi", new Integer[] {0, 0, 3});
        }});

        TreeMap<String, HashMap<String, Integer[]>> res = DataAccess.getStationInfoFromString(info_json);

        assertThat(res.keySet(), equalTo(expected.keySet()));
        for (String k : expected.keySet()) {
            assertThat(res.containsKey(k), is(true));
            for (String type : expected.get(k).keySet()) {
                assertThat(res.get(k).containsKey(type), is(true));
                assertThat(res.get(k).get(type), equalTo(expected.get(k).get(type)));
            }
        }
    }

    @Test
    void testDataAccess_getStationNameFromString() {
        String info_json = "{\"status\":\"ok\",\"data\":{\"aqi\":33,\"idx\":8411,\"attributions\":[{\"url\":\"http://www.mediambient.ad/\",\"name\":\"Departament de Medi Ambient d'Andorra - Qualitat de l'Aire a Andorra\",\"logo\":\"govern-d-andorra.jpg\"},{\"url\":\"https://waqi.info/\",\"name\":\"World Air Quality Index Project\"}],\"city\":{\"geo\":[42.509694,1.539138],\"name\":\"Escaldes Engordany, Andorra\",\"url\":\"https://aqicn.org/city/andorra/fixa\"},\"dominentpol\":\"o3\",\"iaqi\":{\"h\":{\"v\":50.5},\"no2\":{\"v\":5.5},\"o3\":{\"v\":32.5},\"p\":{\"v\":1010.5},\"pm10\":{\"v\":8},\"pm25\":{\"v\":25},\"so2\":{\"v\":0.6},\"t\":{\"v\":10.5},\"w\":{\"v\":2.5},\"wg\":{\"v\":7.5}},\"time\":{\"s\":\"2021-05-01 17:00:00\",\"tz\":\"+02:00\",\"v\":1619888400,\"iso\":\"2021-05-01T17:00:00+02:00\"},\"forecast\":{\"daily\":{\"o3\":[{\"avg\":31,\"day\":\"2021-04-29\",\"max\":33,\"min\":28}],\"pm10\":[{\"avg\":4,\"day\":\"2021-04-29\",\"max\":6,\"min\":4}],\"pm25\":[{\"avg\":14,\"day\":\"2021-04-29\",\"max\":23,\"min\":12}],\"uvi\":[{\"avg\":0,\"day\":\"2021-04-29\",\"max\":3,\"min\":0}]}},\"debug\":{\"sync\":\"2021-05-02T00:26:33+09:00\"}}}";
        String expected = "Escaldes Engordany, Andorra";

        String got = DataAccess.getNameFromString(info_json);
        assertThat(got, equalTo(expected));
    }

}
