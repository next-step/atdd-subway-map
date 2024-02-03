package subway.helper.fixture;

import subway.controller.dto.SectionCreateRequestBody;

public class SectionFixture {
    public static SectionCreateRequestBody 추가구간(Long upStationId, Long downStationId) {
        return new SectionCreateRequestBody(
                upStationId, downStationId, 10
        );
    }
}
