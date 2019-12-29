package exam.sd.weather.dao.jpa;

import exam.sd.weather.bean.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    @Query("SELECT w FROM Weather w LEFT JOIN FETCH w.location l WHERE w.date>=:start AND w.date <=:end AND l.lon=:longitude AND l.lat=:latitude")
    List<Weather> findByRange(@Param("start") LocalDate start, @Param("end") LocalDate end,
                              @Param("latitude") BigDecimal latitude, @Param("longitude") BigDecimal longitude);
}
