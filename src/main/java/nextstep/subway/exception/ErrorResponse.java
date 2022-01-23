package nextstep.subway.exception;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ErrorResponse {
    private final String message;
    private final LocalDateTime date;

    public ErrorResponse(String message) {
        this.message = message;
        this.date = LocalDateTime.now();
    }
}
