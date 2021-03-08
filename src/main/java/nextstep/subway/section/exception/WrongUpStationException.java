package nextstep.subway.section.exception;

public class WrongUpStationException extends RuntimeException {

    public WrongUpStationException() {
        super("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.");
    }
}
