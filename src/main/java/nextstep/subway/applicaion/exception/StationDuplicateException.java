package nextstep.subway.applicaion.exception;

public class StationDuplicateException extends ElementDuplicateException {
    private static final String REASON = "지하철역 이름 중복";

    public StationDuplicateException() {
        super(REASON);
    }
}