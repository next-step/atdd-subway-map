package nextstep.subway.exception;

public class DuplicatedNameException extends RuntimeException {

    public DuplicatedNameException() {
        super("중복된 이름입니다.");
    }

}
