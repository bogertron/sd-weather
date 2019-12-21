package exam.sd.weather.service.impl;

import exam.sd.weather.bean.DeleteRangeRequest;
import exam.sd.weather.bean.Weather;
import exam.sd.weather.service.WeatherService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WeatherServiceImpl implements WeatherService {


    private List<Weather> list = new ArrayList<Weather>();

    @Override
    public void deleteAll() {
        list.clear();
    }

    @Override
    public void delete(DeleteRangeRequest request) {
        List<Weather> weathers = new LinkedList<>();
        for (Weather weather: list) {
            if (weather.getDate().isAfter(request.getStart()) &&
                    weather.getDate().isBefore(request.getEnd()) &&
                    weather.getLocation().getLat() == request.getLatitude() &&
                    weather.getLocation().getLon() == request.getLongitude()) {
                weathers.add((weather));
            }
        }

        for (Weather weather : weathers) {
            list.remove(weather);
        }

    }

    @Override
    public List<Weather> getAll() {
        return list;
    }

    @Override
    public void createWeather(Weather newWeather) {
        list.add(newWeather);
    }
}
