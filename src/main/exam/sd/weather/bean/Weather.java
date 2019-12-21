package exam.sd.weather.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Date;

public class Weather {
    private int id;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date date;
    private float[] temperature;
    private Location location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonFormat(pattern="yyyy-MM-dd")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float[] getTemperature() {
        return temperature;
    }

    public void setTemperature(float[] temperature) {
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
            result = this.id == that.id;
        }
        return result;
    }
}
