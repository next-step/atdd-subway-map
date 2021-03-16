package nextstep.subway.line.ui;

import nextstep.subway.line.domain.exception.InvalidStationException;
import nextstep.subway.line.domain.exception.UnableToDeleteException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LineExceptionControllerHandler {

    @ExceptionHandler({
            InvalidStationException.class,
            UnableToDeleteException.class
    })
    protected ResponseEntity handleException(){
        return ResponseEntity.badRequest().build();
    }
}
