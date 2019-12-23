package exam.sd.weather.controller;

import exam.sd.weather.bean.DeleteRangeRequest;
import exam.sd.weather.bean.NoWeatherMessage;
import exam.sd.weather.bean.Weather;
import exam.sd.weather.exception.DuplicateWeatherException;
import exam.sd.weather.exception.NoWeatherException;
import exam.sd.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RequestMapping(value = "/erase", params = {"startDate", "endDate", "lat", "lon"})
    public void eraseRange(@RequestParam("start")Date startDate,
                           @RequestParam("end") Date endDate,
                           @RequestParam("lat") float latitute,
                           @RequestParam("lon") float longitude) {

        DeleteRangeRequest request = new DeleteRangeRequest(startDate, endDate, latitute, longitude);
        this.weatherService.delete(request);
    }

    @RequestMapping(value = "/erase")
    public void eraseAll() {
        this.weatherService.deleteAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/weather", method = RequestMethod.POST)
    public void addWeather(@RequestBody Weather weather) throws DuplicateWeatherException {
        this.weatherService.createWeather(weather);
    }

    @RequestMapping(value = "/weather", method = RequestMethod.GET)
    public Weather[] getAllWeather() throws NoWeatherException {
        Weather[] result;
        List<Weather> weather = this.weatherService.getAll();

        if (weather.size() > 0) {
            result = new Weather[weather.size()];
            weather.toArray(result);
            return result;
        } else {
            throw new NoWeatherException();
        }

    }

    @ExceptionHandler(NoWeatherException.class)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody NoWeatherMessage handleNoWeather(NoWeatherException ex) {
        return new NoWeatherMessage();
    }
}
