package exam.sd.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The exception that should be thrown to clients if an internal error occurs
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class WeatherException extends RuntimeException {

    public WeatherException(String message) {
        super(message);
    }

}
