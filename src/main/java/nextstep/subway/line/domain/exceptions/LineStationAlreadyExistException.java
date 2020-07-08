package nextstep.subway.line.domain.exceptions;

public class LineStationAlreadyExistException extends RuntimeException {
    public LineStationAlreadyExistException(String message) {
        super(message);
    }
}
