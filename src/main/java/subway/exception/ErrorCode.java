package subway.exception;

import org.springframework.http.HttpStatus;


public interface ErrorCode {

    public HttpStatus getHttpStatus();
    public String getMessage();

}

