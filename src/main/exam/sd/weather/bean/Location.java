package exam.sd.weather.bean;

import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Embeddable
public class Location {
    /* Latitude must be between [-90, 90] */
    @DecimalMin(value = "-90", message = "Lat must be at least -90")
    @DecimalMax(value = "90", message = "Lat cannot be larger than 90")
    @Digits(integer = 2, fraction=4, message = "lat must be between -90 and 90 and only have at most 4 decimal places")
    private BigDecimal lat;
    /* Longitude must be between [-180, 180] */
    @DecimalMin( value = "-180", message = "Lon must be at least -180")
    @DecimalMax(value = "180", message = "Lon cannot be larger than 180")
    @Digits(integer = 3, fraction=4, message = "lon must be between -180 and 180 and only have at most 4 decimal places")
    private BigDecimal lon;
    @NotBlank(message = "city must be have a value")
    private String city;
    @NotBlank(message = "state must be have a value")
    private String state;

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
