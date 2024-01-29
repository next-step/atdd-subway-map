package subway.section;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.line.exception.LineException;

@RestControllerAdvice
public class SectionControllerAdvice {

    @ExceptionHandler(SectionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String test(SectionException e) {
        e.printStackTrace();
        return e.getMessage();
    }
}
