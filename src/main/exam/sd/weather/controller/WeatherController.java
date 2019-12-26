package exam.sd.weather.controller;

import exam.sd.weather.bean.DeleteRangeRequest;
import exam.sd.weather.bean.Weather;
import exam.sd.weather.exception.DuplicateWeatherException;
import exam.sd.weather.exception.NoWeatherException;
import exam.sd.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RequestMapping(value = "/erase", method= RequestMethod.DELETE,
            params = {"start", "end", "lat", "lon"})
    public void eraseRange(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                           @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                           @RequestParam(name = "lat") @Digits(integer = 2, fraction = 4) BigDecimal latitude,
                           @RequestParam(name = "lon") @Digits(integer = 3, fraction = 4) BigDecimal longitude) {

        DeleteRangeRequest request = new DeleteRangeRequest(startDate, endDate, latitude, longitude);
        this.weatherService.delete(request);
    }

    @RequestMapping(value = "/erase", method = RequestMethod.DELETE)
    public void eraseAll() {
        this.weatherService.deleteAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/weather", method = RequestMethod.POST)
    public void addWeather(@Valid @RequestBody Weather weather) throws DuplicateWeatherException {
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
    public @ResponseBody Map<String, String> handleNoWeather(NoWeatherException ex) {

        Map<String, String> result = new HashMap<>();
        result.put("message","There is no weather data");
        return result;
    }

    /**
     * Handler for when the weather object passed in is not populated correctly
     * @param ex Detailed how the weather bean failed validation
     * @return Information back to user as to what failed
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    /**
     * Handler for when parameters are not passed in correctly
     * @param ex Detailed message for which paramaeter failed
     * @return Information back to user as to what failed
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        String message = String.format("Name [%s] parameter [%s] value [%s] message [%s]", ex.getName(),
                ex.getParameter().toString(), ex.getValue(), ex.getMessage());
        errors.put("message", message);
        return errors;
    }
}
