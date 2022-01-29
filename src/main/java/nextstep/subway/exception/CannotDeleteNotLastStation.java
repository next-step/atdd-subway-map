package nextstep.subway.exception;

public class CannotDeleteNotLastStation extends RuntimeException {

    private static final String MESSAGE = "해당 (%s)은 노선의 마지막 역이 아닙니다. 노선의 마지막 역만 지울 수 있습니다.";

    public CannotDeleteNotLastStation(String name) {
        super(String.format(MESSAGE, name));
    }
}
