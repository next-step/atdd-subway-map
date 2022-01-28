package nextstep.subway.applicaion.exception;

public class AlreadyRegisteredLineException extends DuplicatedResourceException {
    private static final String ERROR_MESSAGE = "이미 존재하는 노선이 있습니다.";
    private String lineId;

    public AlreadyRegisteredLineException(String lineName) {
        super(String.format(ERROR_MESSAGE + " 노선 이름: [%s]", lineName));
    }
}
