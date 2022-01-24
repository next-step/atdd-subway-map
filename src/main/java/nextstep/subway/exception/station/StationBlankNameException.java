package nextstep.subway.exception.station;

public class StationBlankNameException extends StationBusinessException {
    private static final String MESSAGE = "blank station name occurred";

    public StationBlankNameException() {
        super(MESSAGE);
    }
}
