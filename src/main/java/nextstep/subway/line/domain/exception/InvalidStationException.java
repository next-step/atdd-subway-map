package nextstep.subway.line.domain.exception;

import org.springframework.http.HttpStatus;

public class InvalidStationException extends RuntimeException{

    private String message;
    private HttpStatus status;

    public InvalidStationException() {}

    public InvalidStationException(String message) {
        this.message = message;
    }
}
