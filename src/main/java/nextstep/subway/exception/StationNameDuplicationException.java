package nextstep.subway.exception;

public class StationNameDuplicationException extends RuntimeException {

    private static final String MESSAGE = "%s는 이미 존재하는 역 이름 입니다.";

    public StationNameDuplicationException(String name) {
        super(String.format(MESSAGE, name));
    }
}
