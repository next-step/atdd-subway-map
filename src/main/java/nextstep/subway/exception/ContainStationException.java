package nextstep.subway.exception;

public class ContainStationException extends RuntimeException {
    private static final String MESSAGE = "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.";

    public ContainStationException() {
        super(MESSAGE);
    }
}
