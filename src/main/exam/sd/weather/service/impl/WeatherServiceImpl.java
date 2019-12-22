package exam.sd.weather.service.impl;

import exam.sd.weather.bean.DeleteRangeRequest;
import exam.sd.weather.bean.Weather;
import exam.sd.weather.dao.jpa.WeatherRepository;
import exam.sd.weather.exception.DuplicateWeatherException;
import exam.sd.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    public WeatherServiceImpl(WeatherRepository weatherRepo) {
        this.weatherRepository = weatherRepo;
    }

    /**
     * Delete all weather information
     */
    @Override
    public void deleteAll() {
        weatherRepository.deleteAll();
    }

    /**
     * Delete weather information which is inside the request
     * @param request critera for weather information getting deleted
     */
    @Transactional
    @Override
    public void delete(DeleteRangeRequest request) {
        /*
         * Ideally, we would issue just delete from the repository, but there was a trade-off between storing
         * the temperature as a blob or an element collection. Went with element collection, and need to
         * retrieve the objects then delete
         */
        List<Weather> entitiesToDelete = weatherRepository.findByRange(request.getStart(), request.getEnd(),
                request.getLatitude(), request.getLongitude());
        weatherRepository.deleteAll(entitiesToDelete);
    }

    /**
     * Retrieve all weather information
     * @return list of all weather information sorted by the id column
     */
    @Transactional(readOnly = true)
    @Override
    public List<Weather> getAll() {
        List<Weather> result;
        Iterable<Weather> weathers = weatherRepository.findAll();
        result = StreamSupport.stream(weathers.spliterator(), false).sorted(new Comparator<Weather>() {
            @Override
            public int compare(Weather o1, Weather o2) {
                return o1.getId().compareTo(o2.getId());
            }
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * Create a new weather object
     * @param newWeather new weather information to attempt to save
     */
    @Transactional
    @Override
    public void createWeather(Weather newWeather) throws DuplicateWeatherException {
        if (!weatherRepository.existsById((newWeather.getId()))) {
            weatherRepository.save(newWeather);
        } else {
            throw new DuplicateWeatherException();
        }
    }
}
