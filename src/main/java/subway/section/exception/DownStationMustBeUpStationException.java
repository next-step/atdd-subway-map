package subway.section.exception;

import subway.common.exception.SubwayException;

import static subway.common.exception.SubwayExceptionPurpose.CREATE;

public class DownStationMustBeUpStationException extends SubwayException {

    public DownStationMustBeUpStationException() {
        super(
                CREATE,
                "/lines/sections",
                "구간의 상행 역은 노선에 등록된 하행 종점역이어야 합니다."
        );
    }

}
