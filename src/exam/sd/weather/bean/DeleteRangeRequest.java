package exam.sd.weather.bean;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.util.Date;

public class DeleteRangeRequest {

    private LocalDate start;
    private LocalDate end;
    private float latitude;
    private float longitude;

    public DeleteRangeRequest(LocalDate start, LocalDate  end, float latitude, float longitude) {
        this.start = start;
        this.end = end;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
