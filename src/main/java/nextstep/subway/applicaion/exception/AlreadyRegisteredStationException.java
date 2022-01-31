package nextstep.subway.applicaion.exception;

public class AlreadyRegisteredStationException extends DuplicatedResourceException {
    private static final String ERROR_MESSAGE = "이미 존재하는 역이 있습니다.";
    private String stationName;

    public AlreadyRegisteredStationException(String stationName) {
        super(String.format(ERROR_MESSAGE + " 역 이름: [%s]", stationName));
    }
}
