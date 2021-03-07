package nextstep.subway.station.exception;

public class StationNameDuplicatedException extends RuntimeException {

    public StationNameDuplicatedException(String name) {
        super("지하철역 이름은 중복될 수 없습니다. name: " + name);
    }
}
