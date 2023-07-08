package subway.section;

public class StationDoesNotMatchException extends RuntimeException {
    public StationDoesNotMatchException() {
        super("역이 일치하지 않습니다.");
    }
}
