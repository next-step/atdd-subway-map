package subway.exception;

public class SectionNotFoundException extends RuntimeException{
    private static final String message = "해당하는 구간을 찾을 수 없습니다.";

    public SectionNotFoundException() {
        super(message);
    }
}
