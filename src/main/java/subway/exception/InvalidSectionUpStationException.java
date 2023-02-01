package subway.exception;

public class InvalidSectionUpStationException extends RuntimeException {

    private static final String MESSAGE =
        "새로운 구간의 상행역은 현재 노선의 하행 종점역이어야 합니다%n" +
        "- 현재 하행 종점역: %s%n" +
        "- 등록할 구간의 상행역: %s";

    public InvalidSectionUpStationException(String lastStationName, String upStationName) {
        super(String.format(MESSAGE, lastStationName, upStationName));
    }
}
