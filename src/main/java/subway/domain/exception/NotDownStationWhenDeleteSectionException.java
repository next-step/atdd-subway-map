package subway.domain.exception;

public class NotDownStationWhenDeleteSectionException extends RuntimeException {

    public NotDownStationWhenDeleteSectionException() {
        super("삭제하려는 구간은 해당 노선에 마지막 하행역이 아닙니다");
    }

}
