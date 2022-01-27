package nextstep.subway.exception;

public class RegisterStationException extends RuntimeException {
    private static final String MESSAGE = "등록하려는 구간의 상행 역이 노선의 하행 종점역이 아닙니다.";

    public RegisterStationException() {
        super(MESSAGE);
    }
}
