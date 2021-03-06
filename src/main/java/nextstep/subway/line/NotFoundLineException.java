package nextstep.subway.line;

public class NotFoundLineException extends RuntimeException{

    private static final String MESSAGE = "에 해당되는 아이디를 찾지 못하였습니다.";

    public NotFoundLineException(Long id) {
        super(id+MESSAGE);
    }
}
