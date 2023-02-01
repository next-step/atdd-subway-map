package subway.exception;

import org.springframework.http.HttpStatus;
import subway.exception.statusmessage.SubwayExceptionStatus;

public class SubwayException extends RuntimeException {

    private final SubwayExceptionStatus statusMessage;

    public SubwayException(SubwayExceptionStatus status, String reason) {
        super(reason);
        this.statusMessage = status;
    }

    public SubwayException(SubwayExceptionStatus status) {
        this(status, "");
    }

    public HttpStatus getStatus() {
        return statusMessage.getStatus();
    }
}
