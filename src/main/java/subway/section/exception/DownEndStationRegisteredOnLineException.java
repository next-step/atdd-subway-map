package subway.section.exception;

import subway.common.exception.SubwayException;

import static subway.common.exception.SubwayExceptionPurpose.DELETE;

public class DownEndStationRegisteredOnLineException extends SubwayException {

    public DownEndStationRegisteredOnLineException() {
        super(
                DELETE,
                "/lines/sections",
                "지하철 노선에 등록된 하행 종점역이어야 합니다."
        );
    }

}