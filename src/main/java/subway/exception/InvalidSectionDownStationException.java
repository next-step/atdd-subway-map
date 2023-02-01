package subway.exception;

public class InvalidSectionDownStationException extends RuntimeException {

    private static final String MESSAGE = 
        "새로운 구간의 하행역은 현재 노선에 등록되어 있는 역이 아니어야 합니다%n" +
        "- %s은 이미 현재 노선에 등록된 역입니다.";

    public InvalidSectionDownStationException(String downStationName) {
        super(String.format(MESSAGE, downStationName));
    }
}
