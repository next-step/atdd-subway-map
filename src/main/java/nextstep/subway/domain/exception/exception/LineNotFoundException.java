package nextstep.subway.domain.exception.exception;

public class LineNotFoundException extends EntityNotFoundException {

    public LineNotFoundException(Long stationId) {
        super(String.format("Can't find line with id of %d", stationId));
    }
}
