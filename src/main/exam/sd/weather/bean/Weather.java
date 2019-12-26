package exam.sd.weather.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import exam.sd.weather.validation.TemperatureArrayConstraint;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Weather {
    @Id
    @NotNull(message = "Id is required")
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "date is required")
    private Date date;
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

    @JsonFormat(pattern="yyyy-MM-dd")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public boolean equals(Object o) {
        boolean result = false;

        if (o instanceof Weather) {
            Weather that = (Weather) o;
            result = this.id.equals(that.id);
        }
        return result;
    }
}
