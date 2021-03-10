package nextstep.subway.station.exception;

import nextstep.subway.common.SystemException;

public class StationNotFoundException extends SystemException {

    public StationNotFoundException(Long stationId) {
        super("지하철 역을 찾을 수 없습니다.(stationId = %d)", stationId);
    }

}
