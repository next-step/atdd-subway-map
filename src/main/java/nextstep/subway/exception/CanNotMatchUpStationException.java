package nextstep.subway.exception;

public class CanNotMatchUpStationException extends BusinessException{

    private static final String message = "마지막 하행선은 새 구간의 상행선과 일치해야합니다.";

    public CanNotMatchUpStationException() {
        super(message);
    }
}
