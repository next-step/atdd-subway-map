package nextstep.subway.applicaion.exception;

public class NewDownStationDuplicateException extends ElementDuplicateException {
    private static final String REASON = "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.";

    public NewDownStationDuplicateException() {
        super(REASON);
    }
}
