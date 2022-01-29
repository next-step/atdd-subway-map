package nextstep.subway.applicaion.dto;

import java.util.Date;
import org.springframework.http.HttpStatus;

public class ErrorResponse {
    private Date timestamp;
    private String path;
    private int status;
    private String error;
    private String message;

    public Date getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    private ErrorResponse(Date timestamp, String path, int status, String error, String message) {
        this.timestamp = timestamp;
        this.path = path;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public static ErrorResponse of(String path, HttpStatus errorStatus, String message) {
        return new ErrorResponse(new Date(), path, errorStatus.value(), errorStatus.getReasonPhrase(), message);
    }
}
