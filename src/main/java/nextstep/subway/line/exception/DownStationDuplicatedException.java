package nextstep.subway.line.exception;

public class DownStationDuplicatedException extends RuntimeException {

    public DownStationDuplicatedException() {
        super("새로운 구간의 하행역은 현재 등록되어있는 역일 수 없습니다.");
    }
}
