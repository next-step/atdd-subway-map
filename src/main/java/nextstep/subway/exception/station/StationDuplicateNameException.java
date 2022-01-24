package nextstep.subway.exception.station;

public class StationDuplicateNameException extends StationBusinessException {
    private static final String MESSAGE = "duplicate station name occurred";

    public StationDuplicateNameException() {
        super(MESSAGE);
    }
}
