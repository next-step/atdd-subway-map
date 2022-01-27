package nextstep.subway.global.error;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import nextstep.subway.exception.AlreadyRegisteredStationException;
import nextstep.subway.exception.CannotDeleteNotLastStation;
import nextstep.subway.exception.CannotDeleteSectionSizeUnderTwo;
import nextstep.subway.exception.LineNameDuplicationException;
import nextstep.subway.exception.NotFoundLineException;
import nextstep.subway.exception.RegisterStationException;
import nextstep.subway.exception.StationNameDuplicationException;
import nextstep.subway.global.error.exception.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StationNameDuplicationException.class)
    public ResponseEntity<ErrorResponse> handleStationNameDuplicationException() {
        ErrorResponse response = ErrorResponse.of(ErrorCode.DUPLICATION_STATION_NAME);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(LineNameDuplicationException.class)
    public ResponseEntity<ErrorResponse> handleLinenNameDuplicationException() {
        ErrorResponse response = ErrorResponse.of(ErrorCode.DUPLICATION_LINE_NAME);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(NotFoundLineException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundLineException() {
        ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND_LINE);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(InvalidDefinitionException.class)
    public ResponseEntity<ErrorResponse> handleValidationException() {
        ErrorResponse response = ErrorResponse.of(ErrorCode.ILLEGAL_ARGUMENT);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(RegisterStationException.class)
    public ResponseEntity<ErrorResponse> handleRegisterStationException() {
        ErrorResponse response = ErrorResponse.of(ErrorCode.ILLEGAL_SECTION_UPSTATION);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(CannotDeleteSectionSizeUnderTwo.class)
    public ResponseEntity<ErrorResponse> handleCannotDeleteSectionSizeUnderTwoException() {
        ErrorResponse response = ErrorResponse.of(ErrorCode.ILLEGAL_ARGUMENT);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(AlreadyRegisteredStationException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyRegisteredStationException() {
        ErrorResponse response = ErrorResponse.of(ErrorCode.ILLEGAL_ARGUMENT);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(CannotDeleteNotLastStation.class)
    public ResponseEntity<ErrorResponse> handleCannotDeleteNotLastStationException() {
        ErrorResponse response = ErrorResponse.of(ErrorCode.ILLEGAL_ARGUMENT);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
