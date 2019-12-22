package exam.sd.weather.bean;

import java.math.BigDecimal;
import java.util.Date;

public class DeleteRangeRequest {

    private Date start;
    private Date end;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public DeleteRangeRequest(Date start, Date  end, BigDecimal latitude, BigDecimal longitude) {
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

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}
