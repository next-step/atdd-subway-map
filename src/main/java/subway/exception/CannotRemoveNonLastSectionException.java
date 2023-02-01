package subway.exception;

public class CannotRemoveNonLastSectionException extends RuntimeException {

    private static final String MESSAGE =
        "지하철 노선에 등록된 마지막 구간만 제거할 수 있습니다%n" +
        "- 현재 노선의 하행 종점역: %s%n" +
        "- 전달된 역: %s";

    public CannotRemoveNonLastSectionException(String lastStationName, String targetStationName) {
        super(String.format(MESSAGE, lastStationName, targetStationName));
    }
}
