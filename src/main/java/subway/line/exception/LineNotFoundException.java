package subway.line.exception;

public class LineNotFoundException extends RuntimeException {

    public LineNotFoundException(Long id) {
        super(String.format("지하철 노선을 찾을 수 없습니다. (id = %d)", id));
    }
}
