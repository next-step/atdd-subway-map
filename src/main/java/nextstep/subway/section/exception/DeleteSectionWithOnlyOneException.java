package nextstep.subway.section.exception;

public class DeleteSectionWithOnlyOneException extends RuntimeException {

    public DeleteSectionWithOnlyOneException() {
        super("지하철 노선에 구간이 1개인 경우는 구간을 삭제할 수 없습니다.");
    }
}
