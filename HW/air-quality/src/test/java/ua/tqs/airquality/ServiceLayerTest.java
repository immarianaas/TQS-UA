package ua.tqs.airquality;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServiceLayerTest {
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
    void whenBadReq_Locations() {
        String badloc = "bad-location";
        when ( ext.getLocationsByCountry( contains(badloc)) ).thenReturn(null);
        when ( cache.getLocationsByCountry( contains(badloc)) ).thenReturn(null);
        
        assertNull(serv.getLocationsByCountry(badloc));
    }
    
}