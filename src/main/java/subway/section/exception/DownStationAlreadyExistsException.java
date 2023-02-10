package subway.section.exception;

import subway.common.exception.SubwayException;

import static subway.common.exception.SubwayExceptionPurpose.*;

public class DownStationAlreadyExistsException extends SubwayException {

    public DownStationAlreadyExistsException() {
        super(
                CREATE,
                "/lines/sections",
                "구간의 하행 역은 노선에 등록되지 않은 역이어야 합니다."
        );
    }

}
