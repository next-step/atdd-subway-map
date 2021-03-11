package nextstep.subway.line.ui;

import nextstep.subway.line.exception.CreateSectionWithWrongUpStationException;
import nextstep.subway.line.exception.DeleteSectionWithNotLastException;
import nextstep.subway.line.exception.DeleteSectionWithOnlyOneException;
import nextstep.subway.line.exception.DownStationDuplicatedException;
import nextstep.subway.line.exception.LineNameDuplicatedException;
import nextstep.subway.line.exception.LineNotFoundException;
import nextstep.subway.section.exception.CannotRemoveRegisteredStationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class LineExceptionHandler {

    @ExceptionHandler(LineNotFoundException.class)
    public void handle(HttpServletResponse response, LineNotFoundException e) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(CannotRemoveRegisteredStationException.class)
    public void handle(HttpServletResponse response, CannotRemoveRegisteredStationException e) throws IOException {
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

    @ExceptionHandler(DeleteSectionWithOnlyOneException.class)
    public void handle(HttpServletResponse response, DeleteSectionWithOnlyOneException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
