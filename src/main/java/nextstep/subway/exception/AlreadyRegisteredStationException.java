package nextstep.subway.exception;

public class AlreadyRegisteredStationException extends RuntimeException {
    private static final String MESSAGE = "이미 등록된 역을 하행역으로 등록 할 수 없습니다.";

    public AlreadyRegisteredStationException() {
        super(MESSAGE);
    }
}
