package nextstep.subway.exception.line;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LineControllerAdvice {

    private Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(LineBusinessException.class)
    protected ResponseEntity<LineErrorResponse> handleLineBusinessException(final LineBusinessException lineBusinessException) {

        log.error("LineBusinessException: {}", lineBusinessException.getMessage());

        final LineErrorCode lineErrorCode = LineErrorCode.values(lineBusinessException);
        final LineErrorResponse lineErrorResponse = new LineErrorResponse(lineErrorCode.message());
        return ResponseEntity.status(lineErrorCode.httpStatus()).body(lineErrorResponse);
    }
}
