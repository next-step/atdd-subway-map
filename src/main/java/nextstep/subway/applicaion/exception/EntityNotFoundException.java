package nextstep.subway.applicaion.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "데이터를 찾을 수 없습니다.";

    public EntityNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
