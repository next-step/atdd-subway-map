package nextstep.subway.exception.handler;

import nextstep.subway.exception.DuplicateRegistrationRequestException;
import nextstep.subway.exception.ExceptionDto;
import nextstep.subway.exception.NotFoundRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicateRegistrationRequestException.class)
    public ResponseEntity<ExceptionDto> handlerDuplicateRequestException(DuplicateRegistrationRequestException e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(e.getMessage());

        return ResponseEntity.badRequest().body(exceptionDto);
    }

    @ExceptionHandler(NotFoundRequestException.class)
    public ResponseEntity<ExceptionDto> handlerNotFoundRequestException(NotFoundRequestException e) {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionDto);
    }
}
