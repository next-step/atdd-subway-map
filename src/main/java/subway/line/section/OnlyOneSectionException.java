package subway.line.section;

public class OnlyOneSectionException extends RuntimeException {

    public OnlyOneSectionException() {
        super("구간이 하나 입니다.");
    }
}
