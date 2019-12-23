package exam.sd.weather.bean;

import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.util.Date;

public class DeleteRangeRequest {

    private Date start;
    private Date end;
    private float latitude;
    private float longitude;

    public DeleteRangeRequest(Date start, Date  end, float latitude, float longitude) {
        this.start = start;
        this.end = end;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
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
