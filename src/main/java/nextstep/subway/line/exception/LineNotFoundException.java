package nextstep.subway.line.exception;

public class LineNotFoundException extends RuntimeException {

    public LineNotFoundException(Long id) {
        super("유효한 지하철 노선이 존재하지 않습니다. id: " + id);
    }
}
