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
        역_생성_요청(StationFixture.역삼역);

        var 노선_생성_응답 = 신분당선_생성_완료();

        // when
        var lineUri = 노선_생성_응답.header("Location");
        var 구간1 = SectionFixture.of(2L, 3L, 10);
        var 구간_등록_응답 = 구간_등록_요청(lineUri, 구간1);

        // then
        구간_등록_성공(구간_등록_응답);
    }

    @DisplayName("지하철 노선에 구간 삭제")
    @Test
    void deleteSection() {
        // given
        역_생성_요청(StationFixture.역삼역);

        var 노선_생성_응답 = 신분당선_생성_완료();
        var 구간1 = SectionFixture.of(2L, 3L, 10);

        var lineUri = 노선_생성_응답.header("Location");
        구간_등록_요청(lineUri, 구간1);

        // when
        var 구간_삭제_응답 = 구간_삭제_요청(lineUri, 3L);

        // then
        구간_삭제_성공(구간_삭제_응답);
    }
}
