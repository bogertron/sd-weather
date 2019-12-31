package exam.sd.weather.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import exam.sd.weather.validation.TemperatureArrayConstraint;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Weather {
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
    @TemperatureArrayConstraint
    private BigDecimal[] temperature;

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

    public BigDecimal[] getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal[] temperature) {
        this.temperature = temperature;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
