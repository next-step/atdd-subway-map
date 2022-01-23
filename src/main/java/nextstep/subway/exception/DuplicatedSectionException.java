package nextstep.subway.exception;

public class DuplicatedSectionException extends IllegalArgumentException{
    private static final String MESSAGE = "이미 등록된 역을 하행 또는 상행 역으로 등록할 수 없습니다.";

    public DuplicatedSectionException() {
        super(MESSAGE);
    }
}
