package nextstep.subway.domain.exception;

public class SectionDeleteException extends IllegalStateException {
    private static final String MESSAGE_ONLY_ONE_SECTION = "구간이 하나만 존재합니다.";
    private static final String MESSAGE_INVALID_MATCH_END_STATION = "마지막 구간만 제거가 가능합니다. stationId=%d";

    public SectionDeleteException() {
        super(MESSAGE_ONLY_ONE_SECTION);
    }

    public SectionDeleteException(Long stationId) {
        super(String.format(MESSAGE_INVALID_MATCH_END_STATION, stationId));
    }
}
