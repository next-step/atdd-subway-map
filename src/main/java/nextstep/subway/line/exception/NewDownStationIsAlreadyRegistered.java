package nextstep.subway.line.exception;

public class NewDownStationIsAlreadyRegistered extends RuntimeException {

    private static final String EXCEPTION_DESCRIPTION = "새로운 구간의 하행역이 이미 등록되어 있습니다.";

    public NewDownStationIsAlreadyRegistered() {
        this(EXCEPTION_DESCRIPTION);
    }

    public NewDownStationIsAlreadyRegistered(String message) {
        super(message);
    }
}
