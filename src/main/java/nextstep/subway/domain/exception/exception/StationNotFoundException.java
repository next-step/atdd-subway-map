package nextstep.subway.domain.exception.exception;

public class StationNotFoundException extends EntityNotFoundException {

    public StationNotFoundException(Long stationId) {
        super(String.format("Can't find station with id of %d", stationId));
    }
}
