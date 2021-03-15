package nextstep.subway.exception;

public class NotLastStationException extends BusinessException{

    private static final String ERROR_MESSAGE = "마지막 지하철 역만 삭제할 수 있습니다.";
    public NotLastStationException() {
        super(ERROR_MESSAGE);
    }
}
