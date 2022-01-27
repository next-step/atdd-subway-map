package nextstep.subway.handler.validator;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;
import nextstep.subway.handler.error.custom.BusinessException;

import static nextstep.subway.handler.error.custom.ErrorCode.*;

public class SectionValidator {

    public static void proper(Line line, Station upStation, Station downStation, int distance) {
        validateUpStation(line, upStation);
        validateDownStation(line, downStation);
        validateDistance(distance);
    }

    private static void validateUpStation(Line line, Station upStation) {
        if (line.isEmptySections() || line.hasAnyMatchedDownStation(upStation)) {
            return;
        }
        throw new BusinessException(NOT_VALID_UP_STATION);
    }

    private static void validateDownStation(Line line, Station downStation) {
        if (line.hasStation(downStation)) {
            throw new BusinessException(NOT_VALID_DOWN_STATION);
        }
    }

    private static void validateDistance(int distance) {
        if (distance < 1) {
            throw new BusinessException(DISTANCE_CAN_NOT_SMALL_THAN_ONE);
        }
    }
}
