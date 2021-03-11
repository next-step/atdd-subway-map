package nextstep.subway.exception;

public class CanNotRemoveSectionException extends BusinessException{

    private static final String message = "구간이 하나일 경우 삭제할 수 없습니다.";

    public CanNotRemoveSectionException() {
        super(message);
    }
}
