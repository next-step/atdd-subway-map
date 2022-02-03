package nextstep.subway.handler.validator;

import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.station.Station;
import nextstep.subway.handler.error.custom.BusinessException;

import static nextstep.subway.handler.error.custom.ErrorCode.*;

public class SectionValidator {

    public static void validDistance(int distance) {
        if (distance < 1) {
            throw new BusinessException(DISTANCE_CAN_NOT_SMALL_THAN_ONE);
        }
    }

    public static void existsOnlyOneStation(Line line, Station upStation, Station downStation) {
        // 두 역이 모두 구간에 포함된 경우
        if (existsStation(line, upStation) && existsStation(line, downStation)) {
            throw new BusinessException(STATIONS_ALL_EXIST);
        }
        // 두 역이 모두 구간에 없는 경우
        if (notExistsStation(line, upStation) && notExistsStation(line, downStation)) {
            throw new BusinessException(STATIONS_ALL_NOT_FOUND);
        }
    }

    private static boolean existsStation(Line line, Station station) {
        return line.hasStation(station);
    }

    private static boolean notExistsStation(Line line, Station station) {
        return !line.hasStation(station);
    }
}