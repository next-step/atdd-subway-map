package nextstep.subway.line.exception;

public class LineNameDuplicatedException extends RuntimeException {

    public LineNameDuplicatedException(String name) {
        super("지하철 노선 이름은 중복될 수 없습니다. name: " + name);
    }
}
