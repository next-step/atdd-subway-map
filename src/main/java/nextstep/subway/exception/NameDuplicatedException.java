package nextstep.subway.exception;

public class NameDuplicatedException extends RuntimeException {

    private static final String MSG_DUPLICATED_NAME = "이미 존재하는 이름입니다. name: %s";

    public NameDuplicatedException(String name) {
        super(String.format(MSG_DUPLICATED_NAME, name));
    }
}
