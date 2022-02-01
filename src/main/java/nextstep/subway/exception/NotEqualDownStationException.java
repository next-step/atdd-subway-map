package nextstep.subway.exception;

public class NotEqualDownStationException extends RuntimeException {

    private static final String MESSAGE = "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.";

    public NotEqualDownStationException() {
        super(MESSAGE);
    }
}