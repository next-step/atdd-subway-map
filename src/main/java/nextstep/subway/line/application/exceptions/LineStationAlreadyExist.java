package nextstep.subway.line.application.exceptions;

public class LineStationAlreadyExist extends RuntimeException {
    public LineStationAlreadyExist(String message) {
        super(message);
    }
}
