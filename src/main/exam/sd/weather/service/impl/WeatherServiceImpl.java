package exam.sd.weather.service.impl;

import exam.sd.weather.bean.DeleteRangeRequest;
import exam.sd.weather.bean.Weather;
import exam.sd.weather.controller.WeatherController;
import exam.sd.weather.dao.jpa.WeatherRepository;
import exam.sd.weather.exception.DuplicateWeatherException;
import exam.sd.weather.exception.WeatherException;
import exam.sd.weather.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    private static Logger logger = LoggerFactory.getLogger(WeatherController.class);

    public WeatherServiceImpl(WeatherRepository weatherRepo) {
        this.weatherRepository = weatherRepo;
    }

    /**
     * Delete all weather information
     */
    @Override
    public void deleteAll() {
        try {
            weatherRepository.deleteAll();
        } catch (PersistenceException pe) {
            logger.error("Failed to delete all", pe);
            throw new WeatherException("Error attempting to delete all");
        }
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
        try {
            List<Weather> entitiesToDelete = weatherRepository.findByRange(request.getStart(), request.getEnd(),
                    request.getLatitude(), request.getLongitude());
            weatherRepository.deleteAll(entitiesToDelete);
            logger.trace("Deleted {} weather points", entitiesToDelete.size());
        } catch (PersistenceException pe) {
            logger.error("Failed to delete range", pe);
            throw new WeatherException("Failed to delete range");
        }
    }

    /**
     * Retrieve all weather information
     * @return list of all weather information sorted by the id column
     */
    @Transactional(readOnly = true)
    @Override
    public List<Weather> getAll() {
        List<Weather> result;
        try {
            result = weatherRepository.findAllByOrderByIdAsc();
        } catch (PersistenceException pe) {
            logger.error("Failed to load all weather data", pe);
            throw new WeatherException("Unable to load all weather information");
        }

        return result;
    }

    /**
     * Create a new weather object
     * @param newWeather new weather information to attempt to save
     */
    @Transactional
    @Override
    public void createWeather(Weather newWeather) throws DuplicateWeatherException {
        try {
            if (!weatherRepository.existsById((newWeather.getId()))) {
                weatherRepository.save(newWeather);
            } else {
                throw new DuplicateWeatherException();
            }
        } catch (PersistenceException pe) {
            logger.error("Failed to create new weather", pe);
            throw new WeatherException("Unable to create new weather");
        }
    }
}
