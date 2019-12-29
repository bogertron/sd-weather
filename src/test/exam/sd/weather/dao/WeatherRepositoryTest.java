package exam.sd.weather.dao;

import exam.sd.weather.bean.Location;
import exam.sd.weather.bean.Weather;
import exam.sd.weather.dao.jpa.WeatherRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class WeatherRepositoryTest {

    @Autowired
    private WeatherRepository weatherRepository;

    @Before
    public void before() {
        weatherRepository.deleteAll();
    }

    /**
     * Verify that the respository has been correctly wired up
     */
    @Test
    public void injectedComponentsAreNotNull() {
        assertNotNull("Weather repository was not wired up", weatherRepository);
    }

    /**
     * Test deletion by range by creating 2 distinct weather points, and delete one of the two
     * @throws ParseException
     */
    @Test
    public void testDeleteByRange() throws ParseException {
        Location l1 = new Location();
        l1.setState("CA");
        l1.setCity("C1");
        l1.setLat(new BigDecimal((22)));
        l1.setLon(new BigDecimal((22)));

        BigDecimal[] temp = new BigDecimal[24];
        for (int idx = 0; idx < temp.length; idx++) {
            temp[idx] = new BigDecimal(44);
        }

        Weather w1 = new Weather();
        w1.setTemperature(temp);
        w1.setLocation(l1);
        w1.setDate(LocalDate.of(2020, 1, 2));
        w1.setId(3L);

        weatherRepository.save(w1);

        Location l2 = new Location();
        l2.setState("CA");
        l2.setCity("C1");
        l2.setLat(new BigDecimal((22)));
        l2.setLon(new BigDecimal((22)));

        Weather w2 = new Weather();
        w2.setTemperature(temp);
        w2.setLocation(l2);
        w2.setDate(LocalDate.of(2020, 1, 5));
        w2.setId(4L);

        weatherRepository.save(w2);

        List<Weather> weatherList = weatherRepository.findAll();
        assertEquals(2, weatherList.size());

        List<Weather> toDelete = weatherRepository.findByRange(LocalDate.of(2020, 1, 2),
                LocalDate.of(2020,1, 4),
                l1.getLat(), l1.getLon());

        weatherRepository.deleteAll(toDelete);

        weatherList = weatherRepository.findAll();
        // since the range only caught a single entity, we should have one left
        assertEquals(1, weatherList.size());

        // make sure the one we have left is the one we were expecting
        assertEquals(w2.getId(), weatherList.get(0).getId());
    }

    /**
     * Verify that the delete all test actually deletes all
     * @throws ParseException
     */
    @Test
    public void deleteAllTest() throws ParseException {
        Location l1 = new Location();
        l1.setState("CA");
        l1.setCity("C1");
        l1.setLat(new BigDecimal(22));
        l1.setLon(new BigDecimal(22));

        Weather w1 = new Weather();
        BigDecimal[] temp = new BigDecimal[24];
        for (int idx = 0; idx < temp.length; idx++) {
            temp[idx] = new BigDecimal(44);
        }
        w1.setTemperature(temp);
        w1.setLocation(l1);
        w1.setDate(LocalDate.now());
        w1.setId(3L);

        weatherRepository.save(w1);

        List<Weather> weatherList = weatherRepository.findAll();
        assertEquals(1, weatherList.size());

        weatherRepository.deleteAll();
        weatherList = weatherRepository.findAll();
        assertTrue(weatherList.isEmpty());
    }
}
