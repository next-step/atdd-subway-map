package nextstep.subway.section;

import nextstep.subway.section.dto.SectionRequest;

public class SectionHelper {

    public static SectionRequest Section_요청_만들기(Long downStationId, Long upStationId, int distance){
        return new SectionRequest(downStationId, upStationId, distance);

    }
}
