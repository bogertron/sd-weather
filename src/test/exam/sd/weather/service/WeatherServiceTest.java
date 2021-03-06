package exam.sd.weather.service;

import exam.sd.weather.bean.DeleteRangeRequest;
import exam.sd.weather.bean.Weather;
import exam.sd.weather.dao.jpa.WeatherRepository;
import exam.sd.weather.exception.DuplicateWeatherException;
import exam.sd.weather.service.impl.WeatherServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Test
    public void repository_verifyDeleteRangeParameters() {
        WeatherRepository repository = mock(WeatherRepository.class);
        WeatherService service = new WeatherServiceImpl(repository);
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2020, 1, 1);
        BigDecimal lat = new BigDecimal("33");
        BigDecimal lon = new BigDecimal("33");
        DeleteRangeRequest request = new DeleteRangeRequest(start, end, lat, lon);
        service.delete(request);
        verify(repository).findByRange(start, end, lat, lon);
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
