package subway.fixture;

import subway.line.dto.request.SaveLineRequestDto;
import subway.line.dto.request.UpdateLineRequestDto;

public class LineFixture {

    public static final SaveLineRequestDto SINBUNDANG_LINE =
            SaveLineRequestDto.builder()
                    .name("신분당선")
                    .color("#f5222d")
                    .distance(12)
                    .build();

    public static final SaveLineRequestDto GYEONGCHUN_LINE =
            SaveLineRequestDto.builder()
                    .name("경춘선")
                    .color("#13c2c2")
                    .distance(25)
                    .build();

    public static final UpdateLineRequestDto UPDATE_SINBUNDANG_LINE =
            UpdateLineRequestDto.builder()
                    .name("업데이트한 신분당선")
                    .color("#cf1322")
                    .build();

}
