package subway.fixture;

import subway.line.dto.request.SaveLineRequestDto;
import subway.line.dto.request.UpdateLineRequestDto;

public class LineFixture {

    public static final SaveLineRequestDto 신분당선을_생성한다(Long upStationId, Long downStationId) {
        return SaveLineRequestDto.builder()
                .name("신분당선")
                .color("#f5222d")
                .distance(15)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .build();
    }

    public static final SaveLineRequestDto 경춘선을_생성한다(Long upStationId, Long downStationId) {
        return SaveLineRequestDto.builder()
                .name("경춘선")
                .color("#13c2c2")
                .distance(25)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .build();
    }

    public static final UpdateLineRequestDto 수정한_신분당선 =
            UpdateLineRequestDto.builder()
                    .name("수정한 신분당선")
                    .color("#cf1322")
                    .build();

}
