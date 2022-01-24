package nextstep.subway.exception.station;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class StationControllerAdvice {

    private Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(StationBusinessException.class)
    protected ResponseEntity<StationErrorResponse> handleArticleBusinessException(final StationBusinessException stationBusinessException) {

        log.error("StationBusinessException: {}", stationBusinessException.getMessage());

        final StationErrorCode articleErrorCode = StationErrorCode.values(stationBusinessException);
        final StationErrorResponse articleErrorResponse = new StationErrorResponse(articleErrorCode.message());
        return ResponseEntity.status(articleErrorCode.httpStatus()).body(articleErrorResponse);
    }
}
