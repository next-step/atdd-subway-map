package subway.application.domain;

public class IllegalMinSizeDeleteSectionException extends RuntimeException {

    public IllegalMinSizeDeleteSectionException() {
        super("구간은 두 개이상인 경우에만 삭제 할 수 있습니다");
    }

}
