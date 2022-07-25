package nextstep.subway.applicaion.handler;

import lombok.extern.slf4j.Slf4j;
import nextstep.subway.applicaion.dto.ErrorResponse;
import nextstep.subway.applicaion.exceptions.BadStationRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BadStationRequestException.class)
    public ResponseEntity<ErrorResponse> handleException(BadStationRequestException exception)
    {
        log.error("AbstractException", exception.getErrorCode());
        ErrorResponse errorResponse = new ErrorResponse(exception.getErrorCode());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(exception.getErrorCode().getStatus()));
    }
}
