package nextstep.subway.section.domain;

import org.springframework.http.HttpStatus;

public class InvalidStationException extends RuntimeException{

    private String message;
    private HttpStatus status;

    public InvalidStationException() {}

    public InvalidStationException(String message) {
        System.err.println(message);
        this.message = message;
    }
}
