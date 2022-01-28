package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step.LineStep;
import nextstep.subway.acceptance.step.SectionStep;
import nextstep.subway.acceptance.step.StationStep;
import nextstep.subway.fixture.LineFixture;
import nextstep.subway.fixture.SectionFixture;
import nextstep.subway.fixture.StationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에 구간 등록")
    @Test
    void createSection() {
        // given
        var stationParams = StationFixture.역삼역;
        StationStep.역_생성_요청(stationParams);

        var lineCreateResponse = 신분당선_생성_완료();
        var params = SectionFixture.of(2L, 3L, 10);

        // when
        var lineUri = lineCreateResponse.header("Location");
        var response = SectionStep.구간_생성_요청(lineUri, params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


    private ExtractableResponse<Response> 신분당선_생성_완료() {
        var station1 = StationFixture.신논현역;
        var station2 = StationFixture.강남역;
        StationStep.역_생성_요청(station1);
        StationStep.역_생성_요청(station2);

        var params = LineFixture.신분당선;
        return LineStep.노선_생성_요청(params);
    }

}
