package exam.sd.weather.service;

import exam.sd.weather.bean.DeleteRangeRequest;
import exam.sd.weather.bean.Weather;
import exam.sd.weather.exception.DuplicateWeatherException;

import java.util.List;

public interface WeatherService {

    void deleteAll();

    void delete(DeleteRangeRequest request);

    List<Weather> getAll();

    void createWeather(Weather newWeather) throws DuplicateWeatherException;
}
