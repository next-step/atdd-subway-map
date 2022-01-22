package nextstep.subway.applicaion.exception;

public class LineDuplicateException extends ElementDuplicateException {
    private static final String REASON = "지하철 노선 이름 중복";

    public LineDuplicateException() {
        super(REASON);
    }
}