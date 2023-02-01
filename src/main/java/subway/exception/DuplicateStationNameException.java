package subway.exception;

public class DuplicateStationNameException extends RuntimeException {

    private static final String MESSAGE = "이미 존재하는 역 이름입니다: %s";

    public DuplicateStationNameException(String stationName) {
        super(String.format(MESSAGE, stationName));
    }
}
