package exam.sd.weather;

import exam.sd.weather.bean.Location;
import exam.sd.weather.bean.Weather;
import exam.sd.weather.controller.WeatherController;
import exam.sd.weather.exception.DuplicateWeatherException;
import exam.sd.weather.service.WeatherService;
import org.apache.tomcat.jni.Local;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.doThrow;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sun.plugin2.util.PojoUtil.toJson;

@RunWith(SpringRunner.class)
@WebMvcTest(WeatherController.class)
@TestPropertySource(locations="classpath:resources/application.properties")
public class WeatherControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WeatherService service;

    /**
     * When there is no weather, instead of returning an empty list, it should return an
     * object with a message indicating that there are no weather points
     * @throws Exception
     */
    @Test
    public void getAllWeather_NoWeather() throws Exception {
        given(service.getAll()).willReturn(new ArrayList<>());

        mvc.perform(get("/weather")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    /**
     * Verify that the erase all command correctly returns a good status code
     * @throws Exception
     */
    @Test
    public void deleteWeather() throws Exception {

        mvc.perform(delete("/erase")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    public ResultActions deleteRange(String parameters) throws Exception {
        return mvc.perform(delete("/erase?" + parameters).
                contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void deleteRange_testAllParams() throws Exception {
        ResultActions ra = deleteRange("start=2019-01-01&end=2019-01-02&lat=11&lon=222")
                .andExpect(status().isOk());
    }

    @Test
    public void deleteRange_testNoLat() throws Exception {
        ResultActions ra = deleteRange("start=2019-01-01&end=2019-01-02&lat=11")
                .andExpect(status().isOk());
    }

    @Test
    public void deleteRange_testNoLon() throws Exception {
        ResultActions ra = deleteRange("start=2019-01-01&end=2019-01-02&lon=11")
                .andExpect(status().isOk());
    }

    @Test
    public void deleteRange_testNoStart() throws Exception {
        ResultActions ra = deleteRange("end=2019-01-02&lat=11&lon=222")
                .andExpect(status().isOk());
    }

    @Test
    public void deleteRange_testNoEnd() throws Exception {
        ResultActions ra = deleteRange("start=2019-01-02&lat=11&lon=222")
                .andExpect(status().isOk());
    }


    /**
     * Verify that the get all weather is well formatted when there is data present
     * @throws Exception
     */
    @Test
    public void getAllWeather_WithWeather() throws Exception {
        List<Weather> weathers = new ArrayList<Weather>();
        Weather w1 = new Weather();
        Location l1 = new Location();
        l1.setCity("City1");
        l1.setLat(4);
        l1.setLon(3);
        l1.setState("State1");
        w1.setDate(LocalDate.now());
        w1.setId(1);
        w1.setLocation(l1);
        w1.setTemperature(new float[]{5,5,5,5,5,5,5,5,5,5,5,5});
        weathers.add(w1);
        Weather w2 = new Weather();
        Location l2 = new Location();
        l2.setCity("City2");
        l2.setLat(4);
        l2.setLon(3);
        l2.setState("State2");
        w2.setDate(LocalDate.of(2020, 1, 1));
        w2.setId(2);
        w2.setLocation(l2);
        w2.setTemperature(new float[]{5,5,5,5,5,5,5,5,5,5,5,5});
        weathers.add(w2);

        given(service.getAll()).willReturn(weathers);

        mvc.perform(get("/weather")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }

    /**
     * Verify that the get all weather is well formatted when there is data present
     * @throws Exception
     */
    @Test
    public void createWeather_testDupe() throws Exception {
        Weather w1 = new Weather();
        Location l1 = new Location();
        l1.setCity("City1");
        l1.setLat(4);
        l1.setLon(3);
        l1.setState("State1");
        w1.setDate(LocalDate.of(2020, 1, 1));
        w1.setId(1);
        w1.setLocation(l1);
        w1.setTemperature(new float[]{5,5,5,5,5,5,5,5,5,5,5,5});

        doThrow(new DuplicateWeatherException()).when(service).createWeather(w1);

        mvc.perform(post("/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(w1)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Verify that the get all weather is well formatted when there is data present
     * @throws Exception
     */
    @Test
    public void createWeather_testSuccess() throws Exception {
        Weather w1 = new Weather();
        Location l1 = new Location();
        l1.setCity("City1");
        l1.setLat(4);
        l1.setLon(3);
        l1.setState("State1");
        w1.setDate(LocalDate.of(2020, 1, 1));
        w1.setId(1);
        w1.setLocation(l1);
        w1.setTemperature(new float[]{5,5,5,5,5,5,5,5,5,5,5,5});
        String json = toJson(w1);
        System.out.println(json);
        mvc.perform(post("/weather")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
    }

}
