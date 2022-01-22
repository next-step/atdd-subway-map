package nextstep.subway.ui;

import nextstep.subway.common.exception.DuplicateAttributeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {

    private Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateAttributeException.class)
    public void handleDuplicateAttributeException(final DuplicateAttributeException ex) {
        log.info(ex.getMessage());
    }
}
