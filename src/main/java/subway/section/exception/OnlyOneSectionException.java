package subway.section.exception;

import subway.common.exception.SubwayException;

import static subway.common.exception.SubwayExceptionPurpose.DELETE;

public class OnlyOneSectionException extends SubwayException {

    public OnlyOneSectionException() {
        super(
                DELETE,
                "/lines/sections",
                "지하철 노선에 상, 구간이 두개 이상이어야 합니다."
        );
    }

}