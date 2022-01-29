package nextstep.subway.exception;

public class SameStationsRegisteredSectionException extends RuntimeException {

    private static final String MESSAGE = "같은 열차는 한 구간에 등록될 수 없습니다. [열차 : %s]";

    public SameStationsRegisteredSectionException(String stationName) {
        super(String.format(MESSAGE, stationName));
    }
}
