package nextstep.subway.domain.exception;

public class NotFoundStationException extends RuntimeException {

    private static final String MESSAGE = "Station 이 존재하지 않습니다. id=%d";

    public NotFoundStationException(Long id) {
        super(String.format(MESSAGE, id));
    }
}
