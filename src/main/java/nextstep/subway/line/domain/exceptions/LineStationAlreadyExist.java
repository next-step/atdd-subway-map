package nextstep.subway.line.domain.exceptions;

public class LineStationAlreadyExist extends RuntimeException {
    public LineStationAlreadyExist(String message) {
        super(message);
    }
}
