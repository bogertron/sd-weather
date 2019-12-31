package exam.sd.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Weather with same id already exists")
public class DuplicateWeatherException extends Exception {
}
