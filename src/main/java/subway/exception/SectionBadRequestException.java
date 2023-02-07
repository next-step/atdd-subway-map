package subway.exception;

import org.springframework.http.HttpStatus;

public class SectionBadRequestException extends SubwayException {
    public static final String MESSAGE = "구간의 지하철역을 삭제할 수 없습니다.";

    public SectionBadRequestException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
