package nextstep.subway.applicaion.common;

public class MinimumSectionException extends IllegalArgumentException {
    public MinimumSectionException() {
        super("해당 라인의 구간 갯수가 최소 갯수이므로 삭제가 불가능합니다.");
    }
}
