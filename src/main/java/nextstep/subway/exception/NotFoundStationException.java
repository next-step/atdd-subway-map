package nextstep.subway.exception;

import nextstep.subway.domain.Station;

public class NotFoundStationException extends IllegalArgumentException {
    private static final String MESSAGE = "지하철역이 존재하지 않습니다. 입력값::";

    public NotFoundStationException(long id) {
        super(MESSAGE + id);
    }

    public NotFoundStationException(Station station) {
        super(MESSAGE + station.getId());
    }
}
