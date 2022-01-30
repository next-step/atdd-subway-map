package nextstep.subway.acceptance;

import nextstep.subway.fixture.SectionFixture;
import nextstep.subway.fixture.StationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.step.LineStep.신분당선_생성_완료;
import static nextstep.subway.acceptance.step.SectionStep.*;
import static nextstep.subway.acceptance.step.StationStep.역_생성_요청;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에 구간 등록")
    @Test
    void createSection() {
        // given
        var stationParams = StationFixture.역삼역;
        역_생성_요청(stationParams);

        var lineCreateResponse = 신분당선_생성_완료();

        // when
        var lineUri = lineCreateResponse.header("Location");
        var params = SectionFixture.of(2L, 3L, 10);
        var response = 구간_등록_요청(lineUri, params);

        // then
        구간_등록_성공(response);
    }

    @DisplayName("지하철 노선에 구간 삭제")
    @Test
    void deleteSection() {
        // given
        var stationParams = StationFixture.역삼역;
        역_생성_요청(stationParams);

        var lineCreateResponse = 신분당선_생성_완료();
        var params = SectionFixture.of(2L, 3L, 10);

        var lineUri = lineCreateResponse.header("Location");
        구간_등록_요청(lineUri, params);

        // when
        var response = 구간_삭제_요청(lineUri, 3L);

        // then
        구간_삭제_성공(response);
    }
}
