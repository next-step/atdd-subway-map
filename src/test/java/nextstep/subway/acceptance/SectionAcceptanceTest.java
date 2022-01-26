package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.step.LineSteps;
import nextstep.subway.acceptance.step.SectionSteps;
import nextstep.subway.acceptance.step.StationSteps;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class SectionAcceptanceTest extends AcceptanceTest {
    final String 이름 = "name";
    final String 번호 = "id";

    final Map<String, String> 광교역 = Map.of(이름, "광교역");
    final Map<String, String> 양재역 = Map.of(이름, "양재역");
    final Map<String, String> 판교역 = Map.of(이름, "판교역");

    /**
     * When 지하철 구간 생성을 요청하면
     * Then 지하철 구간 생성이 성공한다.
     */
    @DisplayName("지하철 구간 생성")
    @Test
    void createSection() {
        final int distance = 100;

        // given
        final Long upStationId = StationSteps.지하철_역_생성_요청(광교역).jsonPath().getLong(번호);
        final Long downStationId = StationSteps.지하철_역_생성_요청(양재역).jsonPath().getLong(번호);
        final Long sectionDownStationId = StationSteps.지하철_역_생성_요청(판교역).jsonPath().getLong(번호);

        // when
        final Long lineId = LineSteps.지하철_노선_생성_요청("신분당선", "bg-red-600", upStationId, downStationId, distance)
                .jsonPath().getLong(번호);

        ExtractableResponse<Response> createResponse = SectionSteps.지하철_구간_생성_요청(lineId, downStationId, sectionDownStationId, distance);

        // then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header("Location")).isNotBlank();
    }
}
