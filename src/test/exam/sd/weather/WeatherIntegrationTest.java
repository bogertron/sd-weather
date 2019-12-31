package exam.sd.weather;

import exam.sd.weather.bean.Location;
import exam.sd.weather.bean.Weather;
import exam.sd.weather.dao.jpa.WeatherRepository;
import exam.sd.weather.util.JacksonSerializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for weather operations
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WeatherRepository weatherRepository;

    @Before
    public void setUp() {
        weatherRepository.deleteAll();
    }

    @Test
    public void creationSucceeds() throws Exception {
        Weather w = new Weather();
        w.setId(1234L);
        w.setDate(LocalDate.of(2020,1, 1));
        List<BigDecimal> temp = new LinkedList<>();

        for (int idx = 0; idx < Weather.TEMPERATURE_COUNT; idx++) {
            temp.add(new BigDecimal(idx));
        }
        w.setTemperature(temp);
        Location l = new Location();
        l.setCity("Parts Unknown");
        l.setState("NV");
        l.setLat(new BigDecimal(12));
        l.setLon(new BigDecimal(21));
        w.setLocation(l);

        String json = JacksonSerializer.serializeWithDate(w);

        mvc.perform(post("/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        List<Weather> finalRepo = weatherRepository.findAll();

        assertEquals("Weather object not saved", 1, finalRepo.size());

        Weather savedWeather = finalRepo.get(0);

        assertEquals("Ids do not match", savedWeather.getId(), w.getId());

    }

    @Test
    public void testDeleteRange() throws Exception {
        Weather oneToKeep = new Weather();
        oneToKeep.setId(1234L);
        oneToKeep.setDate(LocalDate.of(2020,1, 1));
        List<BigDecimal> temp = new LinkedList<>();

        for (int idx = 0; idx < 24; idx++) {
            temp.add(new BigDecimal(idx));
        }
        oneToKeep.setTemperature(temp);
        Location l = new Location();
        l.setCity("Parts Unknown");
        l.setState("NV");
        l.setLat(new BigDecimal(12));
        l.setLon(new BigDecimal(21));
        oneToKeep.setLocation(l);

        String json = JacksonSerializer.serializeWithDate(oneToKeep);

        mvc.perform(post("/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        Weather oneToDelete = new Weather();
        oneToDelete.setId(4321L);
        oneToDelete.setDate(LocalDate.of(2020,1, 3));
        temp = new LinkedList<>();

        for (int idx = 0; idx < Weather.TEMPERATURE_COUNT; idx++) {
            temp.add(new BigDecimal(idx));
        }
        oneToDelete.setTemperature(temp);
        oneToDelete.setLocation(l);

        json = JacksonSerializer.serializeWithDate(oneToDelete);

        mvc.perform(post("/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        // test to verify that 2 unique weather objects are saved
        List<Weather> beforeDelete = weatherRepository.findAll();

        assertEquals("Incorrect number of weather objects in repository before deletion", 2, beforeDelete.size());

        String url = String.format("/erase?start=2020-01-03&end=2020-01-03&lat=%d&lon=%d",
                l.getLat().intValue(), l.getLon().intValue());
        mvc.perform(delete(url))
                .andExpect(status().isOk());

        List<Weather> afterDelete = weatherRepository.findAll();

        assertEquals("Incorrect number of weather objects in repository after deletion", 1, afterDelete.size());

        assertEquals("Wrong weather object deleted", oneToKeep.getId(), afterDelete.get(0).getId());
    }

}
