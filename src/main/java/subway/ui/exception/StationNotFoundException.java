package subway.ui.exception;

public class StationNotFoundException extends NotFoundException {

    private static final String ERROR_MESSAGE = "존재하지 않는 Station 입니다.";

    public StationNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
