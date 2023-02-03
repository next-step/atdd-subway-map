package subway.exception;

import org.springframework.http.HttpStatus;

public class LineNotFoundException extends SubwayException {
    public static final String EXCEPTION_MESSAGE = "지하철노선을 찾을 수 없습니다.";

    public LineNotFoundException() {
        super(HttpStatus.NOT_FOUND, EXCEPTION_MESSAGE);
    }
}
