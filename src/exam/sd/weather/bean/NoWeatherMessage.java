package exam.sd.weather.bean;

public class NoWeatherMessage {
    private String message;

    public NoWeatherMessage() {
        message = "There is no weather data";
    }

    public String getMessage() {
        return message;
    }
}
