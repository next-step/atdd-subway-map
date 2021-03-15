package nextstep.subway.line.exception;

public class LineUndefinedException extends IllegalArgumentException {
    public LineUndefinedException() {
    }

    public LineUndefinedException(Long id ) {
        super("라인이 존재하지 않습니다. id : " + id);
    }
}
