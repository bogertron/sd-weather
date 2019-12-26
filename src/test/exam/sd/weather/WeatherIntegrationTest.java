package exam.sd.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import exam.sd.weather.bean.Location;
import exam.sd.weather.bean.Weather;
import exam.sd.weather.dao.jpa.WeatherRepository;
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
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for weather operations
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherIntegrationTest {

    private static final String dateFormat = "yyyy-MM-dd";
    private static final SimpleDateFormat dataFormatter = new SimpleDateFormat(dateFormat);

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
        w.setDate(dataFormatter.parse("2020-01-01"));
        BigDecimal[] temp = new BigDecimal[24];

        for (int idx = 0; idx < temp.length; idx++) {
            temp[idx] = new BigDecimal(idx);
        }
        w.setTemperature(temp);
        Location l = new Location();
        l.setCity("Parts Unknown");
        l.setState("NV");
        l.setLat(new BigDecimal(12));
        l.setLon(new BigDecimal(21));
        w.setLocation(l);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(w);

        mvc.perform(post("/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());

        List<Weather> finalRepo = weatherRepository.findAll();

        assertEquals("Weather object not saved", 1, finalRepo.size());

        Weather savedWeather = finalRepo.get(0);

        assertEquals("Ids do not match", savedWeather.getId(), w.getId());

    }
}
