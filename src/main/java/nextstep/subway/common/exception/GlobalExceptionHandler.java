package nextstep.subway.common.exception;

import nextstep.subway.line.exception.LineNameDuplicatedException;
import nextstep.subway.section.exception.CreateSectionWithWrongUpStationException;
import nextstep.subway.section.exception.DeleteSectionWithNotLastException;
import nextstep.subway.section.exception.DownStationDuplicatedException;
import nextstep.subway.station.exception.StationNameDuplicatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StationNameDuplicatedException.class)
    public void handle(HttpServletResponse response, StationNameDuplicatedException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(LineNameDuplicatedException.class)
    public void handle(HttpServletResponse response, LineNameDuplicatedException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(CreateSectionWithWrongUpStationException.class)
    public void handle(HttpServletResponse response, CreateSectionWithWrongUpStationException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(DownStationDuplicatedException.class)
    public void handle(HttpServletResponse response, DownStationDuplicatedException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(DeleteSectionWithNotLastException.class)
    public void handle(HttpServletResponse response, DeleteSectionWithNotLastException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
