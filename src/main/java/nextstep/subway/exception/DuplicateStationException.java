package nextstep.subway.exception;

public class DuplicateStationException extends RuntimeException{

    private static final String MESSAGE = "역 이름이 중복 됩니다.";

    public DuplicateStationException() {
        super(MESSAGE);
    }

}
