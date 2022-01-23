package nextstep.subway.applicaion.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final HttpStatus status;
    private final String reason;
    private final LocalDateTime dateTime;

    public ErrorResponse(HttpStatus status, String reason) {
        this.status = status;
        this.reason = reason;
        this.dateTime = LocalDateTime.now();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
