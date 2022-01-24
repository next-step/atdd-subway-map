package nextstep.subway.domain.service;

public class DuplicateArgumentException extends IllegalArgumentException {

    public DuplicateArgumentException(final String message) {
        super(message);
    }
}
