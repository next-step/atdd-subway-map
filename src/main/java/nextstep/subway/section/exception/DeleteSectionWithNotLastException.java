package nextstep.subway.section.exception;

public class DeleteSectionWithNotLastException extends RuntimeException {

    public DeleteSectionWithNotLastException() {
        super("지하철 노선에 등록된 마지막 구간만 제거할 수 있습니다.");
    }
}
