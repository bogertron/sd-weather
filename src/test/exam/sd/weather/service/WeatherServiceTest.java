package exam.sd.weather.service;

import exam.sd.weather.bean.Weather;
import exam.sd.weather.dao.jpa.WeatherRepository;
import exam.sd.weather.exception.DuplicateWeatherException;
import exam.sd.weather.service.impl.WeatherServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class WeatherServiceTest {

    /**
     * Verify that when the delete gets called from the weather service that
     * the repository call is invoked as expected
     */
    @Test
    public void repository_verifyDeleteAll() {
        WeatherRepository repository = mock(WeatherRepository.class);
        WeatherService service = new WeatherServiceImpl(repository);
        service.deleteAll();
        verify(repository).deleteAll();
    }

    /**
     * Verify that if a weather object with an id exists, then the duplicate weather
     * exception is thrown
     */
    @Test
    public void weatherService_testDuplicateWeatherId() {
        Weather dupe = new Weather();
        dupe.setId(444L);
        WeatherRepository repository = mock(WeatherRepository.class);
        when(repository.existsById(dupe.getId())).thenReturn(true);
        WeatherService service = new WeatherServiceImpl((repository));
        assertThrows(DuplicateWeatherException.class, () -> {
            service.createWeather(dupe);
        });
    }

    /**
     * Validate happy path for weather creation
     */
    @Test
    public void weatherService_testSaveWeather() {
        Weather dupe = new Weather();
        dupe.setId(444L);
        WeatherRepository repository = mock(WeatherRepository.class);
        when(repository.existsById(dupe.getId())).thenReturn(false);
        when(repository.save(dupe)).thenReturn(dupe);
        WeatherService service = new WeatherServiceImpl((repository));
        try {
            service.createWeather(dupe);
        } catch (DuplicateWeatherException dwe) {
            fail("Duplicate weather exception incorrectly thrown");
        }

    }
}
