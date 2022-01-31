package nextstep.subway.applicaion.exception;

import org.springframework.http.HttpStatus;

public class NotRemoveStationException extends RuntimeException{
    private static final String REASON = "지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다.";
    private final HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    public NotRemoveStationException() {
        super(REASON);
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getReason(){
        return REASON;
    }
}
