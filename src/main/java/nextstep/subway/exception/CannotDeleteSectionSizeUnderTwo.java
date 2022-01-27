package nextstep.subway.exception;

public class CannotDeleteSectionSizeUnderTwo extends RuntimeException {

    private static final String MESSAGE = "구간의 크기가 2 이하일땐 삭제 할 수 없습니다.";

    public CannotDeleteSectionSizeUnderTwo() {
        super(MESSAGE);
    }
}