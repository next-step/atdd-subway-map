package nextstep.subway.line;

import nextstep.subway.station.DuplicatedStationNameException;

public class DuplicatedLineNameException extends RuntimeException{

    private static final String MESSAGE = "에 해당되는 노선 이름이 존재합합니다.";

    public DuplicatedLineNameException(String lineName) {
        super(lineName + MESSAGE);
    }
}
