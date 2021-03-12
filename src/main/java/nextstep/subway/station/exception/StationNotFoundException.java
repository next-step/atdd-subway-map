package nextstep.subway.station.exception;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException(Long id) {
        super("유효한 지하철역이 존재하지 않습니다. id: " + id);
    }
}
