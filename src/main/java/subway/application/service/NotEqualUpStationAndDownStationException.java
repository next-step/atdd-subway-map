package subway.application.service;

public class NotEqualUpStationAndDownStationException extends RuntimeException {

    public NotEqualUpStationAndDownStationException() {
        super("새로운 구간에 이전 하행과 새로운 상행이 같지 않습니다");
    }

}
