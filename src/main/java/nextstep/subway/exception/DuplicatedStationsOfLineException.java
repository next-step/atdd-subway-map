package nextstep.subway.exception;

import nextstep.subway.domain.Station;

public class DuplicatedStationsOfLineException extends IllegalArgumentException{
    private static final String MESSAGE = "추가하는 노선의 종점/기점 의 역이 달라야 합니다.";

    public DuplicatedStationsOfLineException(Station upStation, Station downStation) {
        super(MESSAGE + upStation.getName() +", " + downStation.getName());
    }
}
