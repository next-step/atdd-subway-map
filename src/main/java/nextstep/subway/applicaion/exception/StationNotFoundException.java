package nextstep.subway.applicaion.exception;

public class StationNotFoundException extends NotFoundException {

    private static final String TYPE = "STATION";

    public StationNotFoundException(Long stationId) {
        super(TYPE, stationId);
    }
}
