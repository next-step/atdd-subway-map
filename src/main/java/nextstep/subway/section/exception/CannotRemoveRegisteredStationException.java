package nextstep.subway.section.exception;

public class CannotRemoveRegisteredStationException extends RuntimeException {

    public CannotRemoveRegisteredStationException(Long id) {
        super("지하철 구간에 등록되어 있는 지하철역은 제거할 수 없습니다. id: " + id);
    }
}
