package subway.line;

public class DuplicateSectionStationException extends RuntimeException{

    public DuplicateSectionStationException() {
        super("이미 존재하는 지하철역입니다.");
    }
}
