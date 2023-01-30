package subway.exception;

public class StationNotFoundException extends RuntimeException {

    private static final String MESSAGE = "해당 지하철역이 존재하지 않습니다 (id = %d)";

    public StationNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
