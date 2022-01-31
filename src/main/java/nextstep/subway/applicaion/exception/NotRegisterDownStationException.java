package nextstep.subway.applicaion.exception;

import org.springframework.http.HttpStatus;

public class NotRegisterDownStationException extends RuntimeException{
    private static final String REASON = "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.";
    private final HttpStatus statusCode = HttpStatus.BAD_REQUEST;

    public NotRegisterDownStationException() {
        super(REASON);
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getReason(){
        return REASON;
    }
}
