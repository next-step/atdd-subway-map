package nextstep.subway.applicaion.dto;

import java.time.LocalDateTime;

public class UpdateLineResponse {
    private LocalDateTime dateTime;

    public UpdateLineResponse(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
