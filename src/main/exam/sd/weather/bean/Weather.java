package exam.sd.weather.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Weather {
    public final static int TEMPERATURE_COUNT = 24;
    @Id
    @NotNull(message = "Id is required")
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "date is required")
    private LocalDate date;

    @NotNull(message = "temperature is required")
    @ElementCollection
    @CollectionTable(name = "weather_temperature", joinColumns = @JoinColumn(name = "weather_id"))
    @OrderColumn(name = "temperature_order")
    @Size(min = TEMPERATURE_COUNT, max = TEMPERATURE_COUNT, message = "Temperature must have 24 values")
    @Column(name = "temperature", precision = 4, scale = 1)
    // for reference of documented ranges: https://www.space.com/17816-earth-temperature.html
    // set limits to next 100 just in case
    private List<
            @DecimalMin(value = "-200", message = "temperature cannot be less than -200")
            @DecimalMax(value = "200", message = "temperature cannot be greater then 200")
            @Digits(integer = 3, fraction = 1, message = "temperature cannot have more than 1 decimal point")
            BigDecimal> temperature;

    @NotNull(message = "location is required")
    @Valid
    private Location location;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<BigDecimal> getTemperature() {
        return temperature;
    }

    public void setTemperature(List<BigDecimal> temperature) {
        this.temperature = temperature;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
