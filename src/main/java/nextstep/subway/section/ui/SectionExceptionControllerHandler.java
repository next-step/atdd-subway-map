package nextstep.subway.section.ui;

import nextstep.subway.section.domain.InvalidStationException;
import nextstep.subway.section.domain.UnableToDeleteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SectionExceptionControllerHandler {

    //https://www.baeldung.com/exception-handling-for-rest-with-spring
    @ExceptionHandler({
            InvalidStationException.class,
            UnableToDeleteException.class
    })
    protected ResponseEntity handleException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
