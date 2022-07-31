package nextstep.subway.applicaion.common;

public class UnappropriateStationException extends IllegalArgumentException {

    public UnappropriateStationException() {
        super("역이 해당 라인의 하행종점역이 아닙니다.");
    }
}
