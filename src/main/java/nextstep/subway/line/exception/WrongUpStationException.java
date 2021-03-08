package nextstep.subway.line.exception;

import nextstep.subway.station.domain.Station;

public class WrongUpStationException extends RuntimeException {

    public WrongUpStationException(Station station) {
        super("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다. 현재 하행 종점역: " + station.getName() + "(" + station.getId() + ")");
    }
}
