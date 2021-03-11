package nextstep.subway.line.exception;

public class DeleteStationIsNotLastStationException extends RuntimeException {

    private static final String EXCEPTION_DESCRIPTION = "삭제하려는 역이 하행 종점역이 아닙니다.";

    public DeleteStationIsNotLastStationException() {
        this(EXCEPTION_DESCRIPTION);
    }

    public DeleteStationIsNotLastStationException(String message) {
        super(message);
    }
}
