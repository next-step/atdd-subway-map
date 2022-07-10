package nextstep.subway.domain.exception;

public class NotFoundLineException extends RuntimeException {

    private static final String MESSAGE = "지하철 노선이 존재하지 않습니다. id=%d";

    public NotFoundLineException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
