package subway.line.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LineControllerAdvice {

    @ExceptionHandler(LineException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String test(LineException e) {
        e.printStackTrace();
        return e.getMessage();
    }
}
