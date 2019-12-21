package exam.sd.weather.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Weather {
    private int id;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
    private float[] temperature;
    private Location location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
}
