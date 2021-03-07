package nextstep.subway.station;

public class DuplicatedStationNameException extends RuntimeException{

    private static final String MESSAGE = "에 해당하는 역 이름이 존재합니다.";

    public DuplicatedStationNameException(String stationName){
        super(stationName + MESSAGE);
    }

}
