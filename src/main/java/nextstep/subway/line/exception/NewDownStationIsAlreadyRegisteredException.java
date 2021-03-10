package nextstep.subway.line.exception;

public class NewDownStationIsAlreadyRegisteredException extends RuntimeException {

    private static final String EXCEPTION_DESCRIPTION = "새로운 구간의 하행역이 이미 등록되어 있습니다.";

    public NewDownStationIsAlreadyRegisteredException() {
        this(EXCEPTION_DESCRIPTION);
    }

    public NewDownStationIsAlreadyRegisteredException(String message) {
        super(message);
    }
}
