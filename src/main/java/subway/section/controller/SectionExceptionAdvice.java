package subway.section.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.common.exception.http.dto.SubwayHttpExceptionResponse;
import subway.section.exception.DownEndStationRegisteredOnLineException;
import subway.section.exception.DownStationAlreadyExistsException;
import subway.section.exception.DownStationMustBeUpStationException;
import subway.section.exception.OnlyOneSectionException;

@RestControllerAdvice
public class SectionExceptionAdvice {

    @ExceptionHandler(DownStationAlreadyExistsException.class)
    public ResponseEntity<SubwayHttpExceptionResponse> handlerException(DownStationAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(SubwayHttpExceptionResponse.from(ex));
    }

    @ExceptionHandler(DownStationMustBeUpStationException.class)
    public ResponseEntity<SubwayHttpExceptionResponse> handlerException(DownStationMustBeUpStationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(SubwayHttpExceptionResponse.from(ex));
    }

    @ExceptionHandler(DownEndStationRegisteredOnLineException.class)
    public ResponseEntity<SubwayHttpExceptionResponse> handlerException(DownEndStationRegisteredOnLineException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(SubwayHttpExceptionResponse.from(ex));
    }

    @ExceptionHandler(OnlyOneSectionException.class)
    public ResponseEntity<SubwayHttpExceptionResponse> handlerException(OnlyOneSectionException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(SubwayHttpExceptionResponse.from(ex));
    }

}
