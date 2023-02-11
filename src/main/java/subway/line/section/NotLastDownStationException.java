package subway.line.section;

public class NotLastDownStationException extends RuntimeException {

    public NotLastDownStationException() {
        super("하행 종점역이 아닙니다.");
    }
}
