package subway.station.exception;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException(Long id) {
        super(String.format("지하철 역을 찾을 수 없습니다. (id = %d)", id));
    }
}
