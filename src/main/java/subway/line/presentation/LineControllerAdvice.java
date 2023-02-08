package subway.line.presentation;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.common.exception.ErrorResponse;
import subway.line.exception.DownStationAlreadyRegisteredException;
import subway.line.exception.LineNotFoundException;
import subway.line.exception.NotLastSectionException;
import subway.line.exception.NotSameAsRegisteredDownStationException;
import subway.line.exception.SectionNotFoundException;
import subway.line.exception.SingleSectionException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@RestControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class LineControllerAdvice {

    /**
     * 지하철 노선을 찾을 수 없을 때 던지는 예외를 핸들링 합니다.
     *
     * @return 예외 메시지와 응답 코드를 리턴
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LineNotFoundException.class)
    ErrorResponse handleLineNotFound(final LineNotFoundException exception) {
        return ErrorResponse.from(exception);
    }

    /**
     * 등록된 하행역과 일치하지 않을 때 던지는 예외를 핸들링 합니다.
     *
     * @return 예외 메시지와 응답 코드를 리턴
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NotSameAsRegisteredDownStationException.class)
    ErrorResponse handleNotSameAsRegisteredDownStation(final NotSameAsRegisteredDownStationException exception) {
        return ErrorResponse.from(exception);
    }

    /**
     * 하행역이 이미 등록되어 있을 때 던지는 예외를 핸들링 합니다.
     *
     * @return 예외 메시지와 응답 코드를 리턴
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DownStationAlreadyRegisteredException.class)
    ErrorResponse handleDownStationAlreadyRegistered(final DownStationAlreadyRegisteredException exception) {
        return ErrorResponse.from(exception);
    }

    /**
     * 구간 삭제 시 최초 생성된 구간만 있는 경우 던지는 예외를 핸들링 합니다.
     *
     * @return 예외 메시지와 응답 코드를 리턴
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SingleSectionException.class)
    ErrorResponse handleSingleSection(final SingleSectionException exception) {
        return ErrorResponse.from(exception);
    }

    /**
     * 삭제할 구간이 마지막 구간이 아닐 때 던지는 예외를 핸들링 합니다.
     *
     * @return 예외 메시지와 응답 코드를 리턴
     */
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NotLastSectionException.class)
    ErrorResponse handleNotLastSection(final NotLastSectionException exception) {
        return ErrorResponse.from(exception);
    }

    /**
     * 구간을 찾을 수 없을 때 던지는 예외를 핸들링 합니다.
     *
     * @return 예외 메시지와 응답 코드를 리턴
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SectionNotFoundException.class)
    ErrorResponse handleSectionNotFound(final SectionNotFoundException exception) {
        return ErrorResponse.from(exception);
    }
}
