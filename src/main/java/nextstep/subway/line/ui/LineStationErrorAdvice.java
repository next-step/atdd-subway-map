package nextstep.subway.line.ui;

import nextstep.subway.station.application.StationDuplicateException;
import nextstep.subway.station.application.StationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class LineStationErrorAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(StationDuplicateException.class)
    public void handlerStationDuplicate() {}

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StationNotFoundException.class)
    public void handlerNotExistStation() {}
}
