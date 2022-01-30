package nextstep.subway.exception;

public class NotFoundException extends RuntimeException {

    private static final String MSG_INVALID_LID = "INVALID %s id: %d";

    public NotFoundException(String type, Long id) {
        super(String.format(MSG_INVALID_LID, type, id));
    }
}
