package exam.sd.weather.bean;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DeleteRangeRequest {

    private LocalDate start;
    private LocalDate end;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public DeleteRangeRequest(LocalDate start, LocalDate end, BigDecimal latitude, BigDecimal longitude) {
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
