package nextstep.subway.exception;

public class OnlyOneSectionException extends RuntimeException {

    public OnlyOneSectionException() {
        super("구간이 1개만 있습니다.");
    }
}
