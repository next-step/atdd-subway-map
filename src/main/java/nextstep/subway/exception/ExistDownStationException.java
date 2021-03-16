package nextstep.subway.exception;

public class ExistDownStationException extends BusinessException{

    private static final String message = "현재 구간에 해당 하행역이 존재합니다.";

    public ExistDownStationException() {
        super(message);
    }
}
