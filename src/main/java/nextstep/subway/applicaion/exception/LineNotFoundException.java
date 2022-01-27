package nextstep.subway.applicaion.exception;

public class LineNotFoundException extends RuntimeException {

    private static final String MSG_INVALID_LINE_ID = "INVALID LINE id: %s";

    public LineNotFoundException(Long id) {
        super(String.format(MSG_INVALID_LINE_ID, id));
    }
}
